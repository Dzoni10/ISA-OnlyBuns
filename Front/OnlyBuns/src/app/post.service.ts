import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { User } from './model/user.model';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { AuthenticationResponse } from './model/authetnification-response.model';
import { Post } from './model/post.model';
import { environment } from './env/enviroment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Registration } from './model/registration.model';
import { RegisterUser } from './model/register-user.model';   // ovde imas i ime i prezime i username i ID I SVE
import { PagedResults } from './model/paged-results.model';
@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient, private router: Router) { }

  getAllPosts(): Observable<PagedResults<Post>>{
    return this.http.get<PagedResults<Post>>(environment.apiHost +'/posts/all');
  }

  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiHost}posts/${postId}`);
  }
  
  updatePost(postId: number, updatedPost: Post): Observable<Post> {
    return this.http.put<Post>(`${environment.apiHost}posts/${postId}`, updatedPost);
  }

  getPopularPostsIn7Days(): Observable<PagedResults<Post>>{
    return this.http.get<PagedResults<Post>>(environment.apiHost +'/posts/popular/last-7-days');
  }
  
  getMosPopularPostsEver(): Observable<PagedResults<Post>>{
    return this.http.get<PagedResults<Post>>(environment.apiHost + 'posts/popular/all-time');
  }

  getCountPostLastMonth(): Observable<number>{
    return this.http.get<number>(environment.apiHost + 'posts/popular/count-last-month');
  }

}
