import { Component, OnInit } from '@angular/core';
import { FormControl,FormGroup,Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { Login } from '../model/login.model';
import { LoginService } from '../login-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone:false
})

export class LoginComponent implements OnInit {

  logged = false;

  constructor( private router: Router, private loginService: LoginService){}

loginForm = new FormGroup({
  email: new FormControl('',[Validators.required]),
  password: new FormControl('',[Validators.required])
});


ngOnInit(): void {
  this.loginService.logged.subscribe(isLogged=>{
    this.logged=isLogged;
  });
}

login(): void{
  const login: Login={
    email: this.loginForm.value.email || "",
    password: this.loginForm.value.password|| ""
  };

  if (this.loginForm.valid){
    this.loginService.login(login).subscribe({
      next: ()=>{
        alert("Uspesno logovanje");
        this.router.navigate(['/']);
      },
      error:(err)=>
      {
        switch(err.status){
          case 404:
            alert("Korisnik sa tim podacima ne postoji");
            break;
          case 401:
              alert("Verifikujte nalog preko mejla da bi mogli da se ulogujete");
              break;
        } 
      }
    });
  }
  }
}


