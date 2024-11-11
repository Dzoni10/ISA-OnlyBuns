import { Component, OnInit } from '@angular/core';
import { faHome, faChartLine, faMapMarkerAlt, faComments, faUser, faList, faChartBar, faUserFriends } from '@fortawesome/free-solid-svg-icons';
import { LoginService } from '../login-service.service';

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

  constructor(private logService: LoginService) { }

  logged = false;
  isAdmin = false;  // This flag determines if the user is an admin

  ngOnInit(): void {
    this.logService.logged.subscribe(isLogged => {
      this.logged = isLogged;
    });

    // Example check for admin status, update this based on your own logic
    this.isAdmin = true;  // Set to true if the user is an admin
  }
}
