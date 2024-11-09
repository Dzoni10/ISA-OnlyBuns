import { Component, OnInit } from '@angular/core';
import { faHome, faChartLine, faMapMarkerAlt, faComments, faUser } from '@fortawesome/free-solid-svg-icons';
import { LoginService } from '../login-service.service';

@Component({
  selector: 'app-side-navbar',
  templateUrl: './side-navbar.component.html',
  styleUrls: ['./side-navbar.component.css'],
  standalone:false
})
export class SideNavbarComponent implements OnInit {
  faHome = faHome;
  faTrending = faChartLine;
  faMap = faMapMarkerAlt;
  faChat = faComments;
  faProfile = faUser;

  constructor(private logService: LoginService){}

  logged = false;

  ngOnInit(): void {
    this.logService.logged.subscribe(isLogged=>{
      this.logged = isLogged;
    })
  }


}
