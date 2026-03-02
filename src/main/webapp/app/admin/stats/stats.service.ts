import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface StatsSummary {
  totalPacienti: number;
  distributieTratament: Record<string, number>;
  histVarsta: Record<string, number>;
  histIMC: Record<string, number>;
}

@Injectable({ providedIn: 'root' })
export class StatsService {
  constructor(private http: HttpClient) {}

  summary(): Observable<StatsSummary> {
    return this.http.get<StatsSummary>('/api/stats/summary');
  }
}
