import { Component, OnInit, ChangeDetectorRef, Renderer2, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { faComments, faUser, faThumbsUp,faUserCircle, faCommentDots , faEdit, faTrash, faCheck, faTimes, faIcons, faPaperPlane} from '@fortawesome/free-solid-svg-icons';
import { FollowingService } from '../following.service';
import { Following } from '../model/following.model';
import { Router } from '@angular/router';

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.css'],
    standalone: false
  
  })

  export class UserProfileComponent implements OnInit {
    faProfileCircle = faUserCircle;
    faTimes=faTimes;
    userPosts: { content: string }[] = [
        { content: 'Prva objava korisnika' },
        { content: 'Druga objava korisnika' },
        { content: 'Treća objava korisnika' }
      ];                                                        // OVO JE SAMO PRIVREMENO
    username: string | null = '';
    user: RegisterUser | undefined;                 // ovo je na ciji profil si usao
    registerUser: RegisterUser | undefined;         // ovo je onaj ko je ulogovan
    followingUsers: Following[] = [];   // Lista korisnika koje trenutni korisnik prati
    followers: Following[] = [];   // Lista followera trenutnog korisnika
    isFollowersModalOpen: boolean = false;  
    isFollowingModalOpen: boolean = false;  
    isFollowing: boolean = false; 
    constructor(private route: ActivatedRoute, private userService: LoginService, private router: Router,private followingService: FollowingService) {}


    ngOnInit(): void {
        this.username = this.route.snapshot.paramMap.get('username');
        if (this.username) {
            this.userService.getUserByUsername(this.username).subscribe({
              next: (users: RegisterUser[]) => {
                if (users.length > 0) {
                  this.user = users[0]; // Pretpostavka da postoji samo jedan korisnik sa tim username
                  this.loadFollowData(this.user.username); // Učitaj following i followers
                } else {
                  alert('Korisnik nije pronađen!');
                }
              },
              error: (err) => {
                console.error('Greška pri preuzimanju korisnika:', err);
                alert('Došlo je do greške pri preuzimanju korisnika.');
              }
            });
          }   
          
          this.userService.user$.subscribe({
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

        onFollowClick(): void {
            if (this.isFollowing) {
                const followingToRemove = this.followers.find(
                    (follower) => follower.username === this.registerUser?.username
                  );
                  if (followingToRemove) {
                    this.followingService.deleteFollowing(followingToRemove.id).subscribe({
                      next: () => {
                        if (this.user) {
                            this.loadFollowData(this.user.username);
                          }  
                        this.isFollowing = false; 
                      },
                      error: (err) => {
                        console.error('Error while unfollowing the user:', err);
                        alert('Failed to unfollow the user.');
                      },
                    });
                  } else {
                    console.error('Following object not found.');
                    alert('Error: Following record not found.');
                  }
                } else { 
                const newFollowing: Following = {
                  id: 0, // API će kreirati ID
                  username: this.registerUser?.username ?? "", // Trenutni korisnik 
                  following:this.user?.username ?? "",   // Korisnik kojeg hocemo da  pratimo
                  creationTime: new Date()
                };
                this.followingService.createFollowing(newFollowing).subscribe({
                    next: (response) => {
                        if (this.user) {
                            this.loadFollowData(this.user.username);
                          }               
                     },
                    error: (err) => {
                      alert('Error occurred while following the user.');
                      console.error(err);
                    }
                  });
          }
        }


          loadFollowData(username: string): void {
            this.followingService.getAllFollowedByUsername(username).subscribe({
              next: (following) => {
                this.followingUsers = following;  // Učitavamo sve followinge
              },
              error: (error) => {
                console.error('Greška pri učitavanju following korisnika', error);
              }
            });
        
            this.followingService.getAllFollowersByUsername(username).subscribe({
              next: (followers) => {
                this.followers = followers;  // Učitavamo sve followerse
                this.checkIfFollowing(); // Proveri da li trenutni korisnik prati

              },
              error: (error) => {
                console.error('Greška pri učitavanju followers korisnika', error);
              }
            });
          }
          checkIfFollowing(): void {
            this.isFollowing = this.followers.some(follower => follower.username === this.registerUser?.username);
          }


        loadUserData(userId:number): void{
            this.userService.getUserForProfile(userId).subscribe({
              next: (user) => {
                this.registerUser = user;
              },
              error: (error) => {
                console.error('Greška prilikom učitavanja profila', error);
              }
            });
          }

          showUsername(username: string): void {
            this.router.navigate(['user-profile', username]).then(() => {
              window.location.reload(); 
            });
          }

          openFollowersModal(): void {
            this.isFollowersModalOpen = true;
          }
          closeFollowersModal(): void {
            this.isFollowersModalOpen = false;
          }
          openFollowingModal(): void {
            this.isFollowingModalOpen = true;
          }
          closeFollowingModal(): void {
            this.isFollowingModalOpen = false;
          }
  }