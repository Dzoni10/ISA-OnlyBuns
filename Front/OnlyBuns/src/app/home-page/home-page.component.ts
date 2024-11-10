
import { Component, OnInit } from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../model/post.model';
import { PagedResults } from '../model/paged-results.model'; 
import { faComments, faUser, faThumbsUp, faCommentDots , faEdit, faTrash, faCheck, faTimes, faIcons} from '@fortawesome/free-solid-svg-icons';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
  standalone: false

})

export class HomePageComponent implements OnInit {
  
  posts: Post[] = []; 
  registerUser: RegisterUser | undefined;

  faChat = faComments;
  faProfile = faUser;
  faLike = faThumbsUp;
  faComment = faCommentDots;
  faEdit = faEdit;  
  faTrash = faTrash; 
  faCheck = faCheck;
  faTimes = faTimes;

  logged =false;

  constructor(private postService: PostService, private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.loadPosts(); 
    
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
    })
  }

  loadUserData(userId:number): void{
    this.loginService.getUserForProfile(userId).subscribe({
      next: (user) => {
        this.registerUser = user;
      },
      error: (error) => {
        console.error('Greška prilikom učitavanja profila', error);
      }
    });
  }


  loadPosts(): void {
    this.postService.getAllPosts().subscribe(
      (data: PagedResults<Post>) => {
        this.posts = data.results; // Uzimamo postove iz rezultata
      },
      (error) => {
        console.error('Greška pri učitavanju postova', error); // Obrada greške
      }
    );
  }

  likePost(postId: number): void {
    if (!this.registerUser?.id) {
      alert("Ako želite da lajkujete objavu ili da je komentarišete morate da se ulogujete!");
    } else {
      alert(`Lajkovao/la post ID: ${postId} kao korisnik. ID korisnika: ${this.registerUser?.id}, USERNAME: ${this.registerUser?.username}`);
    }
  }
  
  commentPost(postId: number): void {
    if (!this.registerUser?.id) {
      alert("Ako želite da lajkujete objavu ili da je komentarišete morate da se ulogujete!");
    } else {
      alert(`Komentarisao/la post ID: ${postId} kao korisnik. ID korisnika: ${this.registerUser?.id}, USERNAME: ${this.registerUser?.username}`);
    }
  }


  editPost(post: any): void {
    post.isEditing = true;
  }

  confirmEdit(post: any): void {
    post.isEditing = false;

    this.postService.updatePost(post.id, post).subscribe(
      (updatedPost) => {
        this.loadPosts();
      },
      (error) => {
        console.error('Greška prilikom ažuriranja posta', error);
        alert('Došlo je do greške prilikom ažuriranja posta.');
      }
    );  }

  cancelEdit(post: any): void {
    post.isEditing = false;
  }

  deletePost(postId: number): void {
    alert(postId);
    this.postService.deletePost(postId).subscribe(
      () => {
        this.loadPosts();
            },
      (error) => {
        console.error('Greška pri brisanju posta', error);
      }
    );
  }


  createPost(){
    this.router.navigate(['createPost']);
  }
}
