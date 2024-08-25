import { Injectable } from '@angular/core';

import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient,private router: Router) { }
  addTask(Task:any): Observable<any> { 
    return this.http.post<any>('http://localhost:8080/api/task',Task);
  }
deleteTask(id:any){
    return this.http.delete('http://localhost:8080/api/task/'+id)
  }

  updateTask(id: any, task: any): Observable<any> {
    return this.http.put<any>('http://localhost:8080/api/task/' + id, task);
  }

getAllDataTask(id:any):Observable<any>{
  return this.http.get<[]>('http://localhost:8080/api/task/'+ id+'/data')
}
 
}
