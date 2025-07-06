import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { JWT_OPTIONS, JwtHelperService, JwtInterceptor } from '@auth0/angular-jwt';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from './navbar/navbar.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { HttpClientModule } from '@angular/common/http';
import { SideNavbarComponent } from './side-navbar/side-navbar.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HomePageComponent } from './home-page/home-page.component';
import { ProfileComponent } from './profile/profile.component';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { PostCreationComponent } from './post-creation/post-creation.component';
import { PostService } from './post-service.service';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { UsersProfilComponent } from './users-profil/users-profil.component';
import { AppAnalyticsComponent } from './app-analytics/app-analytics.component';
import { NgChartsModule  } from 'ng2-charts';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MapPostComponent } from './map-post/map-post.component';
import { TrendsComponent } from './trends/trends.component';
import { ChatsComponent } from './chats/chats.component';
import { ChatOpenComponent } from './chat-open/chat-open.component';  // Uvoz MatProgressSpinnerModule
import { MatTooltipModule } from '@angular/material/tooltip'; // Importuj MatTooltipModule
import { RecommendedLocationsComponent } from './recommended-locations/recommended-locations.component';  // Uvoz MatProgressSpinnerModule


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    SignupComponent,
    SideNavbarComponent,
    HomePageComponent,
    ProfileComponent,
    UserProfileComponent,
    PostCreationComponent,
    UsersProfilComponent,
    AppAnalyticsComponent,
    MapPostComponent,
    TrendsComponent,
    ChatsComponent,
    ChatOpenComponent,
    TrendsComponent,
    RecommendedLocationsComponent
    

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatInputModule,
    ReactiveFormsModule,
    HttpClientModule,
    FontAwesomeModule,
    MatCardModule,
    FormsModule,
    MatIconModule,
    MatSnackBarModule,
    NgChartsModule ,
    MatProgressSpinnerModule,  
    MatTooltipModule
  ],
  providers: [{
    provide: JWT_OPTIONS,
    useValue: JWT_OPTIONS
  },
  JwtHelperService,
  {
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true,
  },
  PostService
],
  bootstrap: [AppComponent]
})
export class AppModule { }
