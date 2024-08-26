import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,private router: Router) { }
  login(userObject:any): Observable<any> { 
    return this.http.post<any>( `api/auth/authenticate` ,userObject);
  }

  signUp(userObject:any): Observable<any> { 
    return this.http.post<any>(`api/auth/register`,userObject);
  }

 
  getAllUsers(offset: number, pageSize: number): Observable<any> {
    let params = new HttpParams()
      .set('offset', offset.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<[]>(`api/users`, { params });
  }
  deleteUser(id:any){
    return this.http.delete(`api/user`+id)
  }
  
  getUser(id:any):Observable<any>{
    return this.http.get(`api/user`+id)
  }
  updateUser(id: any, userObject: any): Observable<any> {
    return this.http.put<any>(`api/user` + id, userObject);
  }
  signOut() {
    localStorage.clear();
    this.router.navigate(['/authentication/login']);
  }
}
