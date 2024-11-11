import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ProfileComponent } from './profile/profile.component';
import { PostCreationComponent } from './post-creation/post-creation.component';
import { UsersProfilComponent } from './users-profil/users-profil.component';



const routes: Routes = [

  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent},
  { path: 'home', component: HomePageComponent },
  {path: 'profile', component: ProfileComponent},
  {path: 'usersprofile', component: UsersProfilComponent},
  {path: 'postcreate', component: PostCreationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
