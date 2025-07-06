import { Component, OnInit } from '@angular/core';
import { faHome, faChartLine, faMapMarkerAlt, faComments, faUser, faList, faChartBar, faUserFriends,faMap } from '@fortawesome/free-solid-svg-icons';
import { LoginService } from '../login-service.service';
import { RegisterUser } from '../model/register-user.model';

@Component({
  selector: 'app-side-navbar',
  templateUrl: './side-navbar.component.html',
  styleUrls: ['./side-navbar.component.css'],
  standalone: false
})
export class SideNavbarComponent implements OnInit {
  faHome = faHome;
  faTrending = faChartLine;
  faMap = faMapMarkerAlt;
  faChat = faComments;
  faProfile = faUser;
  faList = faList;
  faChartBar = faChartBar;
  faUserFriends = faUserFriends;
  faMapp = faMap;

  constructor(private logService: LoginService) { }

  logged = false;

  registerUser: RegisterUser={id: 999, name: "aaa", surname:"bbb", username:"ccc", password:"ddd", email:"eee",adress:{latitude:33.21,longitude:43.33}, loginStatus:false, isAdmin: false, postCount:0, commentNumber:0} ;

  ngOnInit(): void {
    this.logService.logged.subscribe(isLogged => {
      this.logged = isLogged;
    });


    this.logService.user$.subscribe({
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
  
  loadUserData(userId:number): void{
    this.logService.getUserForProfile(userId).subscribe({
      next: (user) => {
        this.registerUser = user;
      },
      error: (error) => {
        console.error('Greška prilikom učitavanja profila', error);
      }
    });
    
  }
}
