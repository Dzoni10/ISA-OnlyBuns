import { Component, OnInit } from '@angular/core';
import { PostService } from '../post.service';
import { CommentService } from '../comment.service';
import { Comment } from '../model/comment.model';
import { Post } from '../model/post.model';
import { RegisterUser } from '../model/register-user.model';
import { LoginService } from '../login-service.service';
import { Router } from '@angular/router';
import { ChartOptions, ChartData, ChartType } from 'chart.js'; // Import odgovarajućih tipova
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-app-analytics',
  templateUrl: './app-analytics.component.html',
  styleUrls: ['./app-analytics.component.css'],
  standalone: false
})
export class AppAnalyticsComponent implements OnInit {

  isLoading = true;  // Dodajemo promenljivu za status učitavanja
  posts: Post[] = [];
  commetns: Comment[] = [];
  users: RegisterUser[] = [];
  usersWithPosts = 0;
  usersWithCommentsOnly = 0;
  inactiveUsers = 0;
  postCommentCounts = {
    weeklyPosts: 0,
    monthlyPosts: 0,
    yearlyPosts: 0,
    weeklyComments: 0,
    monthlyComments: 0,
    yearlyComments: 0
  };
  public radialChartLabels: string[] = ['Posted', 'Commented Only', 'Did Nothing'];
  public radialChartData: number[] = [];
  public radialChartType: ChartType = 'pie';  // Postavi na 'pie'

  public radialChartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      tooltip: {
        bodyFont: {
          size: 18, // Povećanje fonta u tooltipu
        },
        titleFont: {
          size: 20, // Povećanje fonta naslova u tooltipu
        }
      },
      legend: { // Definicija legende unutar 'plugins' objekta
        display: true, // Prikazuje legendu
        position: 'top', // Pozicija legende (može biti 'top', 'bottom', 'left', 'right')
        labels: {
          font: {
            size: 16, // Veličina fonta za legendu
          }
        },
        onClick: (e, legendItem) => {
          // Opcionalno: Omogućava klik na legendu, npr. za filtriranje
          // Ovdje možete dodati logiku ako želite da klik na legendu uklanja ili dodaje podatke u grafikon
        }
      }
    },
    interaction: {
      mode: 'nearest', // Tooltip se prikazuje kada se miš nalazi najbliže segmentu
      intersect: false, // Tooltip se prikazuje i kada miš nije direktno na segmentu
    }
  };
  

  // Postavi boje direktno u 'datasets'
  public radialChartDataSet: ChartData = {
    datasets: [
      {
        data: this.radialChartData,
        backgroundColor: ['#FF5733', '#33FF57', '#3357FF'], // Boje sektora
      }
    ]
  };

  constructor(private changeDetectorRef: ChangeDetectorRef,private postService: PostService, private commentService: CommentService, private loginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.loadComments();
  }

  loadComments(): void {
    this.commentService.getAllComments().subscribe(
      (data) => {
        this.commetns = data.results;
        this.loadPosts();
      },
      (error) => {
        console.error('Error loading comments', error);
      }
    );
  }

  loadPosts(): void {
    this.postService.getAllPosts().subscribe(
      (data) => {
        this.posts = data.results;
        this.calculatePostCommentCounts();
        this.loadUsers();
      },
      (error) => {
        console.error('Error loading posts', error);
      }
    );
  }

  loadUsers(): void {
    this.loginService.getUsers().subscribe(
      (data) => {
        this.users = data.results.filter(user => user.loginStatus === true);
        this.updateUserPostCounts();
      },
      (error) => {
        console.error('Error loading users', error);
      }
    );
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
    this.updateUserCommentCounts();
  }

  updateUserCommentCounts(): void {
    this.users.forEach(user => {
      if (user.commentNumber === undefined) {
        user.commentNumber = 0;
      }
    });

    this.commetns.forEach(comment => {
      const user = this.users.find(user => user.username === comment.username);
      if (user) {
        user.commentNumber += 1;
      }
    });
    this.updateRadialChartData();
  }

  updateRadialChartData(): void {
    const posted = this.users.filter(user => user.postNumber > 0).length;
    const commentedOnly = this.users.filter(user => user.commentNumber > 0 && user.postNumber === 0).length;
    const didNothing = this.users.filter(user => user.postNumber === 0 && user.commentNumber === 0).length;

    const totalUsers = this.users.length;
    this.radialChartData = [
      (posted / totalUsers) * 100,
      (commentedOnly / totalUsers) * 100,
      (didNothing / totalUsers) * 100
    ];

    // Osveži podatke za grafikon
    this.radialChartDataSet.datasets[0].data = this.radialChartData;
    this.changeDetectorRef.detectChanges();
    this.isLoading = false;  // Kada su podaci učitani, isključujemo indikator učitavanja
    
  }


  private isWithinLast(date: Date, days: number): boolean {
    if (!(date instanceof Date) || isNaN(date.getTime())) {
      console.error("Invalid date:", date);
      return false;
    }
    const now = new Date();
    const differenceInTime = now.getTime() - date.getTime();
    const differenceInDays = differenceInTime / (1000 * 3600 * 24);
    return differenceInDays <= days;
  }

  // Funkcija koja računa broj objava i komentara za nedeljni, mesečni i godišnji nivo
  calculatePostCommentCounts(): void {
    this.postCommentCounts.weeklyPosts = this.posts.filter(post => this.isWithinLast(new Date(post.creationTime), 7)).length;
    this.postCommentCounts.monthlyPosts = this.posts.filter(post => this.isWithinLast(new Date(post.creationTime), 30)).length;
    this.postCommentCounts.yearlyPosts = this.posts.filter(post => this.isWithinLast(new Date(post.creationTime), 365)).length;
  
    this.postCommentCounts.weeklyComments = this.commetns.filter(comment => this.isWithinLast(new Date(comment.creationTime), 7)).length;
    this.postCommentCounts.monthlyComments = this.commetns.filter(comment => this.isWithinLast(new Date(comment.creationTime), 30)).length;
    this.postCommentCounts.yearlyComments = this.commetns.filter(comment => this.isWithinLast(new Date(comment.creationTime), 365)).length;
  }
  

}
