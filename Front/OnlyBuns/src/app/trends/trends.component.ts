import { Component, OnInit } from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../model/post.model';
import { Like } from '../model/like.model';
import { PagedResults } from '../model/paged-results.model';
import { LikeService } from '../like.service';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { ThemeService } from 'ng2-charts';


@Component({
  selector: 'app-trends',
  templateUrl: './trends.component.html',
  styleUrls: ['./trends.component.css']
})
export class TrendsComponent implements OnInit {

  posts:Post[]=[];
  likes:Like[]=[];
  registerUser: RegisterUser | undefined;
  popularPosts7Days: Post[]=[];
  mostPopularPosts: Post[]=[];

  constructor(private postService: PostService,private likeService:LikeService, private loginService:LoginService){}

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
    });
    this.loadPosts();
    this.loadMostPopularPostsEver();
    this.loadPopularPosts7Days();
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

      loadLikes(post: Post): void{
        this.likeService.getLikesByPostId(post.id).subscribe((pagedResults: PagedResults<Like>) => {
              post.likes = pagedResults.results;
              post.likeCount = pagedResults.results.length;
              post.userHasLiked = post.likes.some((like:Like)=> like.username === this.registerUser?.username);
            });
      }


      loadPopularPosts7Days(): void{
          this.postService.getPopularPostsIn7Days().subscribe({
            next:(result)=>{
              this.mostPopularPosts = result.results;
              this.mostPopularPosts.forEach(mpp=>{
                this.loadLikes(mpp);
              })
            }, error(err) {
              alert("Nije moguce dobaviti najpopularnije postove u poslednjih 7 dana");
            },
          });
      }

      loadMostPopularPostsEver(): void{
        this.postService.getMosPopularPostsEver().subscribe({
          next:(result)=>{
            this.mostPopularPosts = result.results;
            this.mostPopularPosts.forEach(mppe=>{
              this.loadLikes(mppe);
            })
          }, error(err) {
            alert("Nije moguce dobaviti najpopularnije postove ikada");
          },
        });

      }
}
