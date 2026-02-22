import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFarmacist, NewFarmacist } from '../farmacist.model';

export type PartialUpdateFarmacist = Partial<IFarmacist> & Pick<IFarmacist, 'id'>;

export type EntityResponseType = HttpResponse<IFarmacist>;
export type EntityArrayResponseType = HttpResponse<IFarmacist[]>;

@Injectable({ providedIn: 'root' })
export class FarmacistService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/farmacists');

  create(farmacist: NewFarmacist): Observable<EntityResponseType> {
    return this.http.post<IFarmacist>(this.resourceUrl, farmacist, { observe: 'response' });
  }

  update(farmacist: IFarmacist): Observable<EntityResponseType> {
    return this.http.put<IFarmacist>(`${this.resourceUrl}/${this.getFarmacistIdentifier(farmacist)}`, farmacist, { observe: 'response' });
  }

  partialUpdate(farmacist: PartialUpdateFarmacist): Observable<EntityResponseType> {
    return this.http.patch<IFarmacist>(`${this.resourceUrl}/${this.getFarmacistIdentifier(farmacist)}`, farmacist, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFarmacist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFarmacist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFarmacistIdentifier(farmacist: Pick<IFarmacist, 'id'>): number {
    return farmacist.id;
  }

  compareFarmacist(o1: Pick<IFarmacist, 'id'> | null, o2: Pick<IFarmacist, 'id'> | null): boolean {
    return o1 && o2 ? this.getFarmacistIdentifier(o1) === this.getFarmacistIdentifier(o2) : o1 === o2;
  }

  addFarmacistToCollectionIfMissing<Type extends Pick<IFarmacist, 'id'>>(
    farmacistCollection: Type[],
    ...farmacistsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const farmacists: Type[] = farmacistsToCheck.filter(isPresent);
    if (farmacists.length > 0) {
      const farmacistCollectionIdentifiers = farmacistCollection.map(farmacistItem => this.getFarmacistIdentifier(farmacistItem));
      const farmacistsToAdd = farmacists.filter(farmacistItem => {
        const farmacistIdentifier = this.getFarmacistIdentifier(farmacistItem);
        if (farmacistCollectionIdentifiers.includes(farmacistIdentifier)) {
          return false;
        }
        farmacistCollectionIdentifiers.push(farmacistIdentifier);
        return true;
      });
      return [...farmacistsToAdd, ...farmacistCollection];
    }
    return farmacistCollection;
  }
}
