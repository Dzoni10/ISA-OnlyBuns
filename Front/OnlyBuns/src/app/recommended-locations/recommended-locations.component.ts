import { Component, OnInit } from '@angular/core';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { Icon, Style,Text,Fill,Stroke } from 'ol/style';
import { fromLonLat } from 'ol/proj';
import { LocationService } from '../location.service';

@Component({
  selector: 'app-recommended-locations',
  templateUrl: './recommended-locations.component.html',
  styleUrls: ['./recommended-locations.component.css']
})
export class RecommendedLocationsComponent implements OnInit {
  map!: Map;
  vectorSource!: VectorSource;

  constructor(private locationService:LocationService){}

  ngOnInit(): void {

    this.vectorSource = new VectorSource();
    const vectorLayer = new VectorLayer({
      source: this.vectorSource,
      style: new Style({
        image: new Icon({
          src: 'https://cdn-icons-png.flaticon.com/512/684/684908.png', // Ikonica za post
          scale: 0.05,
        }),
      }),
    });

    this.map = new Map({
      target: 'map',
      layers: [
        new TileLayer({
          source: new OSM(),
        }),
        vectorLayer
      ],
      view: new View({
        center: [0, 0], // Koordinate za centar mape (EPSG:3857)
        zoom: 2, // Početni nivo uvećanja
      }),
    });

    this.locationService.connect(this);
  }

  // Funkcija za dodavanje markera na osnovu koordinata
  addMarker(longitude: number, latitude: number,name: string): void {
    const feature = new Feature({
      geometry: new Point(fromLonLat([longitude, latitude])),
      name:name
    });

    console.log(name);
    //const label = name;

    feature.setStyle(
      new Style({
        image: new Icon({
          src: 'https://cdn-icons-png.flaticon.com/512/684/684908.png', // Ikonica za post
          scale: 0.07,
        }),
        text: new Text({
          text: name, // Tekst koji će biti prikazan
          font: '12px Arial',
          fill: new Fill({
            color: '#000', // Boja teksta
          }),
          stroke: new Stroke({
            color: '#fff', // Obrub teksta za kontrast
            width: 2,
          }),
          offsetY: -35, // Pomeri tekst iznad markera
        }),
      })
    );

    this.vectorSource.addFeature(feature);
  }

}
