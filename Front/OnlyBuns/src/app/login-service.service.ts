import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { TokenStorage } from './jwt/token.service';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from './model/user.model';
import { Router } from '@angular/router';
import { AuthenticationResponse } from './model/authetnification-response.model';
import { Login } from './model/login.model';
import { environment } from './env/enviroment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Registration } from './model/registration.model';
import { RegisterUser, RegisterUserWithFollowing } from './model/register-user.model';
import { PagedResults } from './model/paged-results.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  user$=new BehaviorSubject<User>({email:"",id:0});
  logged = new BehaviorSubject<boolean>(this.isLoggedIn());
  zone: any;

  constructor(private http: HttpClient, private tokenStorage: TokenStorage, private router: Router) { }


  login(login: Login): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(environment.apiHost + 'users/login', login)
      .pipe(
        tap((authenticationResponse) => {
          console.log("odgovor sa servera za login", authenticationResponse)
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
          this.setUser();
          this.logged.next(true);
          localStorage.setItem('isLoggedIn','true');
        })
      );
  }

  private isLoggedIn(): boolean {
    return localStorage.getItem('isLoggedIn') === 'true'; 
  }

  /*
  logout(): void {
    this.router.navigate(['login']).then(_ => {
      this.tokenStorage.clear();
      this.user$.next({email: "", id: 0 });
      this.logged.next(false);
      localStorage.removeItem('isLoggedIn');
      }
    );
  }*//*
    logout(): void {
      this.http.post(environment.apiHost + 'users/logout', {}, { headers: this.getAuthHeaders() })
        .subscribe({
          next: () => {
            console.log("Logout successful on backend");
          },
          error: (error) => {
            console.error("Logout failed on backend", error);
          },
          complete: () => {
            console.log("Clearing session and redirecting...");
            this.tokenStorage.clear();
            this.user$.next({ email: "", id: 0 });
            this.logged.next(false);
            localStorage.removeItem('isLoggedIn');
            this.router.navigate(['login']).then(() => {
              window.location.reload();
            // 🚀 FORCE UI UPDATE
            });
          }}
        )
        
    }
        */

    logout(): void {
      this.http.post(environment.apiHost + 'users/logout', {}, { 
        headers: this.getAuthHeaders(), 
        responseType: 'text'  // 👈 Tell Angular to expect plain text
      }).subscribe({
        next: () => {
          console.log("Logout successful on backend");
        },
        error: (error) => {
          console.error("Logout failed on backend", error);
        },
        complete: () => {
          console.log("Clearing session and redirecting...");
          this.tokenStorage.clear();
          this.user$.next({ email: "", id: 0 });
          this.logged.next(false);
          localStorage.removeItem('isLoggedIn');
          this.router.navigate(['login']).then(/*() => window.location.reload()*/);
        }
      });
    }
    
    private getAuthHeaders(): HttpHeaders {
      const token = this.tokenStorage.getAccessToken();
      return new HttpHeaders({
        Authorization: `Bearer ${token}`
      });
    }

  register(registration: Registration): Observable<User> {
    return this.http
    .post<User>(environment.apiHost + 'users/signup', registration)
    .pipe(
      tap((user) => {
        console.log("Korisnik je registrovan")
      })
  );
      
  }

  changePassword(registerUser:RegisterUser): Observable<String>{
    return this.http.post<String>(environment.apiHost + 'users/changePassword',registerUser)
    .pipe(
      tap((cc)=>{
        console.log("Lozinka je promenjena");
      })
    );
  }
  getUsers(): Observable<PagedResults<RegisterUser>>{
    return this.http.get<PagedResults<RegisterUser>>(environment.apiHost +'/users/all');
  }
  getUsersPagination(page: number): Observable<PagedResults<RegisterUserWithFollowing>>{
    return this.http.get<PagedResults<RegisterUserWithFollowing>>(environment.apiHost +`/users/users?pageNumber=${page}`);
  }
  getUserForProfile(id: number): Observable<RegisterUser>{
    return this.http.get<RegisterUser>(environment.apiHost + '/users/' + id);
  }
    getUserByUsername(username: string): Observable<RegisterUser[]> {
      return this.http.get<RegisterUser[]>(`${environment.apiHost}/users/findUsername?username=${username}`);
    }

    getByUsername(username: string): Observable<RegisterUser> {
      return this.http.get<RegisterUser>(`${environment.apiHost}/users/findOneByUsername?username=${username}`);
    }
    searchUsers(
      name?: string,
      surname?: string,
      email?: string,
      minPosts?: number,
      maxPosts?: number
    ): Observable<PagedResults<RegisterUser>> {
      // Početni URL za pretragu
      let query = `${environment.apiHost}/users/search?`;
    
      // Dodavanje query parametara
      if (name) query += `name=${encodeURIComponent(name)}&`;
      if (surname) query += `surname=${encodeURIComponent(surname)}&`;
      if (email) query += `email=${encodeURIComponent(email)}&`;
      if (minPosts !== undefined) query += `minPosts=${minPosts}&`;
      if (maxPosts !== undefined) query += `maxPosts=${maxPosts}&`;
    
      // Uklanjanje viška "&" sa kraja
      query = query.slice(0, -1);
    
      return this.http.get<PagedResults<RegisterUser>>(query);
    }
    
  checkIfUserExists(): void {
    const accessToken = this.tokenStorage.getAccessToken();
    if (accessToken == null) {
      return;
    }
    this.setUser();
  }

  private setUser(): void {
    const jwtHelperService = new JwtHelperService();
    const accessToken = this.tokenStorage.getAccessToken() || "";
    const decodeToken = jwtHelperService.decodeToken(accessToken);
    const user: User = {
      id: +decodeToken.id,
      email: decodeToken.email
    };
    this.user$.next(user);
  }

  getUserByEmail(email: string): Observable<RegisterUser> {
  return this.http.get<RegisterUser>(`${environment.apiHost}/users/findByEmail/${email}`);
}

}
