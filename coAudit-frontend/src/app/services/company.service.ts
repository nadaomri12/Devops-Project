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
    return this.http.post<any>(`api/company` ,FormData);
  }
  updatecompany(id: any, company: any): Observable<any> {
    return this.http.put<any>(`api/company` + id, company);
  }
  getcompany():Observable<any>{
    return this.http.get<[]>(`api/company`)
  }
}
