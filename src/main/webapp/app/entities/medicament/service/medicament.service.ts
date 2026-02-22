import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicament, NewMedicament } from '../medicament.model';

export type PartialUpdateMedicament = Partial<IMedicament> & Pick<IMedicament, 'id'>;

export type EntityResponseType = HttpResponse<IMedicament>;
export type EntityArrayResponseType = HttpResponse<IMedicament[]>;

@Injectable({ providedIn: 'root' })
export class MedicamentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medicaments');

  create(medicament: NewMedicament): Observable<EntityResponseType> {
    return this.http.post<IMedicament>(this.resourceUrl, medicament, { observe: 'response' });
  }

  update(medicament: IMedicament): Observable<EntityResponseType> {
    return this.http.put<IMedicament>(`${this.resourceUrl}/${this.getMedicamentIdentifier(medicament)}`, medicament, {
      observe: 'response',
    });
  }

  partialUpdate(medicament: PartialUpdateMedicament): Observable<EntityResponseType> {
    return this.http.patch<IMedicament>(`${this.resourceUrl}/${this.getMedicamentIdentifier(medicament)}`, medicament, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedicament>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedicament[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicamentIdentifier(medicament: Pick<IMedicament, 'id'>): number {
    return medicament.id;
  }

  compareMedicament(o1: Pick<IMedicament, 'id'> | null, o2: Pick<IMedicament, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicamentIdentifier(o1) === this.getMedicamentIdentifier(o2) : o1 === o2;
  }

  addMedicamentToCollectionIfMissing<Type extends Pick<IMedicament, 'id'>>(
    medicamentCollection: Type[],
    ...medicamentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicaments: Type[] = medicamentsToCheck.filter(isPresent);
    if (medicaments.length > 0) {
      const medicamentCollectionIdentifiers = medicamentCollection.map(medicamentItem => this.getMedicamentIdentifier(medicamentItem));
      const medicamentsToAdd = medicaments.filter(medicamentItem => {
        const medicamentIdentifier = this.getMedicamentIdentifier(medicamentItem);
        if (medicamentCollectionIdentifiers.includes(medicamentIdentifier)) {
          return false;
        }
        medicamentCollectionIdentifiers.push(medicamentIdentifier);
        return true;
      });
      return [...medicamentsToAdd, ...medicamentCollection];
    }
    return medicamentCollection;
  }
}
