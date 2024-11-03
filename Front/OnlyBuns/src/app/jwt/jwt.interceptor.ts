import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";

const ACCESS_TOKEN = 'access-token';

@Injectable()

export class JwtInterceptor implements HttpInterceptor {
  constructor(){}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessTokenRequest = request.clone({
      setHeaders:{
        Authorization: 'Bearer' +localStorage.getItem(ACCESS_TOKEN),
      },
    });
    return next.handle(accessTokenRequest);
  }
}
