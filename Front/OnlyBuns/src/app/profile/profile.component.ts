import { Component, OnInit } from '@angular/core';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  
constructor (private loginService: LoginService){}

registerUser: RegisterUser | undefined;
   

ngOnInit(): void {
  
  this.loginService.user$.subscribe({
    next: (user) =>{
      if(user&& user.id){
        this.loadUserData(user.id);
      }
    },
    error: (error)=>{
      console.log("Greska prilikom dobijanja korisnika", error);
      alert("Greska pri ucitavanju profila");
    }
  })
}

  isEditingPassword = false;
  newPassword=''
  confirmPassword=''


loadUserData(userId:number): void{
  this.loginService.getUserForProfile(userId).subscribe({
    next: (user) => {
      this.registerUser = user;
    },
    error: (error) => {
      console.error('Greška prilikom učitavanja profila', error);
    }
  });
}


  toggleEditPassword() {
    this.isEditingPassword = !this.isEditingPassword;
    // Resetuj polja prilikom zatvaranja
    if (!this.isEditingPassword) {
      this.newPassword = '';
      this.confirmPassword = '';
    }
  }

  confirmPasswordChange() {
    if (this.newPassword === this.confirmPassword) {
      // Ovde biste pozvali servis za čuvanje nove lozinke na backendu
      if(this.registerUser)
        {
          this.registerUser.password = this.newPassword;
          this.isEditingPassword = false; // Sakrij polja za unos

          this.loginService.changePassword(this.registerUser).subscribe({
            next: ()=>{
            },
            error: (err)=>{
              if(err.status===200)
                alert("Lozinka je uspesno izmenjena");
              else
                {    
                  alert("Nije izmenjena");
                  console.log(err.status)
                }
            }
          });
        
        }
        else
        alert("Korisnik je undefined");
    } else {
      alert('Lozinke se ne podudaraju. Pokušajte ponovo.');
    }
  }

}
