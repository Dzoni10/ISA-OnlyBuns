import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Registration } from '../model/registration.model';
import { RegisterUser } from '../model/register-user.model';
import { PagedResults } from '../model/paged-results.model';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  constructor(private loginService:LoginService,private router: Router){}

  ngOnInit(): void{}


  registrationForm = new FormGroup({
    name:new FormControl('',[Validators.required]),
    surname:new FormControl('',[Validators.required]),
    username:new FormControl('',[Validators.required]),
    password:new FormControl('',[Validators.required]),
    password2:new FormControl('',[Validators.required]),
    email:new FormControl('',[Validators.required]),
    adress:new FormControl('',[Validators.required]),
  });

  register(): void{
    const registration: Registration={
      name: this.registrationForm.value.name || "",
      surname: this.registrationForm.value.surname|| "",
      username: this.registrationForm.value.username || "",
      password: this.registrationForm.value.password || "",
      email: this.registrationForm.value.email || "",
      adress: this.registrationForm.value.adress || "",
    };

    if(this.registrationForm.valid){
      if(this.registrationForm.value.password === this.registrationForm.value.password2){
        
        this.loginService.register(registration).subscribe({
        next: (usr)=>{
          alert("Mejl za aktivaciju je poslat, idite na mejl i potvrdite registraciju");
          this.registrationForm.reset();
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.log("Greska",err)
          switch(err.status){
            case 401:
              console.error("Došlo je do greške mejl vec postoji", err);
              alert("Mejl koji ste uneli vec postoji");
              break;
            case 409:
              console.error("Došlo je do greške korisnicko ime vec postoji");
              alert("Korisnicko ime koje ste uneli vec postoji");
              break;
            case 400:
              console.error("Došlo je do greške korisnicko ime i mejl vec postoje");
              alert("Korisnicko ime i mejl koji ste uneli vec postoje");
              break;
          }
        },
      });
    }
    else{
      alert("Ponovno unesena lozinka se ne poklapa");
    }
  }
  }

}

