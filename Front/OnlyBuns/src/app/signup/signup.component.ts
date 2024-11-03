import { Component } from '@angular/core';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Registration } from '../model/registration.model';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  constructor(private loginService:LoginService,private router: Router){}

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
      if(this.registrationForm.value.password === this.registrationForm.value.password2)
      {
        this.loginService.register(registration).subscribe({
        next: ()=>{
          this.registrationForm.reset();
        },
      });
      }
      else
      alert("Ponovno unesena lozinka se ne poklapa")
    }
  }
}
