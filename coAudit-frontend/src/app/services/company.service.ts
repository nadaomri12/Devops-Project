import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private http: HttpClient,private router: Router) { }
  addcompany(FormData:any): Observable<any> { 
    return this.http.post<any>('http://localhost:8080/api/company' ,FormData);
  }
  updatecompany(id: any, company: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/company/' + id, company);
  }
  getcompany():Observable<any>{
    return this.http.get<[]>('http://localhost:8080/api/company')
  }
}
