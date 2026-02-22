import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRaportAnalitic, NewRaportAnalitic } from '../raport-analitic.model';

export type PartialUpdateRaportAnalitic = Partial<IRaportAnalitic> & Pick<IRaportAnalitic, 'id'>;

type RestOf<T extends IRaportAnalitic | NewRaportAnalitic> = Omit<T, 'perioadaStart' | 'perioadaEnd'> & {
  perioadaStart?: string | null;
  perioadaEnd?: string | null;
};

export type RestRaportAnalitic = RestOf<IRaportAnalitic>;

export type NewRestRaportAnalitic = RestOf<NewRaportAnalitic>;

export type PartialUpdateRestRaportAnalitic = RestOf<PartialUpdateRaportAnalitic>;

export type EntityResponseType = HttpResponse<IRaportAnalitic>;
export type EntityArrayResponseType = HttpResponse<IRaportAnalitic[]>;

@Injectable({ providedIn: 'root' })
export class RaportAnaliticService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/raport-analitics');

  create(raportAnalitic: NewRaportAnalitic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raportAnalitic);
    return this.http
      .post<RestRaportAnalitic>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(raportAnalitic: IRaportAnalitic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raportAnalitic);
    return this.http
      .put<RestRaportAnalitic>(`${this.resourceUrl}/${this.getRaportAnaliticIdentifier(raportAnalitic)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(raportAnalitic: PartialUpdateRaportAnalitic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raportAnalitic);
    return this.http
      .patch<RestRaportAnalitic>(`${this.resourceUrl}/${this.getRaportAnaliticIdentifier(raportAnalitic)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRaportAnalitic>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRaportAnalitic[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRaportAnaliticIdentifier(raportAnalitic: Pick<IRaportAnalitic, 'id'>): number {
    return raportAnalitic.id;
  }

  compareRaportAnalitic(o1: Pick<IRaportAnalitic, 'id'> | null, o2: Pick<IRaportAnalitic, 'id'> | null): boolean {
    return o1 && o2 ? this.getRaportAnaliticIdentifier(o1) === this.getRaportAnaliticIdentifier(o2) : o1 === o2;
  }

  addRaportAnaliticToCollectionIfMissing<Type extends Pick<IRaportAnalitic, 'id'>>(
    raportAnaliticCollection: Type[],
    ...raportAnaliticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const raportAnalitics: Type[] = raportAnaliticsToCheck.filter(isPresent);
    if (raportAnalitics.length > 0) {
      const raportAnaliticCollectionIdentifiers = raportAnaliticCollection.map(raportAnaliticItem =>
        this.getRaportAnaliticIdentifier(raportAnaliticItem),
      );
      const raportAnaliticsToAdd = raportAnalitics.filter(raportAnaliticItem => {
        const raportAnaliticIdentifier = this.getRaportAnaliticIdentifier(raportAnaliticItem);
        if (raportAnaliticCollectionIdentifiers.includes(raportAnaliticIdentifier)) {
          return false;
        }
        raportAnaliticCollectionIdentifiers.push(raportAnaliticIdentifier);
        return true;
      });
      return [...raportAnaliticsToAdd, ...raportAnaliticCollection];
    }
    return raportAnaliticCollection;
  }

  protected convertDateFromClient<T extends IRaportAnalitic | NewRaportAnalitic | PartialUpdateRaportAnalitic>(
    raportAnalitic: T,
  ): RestOf<T> {
    return {
      ...raportAnalitic,
      perioadaStart: raportAnalitic.perioadaStart?.toJSON() ?? null,
      perioadaEnd: raportAnalitic.perioadaEnd?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRaportAnalitic: RestRaportAnalitic): IRaportAnalitic {
    return {
      ...restRaportAnalitic,
      perioadaStart: restRaportAnalitic.perioadaStart ? dayjs(restRaportAnalitic.perioadaStart) : undefined,
      perioadaEnd: restRaportAnalitic.perioadaEnd ? dayjs(restRaportAnalitic.perioadaEnd) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRaportAnalitic>): HttpResponse<IRaportAnalitic> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRaportAnalitic[]>): HttpResponse<IRaportAnalitic[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
