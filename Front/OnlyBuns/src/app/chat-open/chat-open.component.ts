import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ChatService } from '../chats.service';
import { Chat } from '../model/chat.model';
import { LoginService } from '../login-service.service';
import { RegisterUser } from '../model/register-user.model';
import { formatDistanceToNow } from 'date-fns';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ChatMessage } from '../model/chatMessage.model';
import { Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy, AfterViewInit,AfterViewChecked, ViewChild, ElementRef } from '@angular/core';
import { ChatParticipant } from '../model/chatParticipant.model';

@Component({
  selector: 'app-chat-open',
  templateUrl: './chat-open.component.html',
  styleUrls: ['./chat-open.component.css'],
  standalone: false
})
export class ChatOpenComponent implements OnInit, OnDestroy  {

  @ViewChild('participantsSection') private participantsSection!: ElementRef;

  // Ova funkcija se poziva svaki put nakon što se prikaz komponenta promeni
 

  // Funkcija za pomeranje scroll-a na dno
   scrollToBottom(): void {
    try {
      const element = this.participantsSection.nativeElement;
      element.scrollTop = element.scrollHeight;
    } catch (err) {
      console.error(err);
    }
    this.scrollDownButton=false
    setTimeout(() => {
      this.newMessageId = null;
    }, 1000);
  } 

  chatIdforMess= 0;
  chatId: number | null = null;
  selectedChat: Chat | null = null; 
  registerUser: RegisterUser | undefined;
  username='';
  newMessage = '';
  showParticipants = false;
  showAddUser = false;
  scrollDownButton = false;
  newParticipant = '';
  newMessageId: number | null = null;

  private messageSubscription!: Subscription;

constructor(private loginService: LoginService,private snackBar: MatSnackBar, private route: ActivatedRoute, private router: Router, private chatService: ChatService){}

  ngOnInit(): void {
    const chatIdParam = this.route.snapshot.paramMap.get('chatId');
    this.chatId = chatIdParam ? parseInt(chatIdParam, 10) : null;
    this.loginService.user$.subscribe({
      next: (user) =>{
        if(user&& user.id){
          this.loadUserData(user.id);
        }
      },
      error: (error)=>{
        alert("Greska pri ucitavanju profila");
      }
    }) 

    this.messageSubscription = this.chatService.messages$.subscribe((message: ChatMessage) => {
      if (this.selectedChat) {
        this.selectedChat.messages.push(message);  // Dodaj novu poruku u listu poruka
        this.newMessageId = message.id;

        if(this.username!=message.username){
          this.scrollDownButton=true;
        }
        if(this.username==message.username){
          setTimeout(() => {
            this.scrollToBottom();
          }, 0);
        }
      }
    });
  }
  
  loadChat(chatId : number): void {
    this.chatService.getChatById(chatId).subscribe({
      next: (chat) => {
        this.selectedChat = chat;
        setTimeout(() => {
          this.scrollToBottom();
        }, 0);
      },
      error: (err) => {
        console.error('Greška pri dohvatanju četa:', err);
      },
    });

    this.chatService.connectWebSocket(chatId); // Povezivanje sa WebSocket serverom

  }

  loadUserData(userId:number): void{
    this.loginService.getUserForProfile(userId).subscribe({
      next: (user) => {
        this.registerUser = user;
      this.username=this.registerUser.username
      if (this.chatId !== null && !isNaN(this.chatId)) {

        this.chatService.canAccessChat(this.chatId, this.username).subscribe({
          next: (canAccess) => {
            if (canAccess && this.chatId !== null ) {
              this.loadChat(this.chatId);
              this.chatIdforMess=this.chatId;
          }else this.openSnackBar("You don't have premission to this chat!", "Close"); 
        }
      })
      } 
     },
      error: (error) => {
        console.error('Greška prilikom učitavanja profila', error);
      }
    });
  }
  onScroll(): void {
    const element = this.participantsSection.nativeElement;
    if (element.scrollHeight - element.scrollTop <= element.clientHeight + 10) 
      this.scrollDownButton = false;
  }
  get filteredMessages() {
    if (!this.selectedChat || !this.username) return [];
  
    const participant = this.selectedChat.participants.find(p => p.username === this.username);
    if (!participant) return [];
    const userJoinTime = new Date(participant.joinTime);
    // Sortiramo poruke po creationTime-u
    const sortedMessages = [...this.selectedChat.messages].sort((a, b) =>
      new Date(a.creationTime).getTime() - new Date(b.creationTime).getTime()
    );
    // Pronađemo indeks prve poruke koja je novija od joinTime
    const firstNewMessageIndex = sortedMessages.findIndex(
      msg => new Date(msg.creationTime) > userJoinTime
    );
    // Ako nema nijedne poruke posle join-a, vrati samo poslednjih 10 pre join-a
    if (firstNewMessageIndex === -1) {
      return sortedMessages.slice(-10);
    }
    // Poruke posle joinTime
    const messagesAfterJoin = sortedMessages.slice(firstNewMessageIndex);
    // Poruke pre joinTime (max 10 poslednjih pre tog momenta)
    const messagesBeforeJoin = sortedMessages.slice(
      Math.max(0, firstNewMessageIndex - 10),
      firstNewMessageIndex
    );
  
    return [...messagesBeforeJoin, ...messagesAfterJoin];
  }
  
  
  

  getTimeAgo(date: Date): string {
    return formatDistanceToNow(date) + ' ago';  
  }
  toggleParticipants() {
    this.showParticipants = !this.showParticipants;
  }

  removeParticipant(participant: string) {
    if (!this.chatId) return;
  
    const isConfirmed = window.confirm('Are you sure you want to remove this user?');
    
    if (isConfirmed) {
      this.chatService.removeUserFromChat(this.chatId, participant).subscribe({
        next: () => {
          if(this.chatId)
          this.loadChat(this.chatId);
        },
        error: (err) => {
          console.error("Greška pri uklanjanju korisnika:", err);
        }
      });
    }
  }
  
  addParticipant(newParticipant: string) {
    if (!this.chatId) return;
    const newPerticipant: ChatParticipant = {
      username: newParticipant,
      joinTime: new Date() 
      }
    this.chatService.addUserToChat(this.chatId, newPerticipant).subscribe({
      next: () => {
        this.toggleAddUser();
        if (this.chatId) this.loadChat(this.chatId);
        this.openSnackBar("The user has been successfully added to the chat!", "OK"); // Uspešna poruka
      },
      error: (err) => {
        
        let errorMessage = "Došlo je do greške pri dodavanju korisnika.";
        if (err.error && err.error.message) {
          errorMessage = err.error.message; // Uzima poruku sa servera
        }
        
        this.openSnackBar(errorMessage, "Close"); // Prikazuje poruku kao obaveštenje
      }
    });
  }
  
  toggleAddUser() {
    this.showAddUser = !this.showAddUser;
    if (!this.showAddUser) {
        this.newParticipant = ''; // Reset input when closing
    }
}
confirmAddParticipant() {
  if (this.newParticipant.trim()) {
      this.addParticipant(this.newParticipant);
      this.newParticipant = '';
      this.showAddUser = false;
  }
}

openUsersProfile(username: string): void {
  this.router.navigate(['user-profile', username]).then(() => {
    window.location.reload(); 
  });
}
openSnackBar(message: string, action: string = 'OK') {
  this.snackBar.open(message, action, {
    duration: 6000,  
    horizontalPosition: 'center',
    verticalPosition: 'top',
    panelClass: ['snackbar-error'] // Dodaj prilagođenu klasu ako želiš stilizaciju
  });
}
  ngOnDestroy(): void {
    if (this.chatId !== null) {
      this.messageSubscription.unsubscribe();  // Prekini pretplatu pri uništavanju komponente
      this.chatService.disconnectWebSocket(); // Zatvaranje WebSocket veze kada komponenta bude uništena
    }
  }

  sendMessage(): void {
    if (this.newMessage.trim()) {
      const message: ChatMessage = {
        username: this.username, 
        text: this.newMessage,
        creationTime: new Date(),
        id: 0,
        chatId:this.chatIdforMess
      };
      this.newMessage = ''; 
      this.chatService.sendWebSocketMessage(message); // Slanje poruke putem WebSocket-a
    }
  }
}
