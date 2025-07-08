import { Component, OnInit, ChangeDetectorRef, Renderer2, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { faComments, faUser, faThumbsUp,faUserCircle, faCommentDots , faEdit, faTrash, faCheck, faTimes, faIcons, faPaperPlane} from '@fortawesome/free-solid-svg-icons';
import { FollowingService } from '../following.service';
import { Following } from '../model/following.model';
import { Router } from '@angular/router';
import { Post } from '../model/post.model';
import { PostService } from '../post.service';
import { PagedResults } from '../model/paged-results.model';
import { Like } from '../model/like.model';
import { LikeService } from '../like.service';
import { Comment } from '../model/comment.model';
import { CommentService } from '../comment.service';
import { formatDistanceToNow } from 'date-fns';

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.css'],
    standalone: false
  
  })

  export class UserProfileComponent implements OnInit {
        faProfileCircle = faUserCircle;
    faPaperPlane = faPaperPlane;
    faChat = faComments;
    faProfile = faUser;
    faLike = faThumbsUp;
    faComment = faCommentDots;
    faEdit = faEdit;  
    faTrash = faTrash; 
    faCheck = faCheck;
    faTimes = faTimes;
  
    userPosts: Post[] = []; // Zamenio userPosts sa posts
    comments: Comment[] = []; 
  newComment: Comment = {
    id: 0,
    postId: -1,
    text: "",
    creationTime: new Date(),
    username: ""
    };
  isModalOpen = false;
  selectedPost: Post | undefined;
   beforeEditDescription = "";

  newCommentText = '';

    username: string | null = '';
    user: RegisterUser | undefined;                 // ovo je na ciji profil si usao
    registerUser: RegisterUser | undefined;         // ovo je onaj ko je ulogovan
    followingUsers: Following[] = [];   // Lista korisnika koje trenutni korisnik prati
    followers: Following[] = [];   // Lista followera trenutnog korisnika
    isFollowersModalOpen: boolean = false;  
    isFollowingModalOpen: boolean = false;  
    isFollowing: boolean = false; 
    constructor(private route: ActivatedRoute,  private commentService: CommentService,  private likeService: LikeService, private postService: PostService, private userService: LoginService, private router: Router,private followingService: FollowingService) {}


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
          if(this.username) this.loadPostsByUsername(this.username);
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
                      alert('Too many follow requests, please try again later.');
                    }
                  });
          }
        }

 loadPostsByUsername(username: string): void {
        this.postService.getPostsByUsername(username).subscribe({
            next: (data: PagedResults<Post>) => {
                this.userPosts = data.results;
                this.userPosts.forEach(post => {
                    this.loadLikes(post);
                });
            },
            error: (error) => {
                console.error('Greška pri učitavanju postova korisnika', error);
            }
        });
    }
      loadLikes(post: any): void {
        this.likeService.getLikesByPostId(post.id).subscribe((pagedResults: PagedResults<Like>) => {
          post.likes = pagedResults.results;
          post.likeCount = pagedResults.results.length;
          post.userHasLiked = post.likes.some((like:Like)=> like.username === this.registerUser?.username);
        });
      }

  getTimeAgo(date: Date): string {
    return formatDistanceToNow(date) + ' ago';  
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
 
deletePost(postId: number): void {
    const confirmed = window.confirm('Are you sure you want to delete this post?');
    
    if (confirmed) {
      this.postService.deletePost(postId).subscribe(
        () => {
          if(this.username) this.loadPostsByUsername(this.username);
        },
        (error) => {
          console.error('Error deleting post', error);
        }
      );
    }
  }
 confirmEdit(post: any): void {
    post.isEditing = false;

    this.postService.updatePost(post.id, post).subscribe(
      (updatedPost) => {
          if(this.username) this.loadPostsByUsername(this.username);
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

    editPost(post: any): void {
    post.isEditing = true;
    this.beforeEditDescription= post.description;
  }

  }