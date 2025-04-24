import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatWebSocketService {
  private stompClient: Client;
  private messageSubject = new BehaviorSubject<any>(null); // Observable za poruke
  
  constructor() {
    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8080/ws', // Adresa WebSocket servera
      reconnectDelay: 5000, // Automatsko reconnect-ovanje
    });

    this.stompClient.onConnect = () => {
      console.log("Connected to WebSocket");
    };

    this.stompClient.activate(); // Pokreće konekciju
  }

  // Metoda za pretplatu na poruke iz određenog četa
  subscribeToChat(chatId: number) {
    this.stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
      this.messageSubject.next(JSON.parse(message.body));
    });
  }

  // Metoda za slanje poruke
  sendMessage(chatId: number, username: string, text: string) {
    const chatMessage = { username, text };
    this.stompClient.publish({
      destination: `/app/chat/${chatId}/send`, 
      body: JSON.stringify(chatMessage)
    });
  }

  // Observable za slušanje poruka
  getMessages() {
    return this.messageSubject.asObservable();
  }
}
