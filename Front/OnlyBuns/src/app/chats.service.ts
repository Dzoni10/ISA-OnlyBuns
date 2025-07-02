import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from './env/enviroment';
import { Stomp } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Subject } from 'rxjs';
import { Chat } from './model/chat.model';
import { ChatMessage } from './model/chatMessage.model';
import { ChatParticipant } from './model/chatParticipant.model';


@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private stompClient: any;
  private chatId: number | null = null;
  private messageSubject = new Subject<ChatMessage>();
  public messages$ = this.messageSubject.asObservable();  // Observable za praćenje novih poruka

  constructor(private http: HttpClient) {}

  // Kreiranje novog četa
  addChat(chat: any): Observable<any> {
    return this.http.post<any>(environment.apiHost + "chats/create", chat);
  }

  // Dohvatanje svih čatova za korisnika
  getChatsByUser(username: string): Observable<Chat[]> {
    return this.http.get<Chat[]>(`${environment.apiHost}chats/user/${username}`);
  }

  // Dodavanje korisnika u čat
// Dodavanje korisnika u čat
  addUserToChat(chatId: number, participant: ChatParticipant): Observable<void> {
  const payload = { 
    username: participant.username,
    joinTime: participant.joinTime // Šaljemo datum pridruživanja korisnika
  };
  return this.http.post<void>(`${environment.apiHost}chats/${chatId}/add-user`, payload);
}

  checkUserExist(username: string): Observable<void> {
    return this.http.get<void>(`${environment.apiHost}chats/check-user/${username}`);
  }

  // Uklanjanje korisnika iz čata
  removeUserFromChat(chatId: number, username: string): Observable<void> {
    const payload = { username };
    return this.http.delete<void>(`${environment.apiHost}chats/${chatId}/remove-user`, {
      body: payload,
    });
  }
  canAccessChat(chatId: number, username: string): Observable<boolean> {
    return this.http.get<boolean>(`${environment.apiHost}chats/${chatId}/access?username=${username}`);
  }

  getChatById(chatId: number): Observable<Chat> {
    return this.http.get<Chat>(`${environment.apiHost}chats/${chatId}`);
  }

  // Dohvatanje poslednjih 10 poruka za određeni čat
  getLastMessages(chatId: number): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(
      `${environment.apiHost}chats/${chatId}/last-messages`
    );
  }


 // WebSocket inicijalizacija
 connectWebSocket(chatId: number): void {
  this.chatId = chatId;
  const socketFactory = () => new SockJS('http://localhost:8080/ws');
  this.stompClient = Stomp.over(socketFactory);
  

  this.stompClient.connect({}, (frame: any) => {
    console.log('Connected: ' + frame);
    // Subscribe na kanal za prijem poruka
    this.stompClient.subscribe(`/topic/chat/${chatId}`, (message: any) => {
      this.handleMessage(message);
    });
  });
}
 // Funkcija za slanje poruke preko WebSocket-a
sendWebSocketMessage(message: ChatMessage): void {
  if (this.stompClient && this.chatId) {
    this.stompClient.send(`/app/chat/${message.chatId}/send`, {}, JSON.stringify(message));
  }
}
  // Funkcija za rukovanje dolaznim porukama
  handleMessage(message: any): void {
    const chatMessage: ChatMessage = JSON.parse(message.body);
    console.log('New message received: ', chatMessage);
    this.messageSubject.next(chatMessage); // obaveštava Angular komponentu da ažurira UI
  }

  // Zatvaranje WebSocket veze
  disconnectWebSocket(): void {
    if (this.stompClient) {
      this.stompClient.disconnect(() => {
        console.log('Disconnected from WebSocket');
      });
    }
  }

}
