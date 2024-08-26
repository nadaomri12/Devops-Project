import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private http: HttpClient,private router: Router) { }

  getAllProcessObjective(id:any):Observable<any>{
    return this.http.get<[]>(`api/objectivebyprocess/${id}`)
  }

  deleteObj(id:any){
    return this.http.delete(`api/objective/${id}`)
  }

  updateObjective(id: any, objective: any): Observable<any> {
    return this.http.put<any>(`api/objective/${id}`, objective);
  }
  addObjective(objective:any): Observable<any> { 
    return this.http.post<any>(`api/objective`,objective);
  }
//pour operation 
  // Add a new operation
  private apiUrl = `api/operation`; 
  addOperation(Operation:any): Observable<any> { 
    return this.http.post<any>(`api/operation`,Operation);
  }
  // Delete an operation by ID
  deleteOperation(id: any): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Update an operation by ID
  updateOperation(id: any, operation: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, operation);
  }

  // Get an operation by ID
  getOperation(id: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
  getAllProcessOperation(id:any):Observable<any>{
    return this.http.get<[]>(`api/operationbyprocess/${id}`)
  }
  // Get all tasks related to a specific operation
  
  getAllOperationTask(id:any):Observable<any>{
    return this.http.get<[]>(`api/taskbyoperation/${id}`)
  }
  getAllOperations():Observable<any>{
    return this.http.get<[]>(`api/operation`)
  }
  
  

}

