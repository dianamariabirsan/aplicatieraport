import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPacient, NewPacient } from '../pacient.model';

export type PartialUpdatePacient = Partial<IPacient> & Pick<IPacient, 'id'>;

export type EntityResponseType = HttpResponse<IPacient>;
export type EntityArrayResponseType = HttpResponse<IPacient[]>;

@Injectable({ providedIn: 'root' })
export class PacientService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pacients');

  create(pacient: NewPacient): Observable<EntityResponseType> {
    return this.http.post<IPacient>(this.resourceUrl, pacient, { observe: 'response' });
  }

  update(pacient: IPacient): Observable<EntityResponseType> {
    return this.http.put<IPacient>(`${this.resourceUrl}/${this.getPacientIdentifier(pacient)}`, pacient, { observe: 'response' });
  }

  partialUpdate(pacient: PartialUpdatePacient): Observable<EntityResponseType> {
    return this.http.patch<IPacient>(`${this.resourceUrl}/${this.getPacientIdentifier(pacient)}`, pacient, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPacient>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPacient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPacientIdentifier(pacient: Pick<IPacient, 'id'>): number {
    return pacient.id;
  }

  comparePacient(o1: Pick<IPacient, 'id'> | null, o2: Pick<IPacient, 'id'> | null): boolean {
    return o1 && o2 ? this.getPacientIdentifier(o1) === this.getPacientIdentifier(o2) : o1 === o2;
  }

  addPacientToCollectionIfMissing<Type extends Pick<IPacient, 'id'>>(
    pacientCollection: Type[],
    ...pacientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pacients: Type[] = pacientsToCheck.filter(isPresent);
    if (pacients.length > 0) {
      const pacientCollectionIdentifiers = pacientCollection.map(pacientItem => this.getPacientIdentifier(pacientItem));
      const pacientsToAdd = pacients.filter(pacientItem => {
        const pacientIdentifier = this.getPacientIdentifier(pacientItem);
        if (pacientCollectionIdentifiers.includes(pacientIdentifier)) {
          return false;
        }
        pacientCollectionIdentifiers.push(pacientIdentifier);
        return true;
      });
      return [...pacientsToAdd, ...pacientCollection];
    }
    return pacientCollection;
  }
}
