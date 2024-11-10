import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from './env/enviroment';
import { Router } from '@angular/router';
import { TokenStorage } from './jwt/token.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient, private tokenStorage: TokenStorage, private router: Router) { }

  createPost(formData: FormData): Observable<any> {
    return this.http.post<any>(environment.apiHost + "/posts/create", formData);
  }
}
