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

  posts: Post[] = []; 
  users: RegisterUser[] = []; 
  sortDirection: { [key: string]: boolean } = { 'name': true, 'surname': true, 'username': true, 'email': true, 'loginStatus': true };
  filteredUsers: RegisterUser[] = [];
  isSearchModalOpen: boolean = false;
  emailOptions: string[] = []; 
  surnameOptions: string[] = []; 
  nameOptions: string[] = []; 

  currentPage: number = 1;
  usersPerPage: number = 5;
  
  searchCriteria = {
    name: '',
    surname: '',
    email: '',
    minPosts: null,
    maxPosts: null
  };
  constructor(private postService: PostService,private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.loadUsers(); 
    this.loadPosts(); 
  }

  loadPosts(): void {
    this.postService.getAllPosts().subscribe(
      (data: PagedResults<Post>) => {
        this.posts = data.results;
        this.updateUserPostCounts();

      },
      (error) => {
        console.error('Greška pri učitavanju postova', error); // Obrada greške
      }
    );
  }


  loadUsers(): void {
    this.loginService.getUsers().subscribe(
      (data: PagedResults<RegisterUser>) => {
        this.users = data.results.filter(user => user.loginStatus === true);
        this.filteredUsers = [...this.users]; // Prvobitno prikazujemo sve korisnike
        this.nameOptions = this.users.map(user => user.name);
        this.surnameOptions = this.users.map(user => user.surname);
        this.emailOptions = this.users.map(user => user.email);
        this.updateUserPostCounts();

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

  openSearchModal(): void {
    this.isSearchModalOpen = true;
  }

  closeSearchModal(): void {
    this.isSearchModalOpen = false;
  }

  searchUsers(): void {
    this.filteredUsers = this.users.filter(user => {
      const matchesName = this.searchCriteria.name.trim() 
        ? user.name.toLowerCase().includes(this.searchCriteria.name.toLowerCase())
        : true;
      const matchesSurname = this.searchCriteria.surname.trim()
        ? user.surname.toLowerCase().includes(this.searchCriteria.surname.toLowerCase())
        : true;
      const matchesEmail = this.searchCriteria.email.trim()
        ? user.email.toLowerCase().includes(this.searchCriteria.email.toLowerCase())
        : true;
        const matchesMinPosts = this.searchCriteria.minPosts !== null
        ? user.postNumber >= this.searchCriteria.minPosts
        : true;
        const matchesMaxPosts = this.searchCriteria.maxPosts !== null
        ? user.postNumber <= this.searchCriteria.maxPosts
        : true;

        return matchesName && matchesSurname && matchesEmail && matchesMinPosts && matchesMaxPosts;

    });

    this.closeSearchModal();
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
  this.searchUsers();
}

updateUserPostCounts(): void {
  this.users.forEach(user => {
    if (user.postNumber === undefined) {
      user.postNumber = 0; 
    }
  });

  this.posts.forEach(post => {
    const user = this.users.find(user => user.username === post.username);
    if (user) {
      user.postNumber += 1;
    }
  });
}

 // Metoda za vraćanje korisnika za trenutnu stranicu
 getPagedUsers() {
  const startIndex = (this.currentPage - 1) * this.usersPerPage;
  return this.filteredUsers.slice(startIndex, startIndex + this.usersPerPage);
}

// Prebacivanje na sledeću stranicu
goToNextPage() {
  if (this.currentPage * this.usersPerPage < this.users.length) {
    this.currentPage++;
  }
}

// Prebacivanje na prethodnu stranicu
goToPreviousPage() {
  if (this.currentPage > 1) {
    this.currentPage--;
  }
}

// Prebacivanje na prvu stranicu
goToFirstPage() {
  this.currentPage = 1;
}

// Prebacivanje na poslednju stranicu
goToLastPage() {
  this.currentPage = Math.ceil(this.users.length / this.usersPerPage);
}


}