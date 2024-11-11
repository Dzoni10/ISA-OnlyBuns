
import { Component, OnInit } from '@angular/core';
import { PostService } from '../post.service';
import { CommentService } from '../comment.service';
import { Post } from '../model/post.model';
import { Comment } from '../model/comment.model';
import { PagedResults } from '../model/paged-results.model'; 
import { faComments, faUser, faThumbsUp, faCommentDots , faEdit, faTrash, faCheck, faTimes, faIcons, faPaperPlane} from '@fortawesome/free-solid-svg-icons';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { formatDistanceToNow } from 'date-fns';
import { LikeService } from '../like.service';
import { Like } from '../model/like.model';


@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
  standalone: false

})

export class HomePageComponent implements OnInit {
  
  beforeEditDescription = "";
  posts: Post[] = []; 
  registerUser: RegisterUser | undefined;
  newComment: Comment = {
    id: 0,
    postId: -1,
    text: "",
    creationTime: new Date(),
    username: ""
    };
  isModalOpen = false;
  selectedPost: Post | undefined;
  comments: Comment[] = []; 
 
  newCommentText = '';

  faPaperPlane = faPaperPlane;
  faChat = faComments;
  faProfile = faUser;
  faLike = faThumbsUp;
  faComment = faCommentDots;
  faEdit = faEdit;  
  faTrash = faTrash; 
  faCheck = faCheck;
  faTimes = faTimes;


  constructor(private postService: PostService,private commentService: CommentService, private loginService: LoginService, private router: Router, private likeService: LikeService ) { }
  logged =false;




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

 getTimeAgo(date: Date): string {
  return formatDistanceToNow(date) + ' ago';  
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
        this.posts.forEach(post => {
          this.loadLikes(post);
        });
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
      //alert(`Lajkovao/la post ID: ${postId} kao korisnik. ID korisnika: ${this.registerUser?.id}, USERNAME: ${this.registerUser?.username}`);
      
      const newLike : Like = {
        id: 0,
        postId: postId,
        username: this.registerUser.username
      }
      this.likeService.addLike(newLike)

      this.likeService.addLike(newLike).subscribe(
        response => {
          if(this.registerUser)
          alert(`Successfully liked post ID: ${postId} as user ${this.registerUser.username}`);
          // Optionally, handle the response, such as updating the like count or UI
        },
        error => {
          alert("An error occurred while liking the post. Please try again.");
        }
      );
    }
  }
  
  
  
  editPost(post: any): void {
    post.isEditing = true;
    this.beforeEditDescription= post.description;
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
    post.description=this.beforeEditDescription;
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
 
  openModal(post: Post): void {

    if (!this.registerUser?.id) {
      alert("Ako želite da lajkujete objavu ili da je komentarišete morate da se ulogujete!");
    } else {
      this.selectedPost = post;
      this.isModalOpen = true;
      this.loadComments(post.id);
    }
  }
  
  closeModal(): void {
    this.isModalOpen = false;
    this.selectedPost = undefined;
    this.newCommentText="";
  }

  
  
  loadComments(postId: number): void {
    this.commentService.getCommentsByPostId(postId).subscribe(
      (data: PagedResults<Comment>) => {
        this.comments =  data.results;;
      },
      (error) => {
        console.error("Greška pri učitavanju komentara", error);
      }
    );
  }
  
  addComment(postId?: number): void {   
    if(this.newCommentText.trim()){

      this.newComment.text= this.newCommentText || "Unknown";
      this.newComment.postId= postId || -999;
      this.newComment.username = this.registerUser?.username || "Unknown";
        this.commentService.addComment(this.newComment).subscribe(
          () => {
            this.loadComments(this.newComment?.postId); // Osvežava komentare
            this.newCommentText = ''; // Čisti polje za unos
          },
          (error) => {
            console.error("Greška pri dodavanju komentara", error);
          }
        );
        }
        else{
          alert("Morate uneti text!");
        }
  }
  
  loadLikes(post: any): void {
    this.likeService.getLikesByPostId(post.id).subscribe((pagedResults: PagedResults<Like>) => {
      post.likes = pagedResults.results;
      post.likeCount = pagedResults.results.length;
      post.userHasLiked = post.likes.some((like:Like)=> like.username === this.registerUser?.username);
    });
  }
  
  toggleLike(post: Post): void {
    if (!this.registerUser) {
      alert("Please log in to like posts.");
      return;
    }
  
    if (post.userHasLiked) {
      // Unlike the post
      this.likeService.removeLike(post.id, this.registerUser.username).subscribe(
        () => {
          post.likeCount--;
          post.userHasLiked = false;
        },
        error => {
          console.error("Error while unliking the post", error);
        }
      );
    } else {
      // Like the post
      const newLike: Like = {
        id: 0,
        postId: post.id,
        username: this.registerUser.username
      };
  
      this.likeService.addLike(newLike).subscribe(
        () => {
          post.likeCount++;
          post.userHasLiked = true;
        },
        error => {
          if (error.status === 409) {
            alert("You have already liked this post.");
            post.userHasLiked = true;
          }
          console.error("Error while liking the post", error);
        }
      );
    }
  }

}
