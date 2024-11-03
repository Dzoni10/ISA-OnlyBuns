import { Component,OnInit } from '@angular/core';
import { LoginService } from './login-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'OnlyBuns';

  constructor(
    private loginService: LoginService,
  ) {}

  ngOnInit(): void{
    this.checkIfUserExists();
  }

  private checkIfUserExists(): void{
    this.loginService.checkIfUserExists();
  }
}


