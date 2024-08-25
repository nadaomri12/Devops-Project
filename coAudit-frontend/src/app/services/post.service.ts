import { Injectable } from '@angular/core';

import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient,private router: Router) { }
  getPoste():Observable<any>{
    return this.http.get<[]>('http://localhost:8080/api/poste')
  }
   updatePoste(id: any, Poste: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/poste/' + id, Poste);
  }
  
  addJob(Poste:any): Observable<any> { 
    return this.http.post<any>('http://localhost:8080/api/poste',Poste);
  }
  
  deletePoste(id:any){
    return this.http.delete('http://localhost:8080/api/poste/'+id)
  }
}
