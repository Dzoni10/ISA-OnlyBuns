import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { RegisterUser } from '../model/register-user.model';
import { ChatService } from '../chats.service';
import { formatDistanceToNow } from 'date-fns';
import { trigger, transition, style, animate } from '@angular/animations';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Chat } from '../model/chat.model';

@Component({
  selector: 'app-chats',
  templateUrl: './chats.component.html',
  styleUrls: ['./chats.component.css'],
  standalone: false,
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [style({ opacity: 0 }), animate('400ms ease-in', style({ opacity: 1 }))]),
      transition(':leave', [animate('400ms ease-out', style({ opacity: 0 }))])
    ])
  ]
})

export class ChatsComponent implements OnInit {
  registerUser: RegisterUser | undefined;
  chats: Chat[] = [];
  errorMessage: string | null = null;
  isModalOpen: boolean = false; // Kontrola prikaza modala
  newChatTitle: string = '';
  showParticipantInput: boolean = false;
  newParticipant: string = '';
  participants: string[] = [];
  isLoading: boolean = true;

  
  constructor(private loginService: LoginService,private snackBar: MatSnackBar, private chatService: ChatService, private router: Router){}

  @ViewChild('titleInput') titleInputRef!: ElementRef;
  @ViewChild('participantInput') participantInputRef!: ElementRef;

  ngAfterViewInit() {
    this.focusTitleInput();
  }
  ngOnChanges() {
    this.focusTitleInput();
  }
  focusTitleInput() {
    if (this.isModalOpen && this.titleInputRef) {
      setTimeout(() => {
        this.titleInputRef.nativeElement.focus();
      }, 0); // omogućava Angularu da završi render
    }
  }

 
  ngAfterViewChecked() {
    if (this.showParticipantInput) {
      this.focusParticipantInput();
    }
  }
  private focusParticipantInput() {
    if (this.participantInputRef?.nativeElement) {
      this.participantInputRef.nativeElement.focus();
    }
  }
  ngOnInit(): void {

    this.loginService.user$.subscribe({
      next: (user) =>{
        if(user&& user.id){
          this.loadUserData(user.id);
        }
      },
      error: (error)=>{
        console.log("Greska prilikom dobijanja korisnika", error);
        alert("Greska pri ucitavanju profila");
      }
    })  

     
  }

  openNewChatModal(): void {
    this.isModalOpen = true;
    setTimeout(() => {
      this.focusTitleInput();
    }, 0);
  }

  // Zatvara modal
  closeModal(): void {
    this.isModalOpen = false;
    this.showParticipantInput = false;
  }

  addParticipant() {
    const trimmed = this.newParticipant.trim();
    if (!trimmed) return;
    if (this.participants.includes(trimmed)) {
      this.openSnackBar('ℹ️\u00A0\u00A0\u00A0Participant already added!\u00A0\u00A0\u00A0ℹ️', 'Close'); 
      return;
    }
    if(this.newParticipant == this.registerUser?.username){
      this.openSnackBar('👑\u00A0\u00A0\u00A0You are admin of this chat!\u00A0\u00A0\u00A0👑', 'Close'); 
      return;
    }

    this.chatService.checkUserExist(trimmed).subscribe({
      next: () => {
        this.participants.push(trimmed);  // Dodajemo korisnika u lokalnu listu
        this.newParticipant = '';  // Resetujemo input polje
      },
      error: (err) => {
        let errorMessage = "Došlo je do greške pri dodavanju korisnika.";
        if (err.error && err.error.message) {
          errorMessage = err.error.message; // Uzima poruku sa servera ako postoji
        }
        this.openSnackBar('❌\u00A0'+errorMessage+'\u00A0❌', "Close"); 
      }
    });
  }
  removeParticipant(index: number) {
    this.participants.splice(index, 1);
  }
  createChat() {
    if (!this.newChatTitle.trim() || this.participants.length === 0) {
      this.openSnackBar('Enter chats title');
      return;
    }
    
    if (!this.registerUser || !this.registerUser.username)   return;

    const newChat: Omit<Chat, 'id'> = {
      name: this.newChatTitle,
      adminUsername: this.registerUser?.username || '',
      participants: this.participants.map(username => ({
        username: username,
        joinTime: new Date()  // Postavi trenutni datum i vreme
      })),
      messages: []
    };
    this.chatService.addChat(newChat).subscribe({
      next: (chat: Chat) => {
      this.openSnackBar(`Chat "${chat.name}" was successfully created.`, 'Open', () => this.openChat(chat.id)  );  
       if (!this.registerUser || !this.registerUser.username)   return;

        // Reset polja i zatvaranje modala
        this.newChatTitle = '';
        this.participants = [];
        this.newParticipant = '';
        this.isModalOpen = false;
        this.showParticipantInput = false;
        this.loadChats(this.registerUser.username);
      },
      error: (error) => {
        this.openSnackBar('Došlo je do greške prilikom kreiranja čata.');
      }
    });

  }
  

  
  loadChats(username: string): void {
    this.chatService.getChatsByUser(username).subscribe({
      next: (chats) => {
        this.chats = chats;
        this.isLoading=false;
      },
      error: (error) => {
        alert('Greška prilikom dohvatanja čatova:');
        this.errorMessage = 'Došlo je do greške pri dohvatanju čatova.';
      },
    });
  }
    
  loadUserData(userId:number): void{
    this.loginService.getUserForProfile(userId).subscribe({
      next: (user) => {
        this.registerUser = user;
      if (this.registerUser?.username) 
        this.loadChats(this.registerUser.username);
      },
      error: (error) => {
        console.error('Greška prilikom učitavanja profila', error);
      }
    });
  }

  getTimeAgo(date: Date): string {
    return formatDistanceToNow(date) + ' ago';  
  }

  openChat(chatId: number): void {
    this.router.navigate(['chat', chatId]);
  }
  openSnackBar(message: string, action: string = 'OK', onAction?: () => void) {
    const snackBarRef = this.snackBar.open(message, action, {
      duration: 7000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['snackbar-error']
    });
  
    // Ako je akcija "Open", pozovi onAction()
    if (action === 'Open' && onAction) {
      snackBarRef.onAction().subscribe(() => {
        onAction();
      });
    }
  }

}
