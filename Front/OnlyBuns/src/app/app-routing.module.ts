import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ProfileComponent } from './profile/profile.component';
import { PostCreationComponent } from './post-creation/post-creation.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UsersProfilComponent } from './users-profil/users-profil.component';
import { AppAnalyticsComponent } from './app-analytics/app-analytics.component';
import { AuthGuard } from './auth.guard';


const routes: Routes = [

  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent},
  { path: 'home', component: HomePageComponent },
  {path: 'app-analytics', component: AppAnalyticsComponent, canActivate: [AuthGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'usersprofile', component: UsersProfilComponent, canActivate: [AuthGuard]},
  {path: 'postcreate', component: PostCreationComponent, canActivate: [AuthGuard]},
  { path: 'user-profile/:username', component: UserProfileComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
