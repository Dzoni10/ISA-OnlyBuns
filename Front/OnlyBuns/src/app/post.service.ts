import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { User } from './model/user.model';
import { BehaviorSubject, Observable, tap,of } from 'rxjs';
import { Router } from '@angular/router';
import { AuthenticationResponse } from './model/authetnification-response.model';
import { Post } from './model/post.model';
import { environment } from './env/enviroment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Registration } from './model/registration.model';
import { RegisterUser } from './model/register-user.model';   // ovde imas i ime i prezime i username i ID I SVE
import { PagedResults } from './model/paged-results.model';
import { parse } from 'date-fns';
@Injectable({
  providedIn: 'root'
})
export class PostService {


  constructor(private http: HttpClient, private router: Router) { }

  createPost(formData: FormData): Observable<any> {
    return this.http.post<any>(environment.apiHost + "/posts/create", formData).pipe(
    tap(() => this.clearCache())
  );
  }

  deletePost(postId: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiHost}posts/${postId}`).pipe(
    tap(() => this.clearCache()) 
  );
    ;
  }
  
  updatePost(postId: number, updatedPost: Post): Observable<Post> {
    return this.http.put<Post>(`${environment.apiHost}posts/${postId}`, updatedPost).pipe(
    tap(() => this.clearCache())
  );
  }
  getCountPostLastMonth(): Observable<number>{
    return this.http.get<number>(environment.apiHost + 'posts/popular/count-last-month');
  }

  getAllPosts(forceRefresh: boolean = false): Observable<PagedResults<Post>> {
    const cached = sessionStorage.getItem('allPosts');
    
    if (cached && !forceRefresh) {
      const parsed = JSON.parse(cached);
      return of(parsed);
    }

    return new Observable(observer => {
      this.http.get<PagedResults<Post>>(environment.apiHost + '/posts/all').subscribe({
        next: (res) => {
          sessionStorage.setItem('allPosts', JSON.stringify(res));
          observer.next(res);
          observer.complete();
        },
        error: err => observer.error(err)
      });
    });
  }

  getPopularPostsIn7Days(forceRefresh: boolean = false): Observable<PagedResults<Post>> {
    
    const cached = sessionStorage.getItem('popular7Days');
    
    if (cached && !forceRefresh) {
      return of(JSON.parse(cached));
    }

    return new Observable(observer => {
      this.http.get<PagedResults<Post>>(environment.apiHost + '/posts/popular/last-7-days').subscribe({
        next: (res) => {
          sessionStorage.setItem('popular7Days', JSON.stringify(res));
          observer.next(res);
          observer.complete();
        },
        error: err => observer.error(err)
      });
    });
  }

  getMosPopularPostsEver(forceRefresh: boolean = false): Observable<PagedResults<Post>> {
    
    const cached = sessionStorage.getItem('mostPopularEver');
    
    if (cached && !forceRefresh) {
      return of(JSON.parse(cached));
    }

    return new Observable(observer => {
      this.http.get<PagedResults<Post>>(environment.apiHost + 'posts/popular/all-time').subscribe({
        next: (res) => {
          sessionStorage.setItem('mostPopularEver',JSON.stringify(res));
          observer.next(res);
          observer.complete();
        },
        error: err => observer.error(err)
      });
    });
  }
  
  clearCache(): void {
    sessionStorage.removeItem('allPosts');
    sessionStorage.removeItem('popular7Days');
    sessionStorage.removeItem('mostPopularEver');
  }



}
