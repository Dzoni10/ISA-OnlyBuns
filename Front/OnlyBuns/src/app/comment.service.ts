import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from './env/enviroment';
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

  getAllComments(): Observable<PagedResults<Comment>>{
    return this.http.get<PagedResults<Comment>>(environment.apiHost +'/comments/all');
  }

}
