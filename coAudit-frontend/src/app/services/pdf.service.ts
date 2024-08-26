import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PDFService {

  constructor(private http: HttpClient,private router: Router) { }
  exportUsersToPDF(): Observable<Blob> {
    return this.http.get( `api/users/export/pdf`, {
      responseType: 'blob'
    });
   
  }

  exportQPToPDF(): Observable<Blob> {
    return this.http.get(`api/qualitypolicy/export/pdf`, {
      responseType: 'blob'
    });
   
  }

  exportProcessToPDF(id:any): Observable<Blob> {
    return this.http.get(`api/process/export/pdf/${id}`, {
      responseType: 'blob'
    });
   
  }
  exportPosteToPDF(id:any): Observable<Blob> {
    return this.http.get(`api/poste/exportfichedeposte/${id}`, {
      responseType: 'blob'
    });
   
  }
 exportObjectiveProcessToPDF(id: any): Observable<Blob> {
    return this.http.get(`api/${id}/objectives/export/pdf`, {
      responseType: 'blob'
    });
  }

  exportOPERATIONProcessToPDF(id: any): Observable<Blob> {
    return this.http.get(`api/${id}/operations/export/pdf`, {
      responseType: 'blob'
    });
  }
  }

