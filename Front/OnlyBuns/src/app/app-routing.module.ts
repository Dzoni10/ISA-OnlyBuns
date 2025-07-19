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
import { MapPostComponent } from './map-post/map-post.component';
import { TrendsComponent } from './trends/trends.component';
import { RecommendedLocationsComponent } from './recommended-locations/recommended-locations.component';
import { ChatsComponent } from './chats/chats.component';
import { ChatOpenComponent } from './chat-open/chat-open.component';


const routes: Routes = [

  {path: 'login', component: LoginComponent},
  {path: 'chats', component: ChatsComponent , canActivate:[AuthGuard]},
  {path: 'chat/:chatId', component: ChatOpenComponent , canActivate:[AuthGuard]},
  {path: 'signup', component: SignupComponent},
  {path: 'home', component: HomePageComponent },
  {path: 'app-analytics', component: AppAnalyticsComponent, canActivate: [AuthGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'usersprofile', component: UsersProfilComponent, canActivate: [AuthGuard]},
  {path: 'postcreate', component: PostCreationComponent, canActivate: [AuthGuard]},
  {path: 'user-profile/:username', component: UserProfileComponent},
  {path: 'postOnMap', component:MapPostComponent, canActivate:[AuthGuard]},
  {path: 'trends', component:TrendsComponent, canActivate:[AuthGuard]},
  {path: 'recommendedLocations', component:RecommendedLocationsComponent,canActivate:[AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
