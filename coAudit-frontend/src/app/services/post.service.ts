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
    return this.http.get<[]>(`api/poste`)
  }
   updatePoste(id: any, Poste: any): Observable<any> {
    return this.http.put<any>(`api/poste` + id, Poste);
  }
  
  addJob(Poste:any): Observable<any> { 
    return this.http.post<any>(`api/poste`,Poste);
  }
  
  deletePoste(id:any){
    return this.http.delete(`api/poste`+id)
  }
}
