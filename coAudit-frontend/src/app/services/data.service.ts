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
    return this.http.get<[]>(`api/data`)
  }
   
  addData(data:any): Observable<any> { 
    return this.http.post<any>(`api/data`,data);
  }
deleteData(id:any){
    return this.http.delete(`api/data/${id}`)
  }

  updateData(taskDataId:any,id: any, data: any): Observable<any> {
    return this.http.put<any>(`api/data/taskData/` + taskDataId, data);
  }
}