import { Component, OnInit, ChangeDetectorRef, Renderer2, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { fromLonLat, toLonLat } from 'ol/proj';
import { Tile as TileLayer } from 'ol/layer';
import { OSM } from 'ol/source';
import { Map } from 'ol';
import { View } from 'ol';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import { Coordinate } from 'ol/coordinate';
import { Style, Icon, Circle as CircleStyle, Fill, Stroke } from 'ol/style';
import { PostService } from '../post-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-creation',
  templateUrl: './post-creation.component.html',
  styleUrls: ['./post-creation.component.css']
})
export class PostCreationComponent implements OnInit {
  createPostForm!: FormGroup;
  selectedFile: File | null = null;
  previewImage$ = new BehaviorSubject<string | null>(null);
  addressLabel: string | null = null;

  center: [number, number] = [20.4489, 44.7866]; // Coordinates for Belgrade
  zoom = 12;

  map: Map | undefined;
  markerFeature: Feature | undefined;
  markerLayer: VectorLayer | undefined;
  vectorSource: VectorSource = new VectorSource(); // Ensure vector source is always initialized

  currentCoordinates: [number, number] = this.center; // Initialize with the default coordinates
  @ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef;

  constructor(
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
    private renderer: Renderer2,
    private postService: PostService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.createPostForm = this.fb.group({
      description: ['', [Validators.required]],
      image: [null, Validators.required],
      location: ['', Validators.required],   // Add location to the form group
    });

    // Log the initial coordinates
    console.log('Initial Coordinates:', this.currentCoordinates);
  }

  ngAfterViewInit(): void {
    this.initializeMap();
  }

  private initializeMap(): void {
    const osmLayer = new TileLayer({
      source: new OSM(),
    });

    const view = new View({
      center: fromLonLat(this.center),
      zoom: this.zoom,
      constrainResolution: false,
    });

    this.map = new Map({
      target: this.mapContainer.nativeElement,
      layers: [osmLayer],
      view: view,
    });

    // Initialize marker layer with vector source
    this.markerLayer = new VectorLayer({
      source: this.vectorSource,
    });
    this.map.addLayer(this.markerLayer);

    // Add initial marker at the specified center and get the address
    this.addMarker(fromLonLat(this.center));
    //this.updateAddressLabel(this.center);

    // Update marker and address on map click
    this.map.on('click', (event) => this.onMapClick(event));
  }

  private onMapClick(event: any): void {
    const coordinates = event.coordinate as Coordinate;
    
    // Update the current coordinates with the clicked location
    this.currentCoordinates = toLonLat(coordinates) as [number, number];

    // Log the updated coordinates
    console.log('Updated Coordinates:', this.currentCoordinates);

    // Add or update the marker at the clicked position
    this.addMarker(coordinates);
  
    // Convert the clicked coordinates to lon/lat and update the address label
    const lonLatCoords = toLonLat(coordinates) as [number, number];
    this.updateAddressLabel(lonLatCoords);
  
    // Update the form's location field with the address
    this.createPostForm.patchValue({
      location: this.addressLabel || ''  // Set the form control value with the address or an empty string
    });
  
    // Re-center the map on the clicked coordinates
    this.map?.getView().setCenter(coordinates);
  }
  
  
  private addMarker(coordinates: Coordinate): void {
    // Remove any existing markers
    this.vectorSource.clear();
  
    // Create a new marker feature
    const markerFeature = new Feature({
      geometry: new Point(coordinates),
    });
  
    // Define the marker style with both icon and circle around it
    const markerStyle = new Style({
      // Create a circle effect around the marker
      image: new CircleStyle({
        radius: 12, // Radius of the circle (in pixels)
        fill: new Fill({
          color: 'rgba(255, 0, 0, 0.5)', // Color of the circle (semi-transparent red)
        }),
        stroke: new Stroke({
          color: 'rgba(255, 0, 0, 1)', // Stroke color of the circle (solid red)
          width: 2, // Stroke width (optional)
        }),
      }),
    });
  
    // Create a separate icon for the marker
    const iconStyle = new Style({
      image: new Icon({
        anchor: [0.5, 1], // Position the icon at the bottom
        src: 'https://upload.wikimedia.org/wikipedia/commons/4/4f/Red_pin_icon.svg',
        scale: 0.5, // Adjust the scale of the pin
      }),
    });
  
    // Apply both the circle and icon styles to the feature
    markerFeature.setStyle([markerStyle, iconStyle]);
  
    // Add the feature to the vector source
    this.vectorSource.addFeature(markerFeature);
  }

  private async updateAddressLabel(coordinates: [number, number]): Promise<void> {
    const [lon, lat] = coordinates;
    const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`);
    const data = await response.json();
    
    // Update the location field with the fetched address
    const address = data.display_name || 'Address not found';
    this.createPostForm.get('location')?.setValue(address);
    // Set the address into the form control (location field)
  this.createPostForm.patchValue({
    location: data.display_name || 'Address not found'
  });
    this.createPostForm.get('location')?.markAsTouched();
  }

  async updateMapLocation(event: Event): Promise<void> {
    const input = (event.target as HTMLInputElement).value;
    const coordinates = await this.getCoordinatesFromLocation(input);
    if (coordinates) {
      const olCoordinates = fromLonLat(coordinates);
      this.map?.getView().setCenter(olCoordinates);
      this.map?.getView().setZoom(13);
      this.addMarker(olCoordinates);
      this.updateAddressLabel(coordinates);
    }
  }

  async getCoordinatesFromLocation(location: string): Promise<[number, number] | null> {
    try {
      const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${location}`);
      const data = await response.json();
      if (data && data.length > 0) {
        const { lat, lon } = data[0];
        return [parseFloat(lon), parseFloat(lat)];
      }
      return null;
    } catch (error) {
      console.error('Geocoding failed:', error);
      return null;
    }
  }

  onFileSelect(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.previewImage$.next(URL.createObjectURL(file));
      // Manually set the file to the form control for image
    this.createPostForm.get('image')?.setValue(file);
    }
  }

  onSubmit(): void {
    if (this.createPostForm.invalid) {
      return; // Prevent form submission if invalid
    }

    const postData = this.createPostForm.value;
    
    // Prepare the form data to send to the backend
    const formData = {
      creatorId: 1,  // Example creatorId, replace with actual value
      creationTime: new Date().toISOString(),
      description: postData.description,
      location: {
        latitude: this.currentCoordinates[0],  // Set latitude
        longitude: this.currentCoordinates[1]  // Set longitude
      }
    };

    // Call createPost method from PostService
    this.postService.createPost(formData).subscribe({
      next: (response) => {
        console.log('Post created successfully', response);
        this.router.navigate(['/posts']);  // Navigate to posts list or any other desired route
      },
      error: (error) => {
        console.error('Error creating post', error);
      }
    });
  }

  onClearImage(): void {
    this.selectedFile = null;
    this.previewImage$.next(null);
    this.createPostForm.get('image')?.reset();
  }
}
