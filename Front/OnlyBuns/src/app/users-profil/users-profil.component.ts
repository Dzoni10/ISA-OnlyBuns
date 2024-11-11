import { Component, OnInit } from '@angular/core';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { PagedResults } from '../model/paged-results.model';

@Component({
  selector: 'app-users-profil',
  templateUrl: './users-profil.component.html',
  styleUrls: ['./users-profil.component.css'],
  standalone: false
})
export class UsersProfilComponent implements OnInit {

  users: RegisterUser[] = []; 
  sortDirection: { [key: string]: boolean } = { 'name': true, 'surname': true, 'username': true, 'email': true, 'loginStatus': true };

  constructor(private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.loadUsers(); 
  }

  loadUsers(): void {
    this.loginService.getUsers().subscribe(
      (data: PagedResults<RegisterUser>) => {
        this.users = data.results; // Uzimamo postove iz rezultata
      },
      (error) => {
        console.error('Greška pri učitavanju korisnika', error); // Obrada greške
      }
    );
  }

  sortBy(field: keyof RegisterUser): void {
    this.sortDirection[field] = !this.sortDirection[field]; // Promena pravca sortiranja za to polje
    this.users.sort((a, b) => {
      const aValue = a[field];
      const bValue = b[field];
  
      // Proveriti da li su vrednosti definisane i uporediti ih
      if (aValue === undefined || bValue === undefined) {
        return 0; // Ako je neka vrednost undefined, nemojte menjati redosled
      }
  
      if (aValue < bValue) {
        return this.sortDirection[field] ? -1 : 1;
      } else if (aValue > bValue) {
        return this.sortDirection[field] ? 1 : -1;
      } else {
        return 0;
      }
    });
  }
  
  
}
