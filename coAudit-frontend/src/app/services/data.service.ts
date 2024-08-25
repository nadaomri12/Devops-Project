import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient,private router: Router) { }
  
  getAllData():Observable<any>{
    return this.http.get<[]>('http://localhost:8080/api/data')
  }
   
  addData(data:any): Observable<any> { 
    return this.http.post<any>('http://localhost:8080/api/data',data);
  }
deleteData(id:any){
    return this.http.delete('http://localhost:8080/api/data/'+id)
  }

  updateData(taskDataId:any,id: any, data: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/data/taskData/' + taskDataId, data);
  }
}