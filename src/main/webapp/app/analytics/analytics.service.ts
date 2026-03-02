import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChartPointDTO {
  label: string;
  value: number;
}

export interface HistogramBinDTO {
  from: number;
  to: number;
  count: number;
}

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  private http = inject(HttpClient);

  alocariByMedicament(dataSource: 'REAL' | 'SIMULAT' | 'ALL' = 'ALL'): Observable<ChartPointDTO[]> {
    return this.http.get<ChartPointDTO[]>('api/analytics/alocari/by-medicament', { params: { dataSource } });
  }

  scorHistogram(medicament: string | null, bins = 10, dataSource: 'REAL' | 'SIMULAT' | 'ALL' = 'ALL'): Observable<HistogramBinDTO[]> {
    const params: Record<string, string> = { bins: String(bins), dataSource };
    if (medicament) params['medicament'] = medicament;
    return this.http.get<HistogramBinDTO[]>('api/analytics/scor/histogram', { params });
  }

  alocariByMonth(dataSource: 'REAL' | 'SIMULAT' | 'ALL' = 'ALL'): Observable<ChartPointDTO[]> {
    return this.http.get<ChartPointDTO[]>('api/analytics/alocari/by-month', { params: { dataSource } });
  }

  pacientByAgeGroup(dataSource: 'REAL' | 'SIMULAT' | 'ALL' = 'ALL'): Observable<ChartPointDTO[]> {
    return this.http.get<ChartPointDTO[]>('api/analytics/pacienti/by-age-group', { params: { dataSource } });
  }
}
