import { Component, OnInit,ChangeDetectorRef, Renderer2, ElementRef, ViewChild } from '@angular/core';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Registration } from '../model/registration.model';
import { RegisterUser } from '../model/register-user.model';
import { PagedResults } from '../model/paged-results.model';
import { Map } from 'ol';
import Feature from 'ol/Feature';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Coordinate } from 'ol/coordinate';
import { Style, Icon, Circle as CircleStyle, Fill, Stroke } from 'ol/style';
import { fromLonLat, toLonLat } from 'ol/proj';
import { Tile as TileLayer } from 'ol/layer';
import { OSM } from 'ol/source';
import { View } from 'ol';
import Point from 'ol/geom/Point';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  standalone: false

})
export class SignupComponent implements OnInit {

  center: [number, number] = [20.4489, 44.7866];
  zoom = 12;
  map: Map | undefined;
  markerFeature: Feature | undefined;
  markerLayer: VectorLayer | undefined;
  vectorSource: VectorSource = new VectorSource();
  currentCoordinates: [number, number] = this.center;
  addressLabel: string | null = null;

  @ViewChild('mapContainer', { static: true }) mapContainer!: ElementRef;

  constructor(private loginService:LoginService,private router: Router){}

  ngOnInit(): void{}


  registrationForm = new FormGroup({
    name:new FormControl('',[Validators.required]),
    surname:new FormControl('',[Validators.required]),
    username:new FormControl('',[Validators.required]),
    password:new FormControl('',[Validators.required]),
    password2:new FormControl('',[Validators.required]),
    email:new FormControl('',[Validators.required]),
    adress:new FormControl('',[Validators.required]),
  });

  register(): void{
    // const registration: Registration={
    //   name: this.registrationForm.value.name || "",
    //   surname: this.registrationForm.value.surname|| "",
    //   username: this.registrationForm.value.username || "",
    //   password: this.registrationForm.value.password || "",
    //   email: this.registrationForm.value.email || "",
    //   adress: this.registrationForm.value.adress || "",
    // };

    if(this.registrationForm.valid){
      if(this.registrationForm.value.password === this.registrationForm.value.password2){
        
        const addressText = this.registrationForm.value.adress || "";

        this.getCoordinatesFromLocation(addressText).then((coordinates)=>{

          if(!coordinates)
          {
            alert('Adresa nije pronadjena');
            return;
          }

          const [lon,lat] = coordinates;

      const registration: Registration={
      name: this.registrationForm.value.name || "",
      surname: this.registrationForm.value.surname|| "",
      username: this.registrationForm.value.username || "",
      password: this.registrationForm.value.password || "",
      email: this.registrationForm.value.email || "",
      adress: {latitude:lat,longitude:lon}
    };
          

        this.loginService.register(registration).subscribe({
        next: (usr)=>{
          alert("Mejl za aktivaciju je poslat, idite na mejl i potvrdite registraciju");
          this.registrationForm.reset();
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.log("Greska",err)
          switch(err.status){
            case 401:
              console.error("Došlo je do greške mejl vec postoji", err);
              alert("Mejl koji ste uneli vec postoji");
              break;
            case 409:
              console.error("Došlo je do greške korisnicko ime vec postoji");
              alert("Korisnicko ime koje ste uneli vec postoji");
              break;
            case 400:
              console.error("Došlo je do greške korisnicko ime i mejl vec postoje");
              alert("Korisnicko ime i mejl koji ste uneli vec postoje");
              break;
          }
        },
      });

      }).catch((err)=>{
        console.error("Greska pri geokodiranju",err);
        alert("Adresa nije pronadjena");
      });
    }
    else{
      alert("Ponovno unesena lozinka se ne poklapa");
    }
  }
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
      this.registrationForm.patchValue({
        adress: this.addressLabel || ''  // Set the form control value with the address or an empty string
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
    this.registrationForm.get('adress')?.setValue(address);
    // Set the address into the form control (location field)
  this.registrationForm.patchValue({
    adress: data.display_name || 'Address not found'
  });
    this.registrationForm.get('adress')?.markAsTouched();
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
  
    async getCoordinatesFromLocation(adress: string): Promise<[number, number] | null> {
      try {
        const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${adress}`);
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


}

