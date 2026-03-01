import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlocareTratament, NewAlocareTratament } from '../alocare-tratament.model';

export type PartialUpdateAlocareTratament = Partial<IAlocareTratament> & Pick<IAlocareTratament, 'id'>;

type RestOf<T extends IAlocareTratament | NewAlocareTratament> = Omit<T, 'dataDecizie'> & {
  dataDecizie?: string | null;
};

export type RestAlocareTratament = RestOf<IAlocareTratament>;

export type NewRestAlocareTratament = RestOf<NewAlocareTratament>;

export type PartialUpdateRestAlocareTratament = RestOf<PartialUpdateAlocareTratament>;

export type EntityResponseType = HttpResponse<IAlocareTratament>;
export type EntityArrayResponseType = HttpResponse<IAlocareTratament[]>;

@Injectable({ providedIn: 'root' })
export class AlocareTratamentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alocare-trataments');

  create(alocareTratament: NewAlocareTratament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alocareTratament);
    return this.http
      .post<RestAlocareTratament>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alocareTratament: IAlocareTratament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alocareTratament);
    return this.http
      .put<RestAlocareTratament>(`${this.resourceUrl}/${this.getAlocareTratamentIdentifier(alocareTratament)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alocareTratament: PartialUpdateAlocareTratament): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alocareTratament);
    return this.http
      .patch<RestAlocareTratament>(`${this.resourceUrl}/${this.getAlocareTratamentIdentifier(alocareTratament)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlocareTratament>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlocareTratament[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  reevaluate(id: number): Observable<EntityResponseType> {
    return this.http
      .post<RestAlocareTratament>(`${this.resourceUrl}/${id}/reevaluate`, null, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getAlocareTratamentIdentifier(alocareTratament: Pick<IAlocareTratament, 'id'>): number {
    return alocareTratament.id;
  }

  compareAlocareTratament(o1: Pick<IAlocareTratament, 'id'> | null, o2: Pick<IAlocareTratament, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlocareTratamentIdentifier(o1) === this.getAlocareTratamentIdentifier(o2) : o1 === o2;
  }

  addAlocareTratamentToCollectionIfMissing<Type extends Pick<IAlocareTratament, 'id'>>(
    alocareTratamentCollection: Type[],
    ...alocareTratamentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alocareTrataments: Type[] = alocareTratamentsToCheck.filter(isPresent);
    if (alocareTrataments.length > 0) {
      const alocareTratamentCollectionIdentifiers = alocareTratamentCollection.map(alocareTratamentItem =>
        this.getAlocareTratamentIdentifier(alocareTratamentItem),
      );
      const alocareTratamentsToAdd = alocareTrataments.filter(alocareTratamentItem => {
        const alocareTratamentIdentifier = this.getAlocareTratamentIdentifier(alocareTratamentItem);
        if (alocareTratamentCollectionIdentifiers.includes(alocareTratamentIdentifier)) {
          return false;
        }
        alocareTratamentCollectionIdentifiers.push(alocareTratamentIdentifier);
        return true;
      });
      return [...alocareTratamentsToAdd, ...alocareTratamentCollection];
    }
    return alocareTratamentCollection;
  }

  protected convertDateFromClient<T extends IAlocareTratament | NewAlocareTratament | PartialUpdateAlocareTratament>(
    alocareTratament: T,
  ): RestOf<T> {
    return {
      ...alocareTratament,
      dataDecizie: alocareTratament.dataDecizie?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAlocareTratament: RestAlocareTratament): IAlocareTratament {
    return {
      ...restAlocareTratament,
      dataDecizie: restAlocareTratament.dataDecizie ? dayjs(restAlocareTratament.dataDecizie) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlocareTratament>): HttpResponse<IAlocareTratament> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlocareTratament[]>): HttpResponse<IAlocareTratament[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
