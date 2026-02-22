import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedic, NewMedic } from '../medic.model';

export type PartialUpdateMedic = Partial<IMedic> & Pick<IMedic, 'id'>;

export type EntityResponseType = HttpResponse<IMedic>;
export type EntityArrayResponseType = HttpResponse<IMedic[]>;

@Injectable({ providedIn: 'root' })
export class MedicService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medics');

  create(medic: NewMedic): Observable<EntityResponseType> {
    return this.http.post<IMedic>(this.resourceUrl, medic, { observe: 'response' });
  }

  update(medic: IMedic): Observable<EntityResponseType> {
    return this.http.put<IMedic>(`${this.resourceUrl}/${this.getMedicIdentifier(medic)}`, medic, { observe: 'response' });
  }

  partialUpdate(medic: PartialUpdateMedic): Observable<EntityResponseType> {
    return this.http.patch<IMedic>(`${this.resourceUrl}/${this.getMedicIdentifier(medic)}`, medic, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedic>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedic[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicIdentifier(medic: Pick<IMedic, 'id'>): number {
    return medic.id;
  }

  compareMedic(o1: Pick<IMedic, 'id'> | null, o2: Pick<IMedic, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicIdentifier(o1) === this.getMedicIdentifier(o2) : o1 === o2;
  }

  addMedicToCollectionIfMissing<Type extends Pick<IMedic, 'id'>>(
    medicCollection: Type[],
    ...medicsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medics: Type[] = medicsToCheck.filter(isPresent);
    if (medics.length > 0) {
      const medicCollectionIdentifiers = medicCollection.map(medicItem => this.getMedicIdentifier(medicItem));
      const medicsToAdd = medics.filter(medicItem => {
        const medicIdentifier = this.getMedicIdentifier(medicItem);
        if (medicCollectionIdentifiers.includes(medicIdentifier)) {
          return false;
        }
        medicCollectionIdentifiers.push(medicIdentifier);
        return true;
      });
      return [...medicsToAdd, ...medicCollection];
    }
    return medicCollection;
  }
}
