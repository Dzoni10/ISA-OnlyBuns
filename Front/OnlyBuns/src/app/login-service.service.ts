import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { TokenStorage } from './jwt/token.service';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from './model/user.model';
import { Router } from '@angular/router';
import { AuthenticationResponse } from './model/authetnification-response.model';
import { Login } from './model/login.model';
import { environment } from './env/enviroment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Registration } from './model/registration.model';
import { RegisterUser } from './model/register-user.model';
import { PagedResults } from './model/paged-results.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  user$=new BehaviorSubject<User>({username:"",id:0});
  constructor(private http: HttpClient, private tokenStorage: TokenStorage, private router: Router) { }


  login(login: Login): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(environment.apiHost + 'users/login', login)
      .pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
          this.setUser();
        })
      );
  }

  logout(): void {
    this.router.navigate(['/']).then(_ => {
      this.tokenStorage.clear();
      this.user$.next({username: "", id: 0 });
      }
    );
  }

  register(registration: Registration): Observable<AuthenticationResponse> {
    return this.http
    .post<AuthenticationResponse>(environment.apiHost + 'users/signup', registration)
    .pipe(
      tap((authenticationResponse) => {
        console.log("odgovor sa servera", authenticationResponse)
        this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
       // this.setUser();
      })
  );
      
  }
  getUsers(): Observable<PagedResults<RegisterUser>>{
    return this.http.get<PagedResults<RegisterUser>>(environment.apiHost +'/users/all');
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
    const user: User = {
      id: +jwtHelperService.decodeToken(accessToken).id,
      username: jwtHelperService.decodeToken(accessToken).username
    };
    this.user$.next(user);
  }
}
