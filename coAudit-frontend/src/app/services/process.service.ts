import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ProcessService {

  constructor(private http: HttpClient,private router: Router) { }
  getAllProcess(offset: number, pageSize: number): Observable<any> {
    let params = new HttpParams()
      .set('offset', offset.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<[]>("http://localhost:8080/api/process", { params });
  }

  addProcess(process:any): Observable<any> { 
    return this.http.post<any>('http://localhost:8080/api/process',process);
  }
  deleteProcess(id:any){
    return this.http.delete('http://localhost:8080/api/process/'+id)
  }

  updateProcess(id: any, process: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/process/' + id, process);
  }
  getProcess(id:any):Observable<any>{
    return this.http.get('http://localhost:8080/api/process/'+id)
  }

}
