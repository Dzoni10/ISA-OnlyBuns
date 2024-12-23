import { Component, OnInit } from '@angular/core';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { Following } from '../model/following.model';
import { FollowingService } from '../following.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { PostService } from '../post.service';
import { Post } from '../model/post.model';
import { PagedResults } from '../model/paged-results.model';
import { LikeService } from '../like.service';
import { Like } from '../model/like.model';
import {faUserCircle , faTrash, faTimes} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  standalone:false
})
export class ProfileComponent implements OnInit {
  faProfileCircle = faUserCircle;  
  faTrash = faTrash;
  faTimes = faTimes;
  
constructor (private loginService: LoginService, private followingService: FollowingService, private postService: PostService, private likeService: LikeService){}

registerUser: RegisterUser | undefined;
followers : Following[]=[];
followingUsers: Following[]=[];
isFollowing: boolean = false; 
userRow: RegisterUser | undefined;
private userCache: Map<number, RegisterUser> = new Map();
loading: boolean = false;
posts: Post[]=[];
logged=false;

ngOnInit(): void {
  
  this.loginService.logged.subscribe(isLogged=>{
    this.logged = isLogged;
  })

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
  });

  this.loadPosts();
  
}

  isEditingPassword = false;
  newPassword=''
  confirmPassword=''


loadUserData(userId:number): void{
  this.loginService.getUserForProfile(userId).subscribe({
    next: (user) => {
      this.registerUser = user;
      if(this.registerUser?.username!== undefined)
        {
          this.getFollowers(this.registerUser?.username);
        }
    },
    error: (error) => {
      console.error('Greška prilikom učitavanja profila', error);
    }
  });
}

getUserByID(id: number): Observable<RegisterUser>{

  if (this.userCache.has(id)) {
    return of(this.userCache.get(id)!); // Vraća Observable sa keširanim korisnikom
  }

  // Ako nije, pozovite API i sačuvajte rezultat u keš
  this.loading=true;
  return this.loginService.getUserForProfile(id).pipe(
    tap(user => {
      this.userCache.set(id, user); // Sačuvajte korisnika u keš
      this.loading=false;
      console.log('Dohvaćen korisnik sa API-ja:', user);
    })
  );
}

  toggleEditPassword() {
    this.isEditingPassword = !this.isEditingPassword;
    // Resetuj polja prilikom zatvaranja
    if (!this.isEditingPassword) {
      this.newPassword = '';
      this.confirmPassword = '';
    }
  }

  confirmPasswordChange() {
    if (this.newPassword === this.confirmPassword) {
      // Ovde biste pozvali servis za čuvanje nove lozinke na backendu
      if(this.registerUser)
        {
          this.registerUser.password = this.newPassword;
          this.isEditingPassword = false; // Sakrij polja za unos
          this.loginService.changePassword(this.registerUser).subscribe({
            next: ()=>{
            },
            error: (err)=>{
              if(err.status===200)
                alert("Lozinka je uspesno izmenjena");
              else
                {    
                  alert("Nije izmenjena");
                  console.log(err.status)
                }
            }
          });
        }
        else
        alert("Korisnik je undefined");
    } else {
      alert('Lozinke se ne podudaraju. Pokušajte ponovo.');
    }
  }

  getFollowers(username: string){
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

  loadPosts(): void {
      this.postService.getAllPosts().subscribe(
        (data: PagedResults<Post>) => {
          this.posts = data.results.filter(p=>p.creatorId === this.registerUser?.id); // Uzimamo postove iz rezultata
          this.posts.forEach(post => {
            this.loadLikes(post);
          });
        },
        (error) => {
          console.error('Greška pri učitavanju postova', error); // Obrada greške
        }
      );
    }
  
    loadLikes(post: Post): void{
      this.likeService.getLikesByPostId(post.id).subscribe((pagedResults: PagedResults<Like>) => {
            post.likes = pagedResults.results;
            post.likeCount = pagedResults.results.length;
            post.userHasLiked = post.likes.some((like:Like)=> like.username === this.registerUser?.username);
          });
    }

    deletePost(postId: number): void {
      const confirmed = window.confirm('Are you sure you want to delete this post?');
      
      if (confirmed) {
        this.postService.deletePost(postId).subscribe(
          () => {
            this.loadPosts();
          },
          (error) => {
            console.error('Error deleting post', error);
          }
        );
      }
    }

}
