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
  styleUrls: ['./signup.component.css'],
  standalone:false
})
export class SignupComponent implements OnInit {

  constructor(private loginService:LoginService,private router: Router){}

  allUsers: RegisterUser[]=[]

  ngOnInit(): void{
    this.getUsers()
  }

  getUsers():void{
    this.loginService.getUsers().subscribe({
      next: (result: PagedResults<RegisterUser>)=>{
        this.allUsers = result.results;
      },
      error:(err:any)=>{
        console.log(err)
      }
    });
  }

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
        if(this.allUsers && this.allUsers.length>0){
          
        const usernameMailExist = this.allUsers.some((user)=>((user.email === registration.email) || (user.username === registration.username)));
        if(usernameMailExist)
        {
          alert("Korisnicko ime ili mejl vec postoje izaberite druge");
        }
        else
        {
        this.loginService.register(registration).subscribe({
        next: (usr)=>{
          alert("Mejl za aktivaciju je poslat, idite na mejl i potvrdite registraciju");
          this.registrationForm.reset();
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error("Došlo je do greške:", err);
          alert("Registracija nije uspela. Pokušajte ponovo." + err.value);
        },
      });
      }
    }else{
      alert("Nema dostupnih podataka o korisnicima");
    }
    }
    else{
      alert("Ponovno unesena lozinka se ne poklapa");
    }
  }
  }

}

