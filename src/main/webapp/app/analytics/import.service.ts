import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ImportService {
  private http = inject(HttpClient);

  importPacientiCsv(file: File, dataSource: 'SIMULAT' | 'REAL' = 'SIMULAT'): Observable<{ imported: number; dataSource: string }> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<{ imported: number; dataSource: string }>('api/import/pacienti-alocari', form, { params: { dataSource } });
  }
}
