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
import { Comment } from '../app/model/comment.model';
import { Like } from './model/like.model';

@Injectable({
  providedIn: 'root'
})
export class LikeService {

  constructor(private http: HttpClient, private router: Router) { }

 
  getLikesByPostId(postId: number): Observable<PagedResults<Like>>{
    return this.http.get<PagedResults<Like>>(`${environment.apiHost}likes/post/${postId}`);
  }
  addLike(like: any): Observable<any> {
    return this.http.post<any>(environment.apiHost + "likes/create", like);
  }
  removeLike(postId: number, username: string) {
    //return this.http.delete<any>(environment.apiHost + "likes/delete", like);
    return this.http.delete(`${environment.apiHost}likes/delete/${postId}/${username}`);
  }
  // removeLike(id: any, username: string) {
  //   return this.http.delete<any>(environment.apiHost + "likes/delete", like);
  // }
}
