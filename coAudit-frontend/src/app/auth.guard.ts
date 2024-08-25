import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    const token = localStorage.getItem('token');

    if (token) {
      // If token exists, allow access to the route
      return true;
    } else {
      // If no token, redirect to login page
      this.router.navigate(['/authentication/login']);
      return false;
    }
  }
}
