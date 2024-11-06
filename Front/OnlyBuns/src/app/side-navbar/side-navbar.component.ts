import { Component } from '@angular/core';
import { faHome, faChartLine, faMapMarkerAlt, faComments, faUser } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-side-navbar',
  templateUrl: './side-navbar.component.html',
  styleUrls: ['./side-navbar.component.css']
})
export class SideNavbarComponent {
  faHome = faHome;
  faTrending = faChartLine;
  faMap = faMapMarkerAlt;
  faChat = faComments;
  faProfile = faUser;
}
