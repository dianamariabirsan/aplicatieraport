import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ICsvRaportAnaliticImportResult } from '../raport-analitic-import.model';

@Injectable({ providedIn: 'root' })
export class RaportAnaliticImportService {
  private readonly http = inject(HttpClient);
  private readonly applicationConfigService = inject(ApplicationConfigService);

  private readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/import/raport-analitic/csv');

  uploadCsv(file: File): Observable<ICsvRaportAnaliticImportResult> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<ICsvRaportAnaliticImportResult>(this.resourceUrl, formData);
  }
}
