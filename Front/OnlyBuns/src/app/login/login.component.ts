import { Component } from '@angular/core';
import { FormControl,FormGroup,Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { Login } from '../model/login.model';
import { LoginService } from '../login-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {

  constructor( private router: Router, private loginService: LoginService){}

loginForm = new FormGroup({
  email: new FormControl('',[Validators.required]),
  password: new FormControl('',[Validators.required])
});

login(): void{
  const login: Login={
    email: this.loginForm.value.email || "",
    password: this.loginForm.value.password|| ""
  };

  if (this.loginForm.valid){
    this.loginService.login(login).subscribe({
      next: ()=>{
        this.router.navigate(['/']);
      },
    });
  }
  }
}


