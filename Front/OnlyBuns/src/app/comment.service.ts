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

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient, private router: Router) { }

 
  getCommentsByPostId(postId: number): Observable<PagedResults<Comment>>{
    return this.http.get<PagedResults<Comment>>(`${environment.apiHost}comments/post/${postId}`);
  }
  addComment(comment: any): Observable<any> {
    return this.http.post<any>(environment.apiHost + "comments/create", comment);
  }
}
