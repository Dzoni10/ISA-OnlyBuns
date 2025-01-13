import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { Client} from '@stomp/stompjs';
import { RecommendedLocationsComponent } from './recommended-locations/recommended-locations.component';


@Injectable({
  providedIn: 'root'
})
export class LocationService {

  private stompClient!: Client;
  private readonly serverUrl = 'http://localhost:8080/ws';
  private mapComponent!: RecommendedLocationsComponent;

  connect(mapComponent: RecommendedLocationsComponent): void {
    this.mapComponent = mapComponent;
    const socket = new SockJS(this.serverUrl);

    this.stompClient = new Client({
      webSocketFactory: () => socket, // Povezivanje sa SockJS
      reconnectDelay: 5000, // Automatsko ponovno povezivanje
      debug: (str) => console.log('STOMP debug: ', str), // Debug log
    });
    
    

    this.stompClient.onConnect = () => {
      console.log('Povezano na WebSocket');
      this.stompClient.subscribe('/topic/locations', (message) => {
        const location: {latitude:number; longitude:number} = JSON.parse(message.body);
        console.log('Primljena lokacija: ', location);
        // Ažurirajte mapu sa koordinatama ovde
       // this.updateMap(location);
       this.mapComponent.addMarker(location.longitude,location.latitude);
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Greška u STOMP konekciji: ', frame.headers['message']);
      console.error('Detalji: ', frame.body);
    };

    this.stompClient.activate(); // Aktivirajte klijenta

  }
  // updateMap(location: Location) {
  //   // Vaša logika za ažuriranje mape na osnovu lokacije
  //   console.log('Ažuriranje mape sa lokacijom: ', location);
  // }
}
