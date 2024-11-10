import { Component, OnInit } from '@angular/core';
import { FormControl,FormGroup,Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { Login } from '../model/login.model';
import { LoginService } from '../login-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone:false
})

export class LoginComponent implements OnInit {

  logged = false;

  constructor( private router: Router, private loginService: LoginService, private snackBar: MatSnackBar){}

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
        this.openSnackBar("Uspešno logovanje");
        this.router.navigate(['home']);
      },
      error:(err)=>
      {
        let message = '';
        switch(err.status){
          case 404:
            // alert("Korisnik sa tim podacima ne postoji");
            message = "Korisnik sa tim podacima ne postoji";
            break;
          case 401:
              // alert("Verifikujte nalog preko mejla da bi mogli da se ulogujete");
              message = "Verifikujte nalog preko mejla da bi mogli da se ulogujete";
              break;
          case 429:
              // alert("Prekoracili ste broj pokusaja za prijavu, pokusajte ponovo za 1 min");
              message = "Prekoracili ste broj pokusaja za prijavu, pokusajte ponovo za 1 min";
              break;
        } 
        this.openSnackBar(message);
      }
    });
  }
  }

  openSnackBar(message: string): void {
    this.snackBar.open(message, '', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }

}


