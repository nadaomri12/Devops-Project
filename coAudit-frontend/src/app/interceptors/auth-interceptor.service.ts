import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Retrieve the token from localStorage
    const authToken = localStorage.getItem('token');
    console.log('Auth Token:', authToken); 

    
    const authReq = authToken ? req.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`
      }
    }) : req;

    // Pass the modified request to the next handler
    return next.handle(authReq);
  }
}
