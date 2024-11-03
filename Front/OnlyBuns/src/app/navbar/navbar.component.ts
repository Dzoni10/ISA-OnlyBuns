import { Component,OnInit,ViewEncapsulation } from '@angular/core';
import { User } from '../model/user.model';
import { LoginService } from '../login-service.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class NavbarComponent implements OnInit {

  user: User | undefined;
  constructor(private logService: LoginService){}

  ngOnInit(): void {
   
    this.logService.user$.subscribe(user=>{
      this.user = user;
    });
  }
  onLogout(): void{
    this.logService.logout();
  }
}
