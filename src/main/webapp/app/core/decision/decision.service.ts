import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IEvaluareDecizieCerere, IEvaluareDecizieRezultat } from './decision.model';

@Injectable({ providedIn: 'root' })
export class DecisionService {
  private readonly resourceUrl = '/api/decizie-tratament';

  constructor(private http: HttpClient) {}

  evalueaza(cerere: IEvaluareDecizieCerere): Observable<IEvaluareDecizieRezultat> {
    return this.http.post<IEvaluareDecizieRezultat>(this.resourceUrl, cerere);
  }
}
