import { Component, OnInit } from '@angular/core';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { PagedResults } from '../model/paged-results.model';
import { Post } from '../model/post.model';
import { PostService } from '../post.service';

@Component({
  selector: 'app-users-profil',
  templateUrl: './users-profil.component.html',
  styleUrls: ['./users-profil.component.css'],
  standalone: false
})
export class UsersProfilComponent implements OnInit {

  users: RegisterUser[] = []; 
  sortDirection: { [key: string]: boolean } = { 'name': true, 'surname': true, 'username': true, 'email': true, 'loginStatus': true };
  filteredUsers: RegisterUser[] = [];
  isSearchModalOpen: boolean = false;
  emailOptions: string[] = []; 
  surnameOptions: string[] = []; 
  nameOptions: string[] = []; 
  totalUsers = 0;

  currentPage: number = 1;
  
  searchCriteria = {
    name: '',
    surname: '',
    email: '',
    minPosts: null,
    maxPosts: null
  };
  constructor(private postService: PostService,private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.loadUsersForPagination(); 
  }

  loadUsersForPagination(): void {
    this.loginService.getUsersPagination(this.currentPage).subscribe(
      (data: PagedResults<RegisterUser>) => {
        this.users = data.results;
        this.filteredUsers = [...this.users]; // Prvobitno prikazujemo sve korisnike
        this.nameOptions = this.users.map(user => user.name);
        this.surnameOptions = this.users.map(user => user.surname);
        this.emailOptions = this.users.map(user => user.email);
      },
      (error) => {
        console.error('Greška pri učitavanju korisnika', error); // Obrada greške
      }
    );
  }

  sortBy(field: keyof RegisterUser): void {
    this.sortDirection[field] = !this.sortDirection[field]; // Promena pravca sortiranja za to polje
    this.filteredUsers.sort((a, b) => {
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
  
  makeAdmin(user: RegisterUser): void {
    // Primer logike za ažuriranje korisnika na serveru
    user.isAdmin = true;
    /*
    this.loginService.updateUser(user).subscribe(
      () => {
        console.log(`${user.username} is now an admin.`);
      },
      (error) => {
        console.error('Error making user admin', error);
        // Vratite promenjivu na false ako je došlo do greške
        user.isAdmin = false;
      }
    );
    */
  }
  searchUsers(): void {
    const { name, surname, email, minPosts, maxPosts } = this.searchCriteria;

    this.loginService.searchUsers(
      name || undefined,
      surname || undefined,
      email || undefined,
      minPosts !== null ? minPosts : undefined,
      maxPosts !== null ? maxPosts : undefined
    ).subscribe({
      next: (result) => {
        this.users = result.results;
        this.totalUsers = result.totalCount;
        this.filteredUsers = [...this.users]; 
        this.closeSearchModal();
      },
      error: (err) => {
        console.error('Error fetching users:', err);
      },
    });
    
  }

  openSearchModal(): void {
    this.isSearchModalOpen = true;
  }

  closeSearchModal(): void {
    this.isSearchModalOpen = false;
  }

 
isSearchCriteriaFilled(): boolean {
  return (
    (this.searchCriteria.name && this.searchCriteria.name.trim() !== '') ||
    (this.searchCriteria.surname && this.searchCriteria.surname.trim() !== '') ||
    (this.searchCriteria.email && this.searchCriteria.email.trim() !== '') ||
    this.searchCriteria.minPosts !== null ||
    this.searchCriteria.maxPosts !== null
  );
}
resetSearchCriteria(): void {
  this.searchCriteria = {
    name: '',
    surname: '',
    email: '',
    minPosts: null,
    maxPosts: null
  };
  this.loadUsersForPagination();
}


// Prebacivanje na sledeću stranicu
goToNextPage() {
  this.currentPage++;
  this.loadUsersForPagination();
}

// Prebacivanje na prethodnu stranicu
goToPreviousPage() {
  if (this.currentPage > 1) {
    this.currentPage--;
    this.loadUsersForPagination();
  }
}

// Prebacivanje na prvu stranicu
goToFirstPage() {
  this.currentPage = 1;
  this.loadUsersForPagination();

}

// Prebacivanje na poslednju stranicu
goToLastPage() {
  //posto je paginacija premestena na back vise nemam info o ukupnom broju usera tkd ovo nista sad pametno ne radi 
  this.loadUsersForPagination();
}

}