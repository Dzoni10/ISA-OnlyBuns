import { Injectable } from '@angular/core';
import { LoginService } from './login-service.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private loginService: LoginService) {}

  isLoggedIn(): Observable<boolean> {
    return this.loginService.logged; 
  }
}
