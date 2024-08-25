import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class QualitypolicyService {

  constructor(private http: HttpClient,private router: Router) { }
  
  
  addQuality(quality:any): Observable<any> { 
    return this.http.post<any>('http://localhost:8080/api/qualitypolicy',quality);
  }
  getAllQualityPolicy():Observable<any>{
    return this.http.get<[]>('http://localhost:8080/api/qualitypolicy')
  }
  deleteQualityPolicy(id:any){
    return this.http.delete('http://localhost:8080/api/qualitypolicy/'+id)
  }
  
  getQualityPolicy(id:any):Observable<any>{
    return this.http.get('http://localhost:8080/api/qualitypolicy/'+id)
  }
  updateQualityPolicy(id: any, quality: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/qualitypolicy/' + id, quality);
  }
 
}
