import { User } from './model/user.model';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Following } from './model/following.model';
import { environment } from './env/enviroment';
import { PagedResults } from './model/paged-results.model';
@Injectable({
    providedIn: 'root'
  })
  export class FollowingService {
    private apiUrl = `${environment.apiHost}following`;

    constructor(private http: HttpClient, private router: Router) { }

getAllFollowing(): Observable<PagedResults<Following>> {
  return this.http.get<PagedResults<Following>>(environment.apiHost + '/following/all');
}

// Dohvati sve koje korisnik prati (followed users)
getAllFollowedByUsername(username: string): Observable<Following[]> {
  return this.http.get<Following[]>(`${this.apiUrl}/followed-by/${username}`);
}

  // Dohvati sve pratioce korisnika
  getAllFollowersByUsername(username: string): Observable<Following[]> {
    return this.http.get<Following[]>(`${this.apiUrl}/followers/${username}`);
  }

  // Kreiraj novi "follow" zapis
  createFollowing(following: Following): Observable<Following> {
    return this.http.post<Following>(`${this.apiUrl}/create`, following);
}
//return this.http.post<any>(environment.apiHost + "likes/create", like);

  // Proveri da li korisnik prati drugog korisnika
  doesUserFollow(username: string, following: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/exists/${username}/${following}`);
  }

  // Obrisi "follow" zapis
  deleteFollowing(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
  }