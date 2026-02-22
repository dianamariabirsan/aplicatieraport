import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReactieAdversa, NewReactieAdversa } from '../reactie-adversa.model';

export type PartialUpdateReactieAdversa = Partial<IReactieAdversa> & Pick<IReactieAdversa, 'id'>;

type RestOf<T extends IReactieAdversa | NewReactieAdversa> = Omit<T, 'dataRaportare'> & {
  dataRaportare?: string | null;
};

export type RestReactieAdversa = RestOf<IReactieAdversa>;

export type NewRestReactieAdversa = RestOf<NewReactieAdversa>;

export type PartialUpdateRestReactieAdversa = RestOf<PartialUpdateReactieAdversa>;

export type EntityResponseType = HttpResponse<IReactieAdversa>;
export type EntityArrayResponseType = HttpResponse<IReactieAdversa[]>;

@Injectable({ providedIn: 'root' })
export class ReactieAdversaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reactie-adversas');

  create(reactieAdversa: NewReactieAdversa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reactieAdversa);
    return this.http
      .post<RestReactieAdversa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reactieAdversa: IReactieAdversa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reactieAdversa);
    return this.http
      .put<RestReactieAdversa>(`${this.resourceUrl}/${this.getReactieAdversaIdentifier(reactieAdversa)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reactieAdversa: PartialUpdateReactieAdversa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reactieAdversa);
    return this.http
      .patch<RestReactieAdversa>(`${this.resourceUrl}/${this.getReactieAdversaIdentifier(reactieAdversa)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReactieAdversa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReactieAdversa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReactieAdversaIdentifier(reactieAdversa: Pick<IReactieAdversa, 'id'>): number {
    return reactieAdversa.id;
  }

  compareReactieAdversa(o1: Pick<IReactieAdversa, 'id'> | null, o2: Pick<IReactieAdversa, 'id'> | null): boolean {
    return o1 && o2 ? this.getReactieAdversaIdentifier(o1) === this.getReactieAdversaIdentifier(o2) : o1 === o2;
  }

  addReactieAdversaToCollectionIfMissing<Type extends Pick<IReactieAdversa, 'id'>>(
    reactieAdversaCollection: Type[],
    ...reactieAdversasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reactieAdversas: Type[] = reactieAdversasToCheck.filter(isPresent);
    if (reactieAdversas.length > 0) {
      const reactieAdversaCollectionIdentifiers = reactieAdversaCollection.map(reactieAdversaItem =>
        this.getReactieAdversaIdentifier(reactieAdversaItem),
      );
      const reactieAdversasToAdd = reactieAdversas.filter(reactieAdversaItem => {
        const reactieAdversaIdentifier = this.getReactieAdversaIdentifier(reactieAdversaItem);
        if (reactieAdversaCollectionIdentifiers.includes(reactieAdversaIdentifier)) {
          return false;
        }
        reactieAdversaCollectionIdentifiers.push(reactieAdversaIdentifier);
        return true;
      });
      return [...reactieAdversasToAdd, ...reactieAdversaCollection];
    }
    return reactieAdversaCollection;
  }

  protected convertDateFromClient<T extends IReactieAdversa | NewReactieAdversa | PartialUpdateReactieAdversa>(
    reactieAdversa: T,
  ): RestOf<T> {
    return {
      ...reactieAdversa,
      dataRaportare: reactieAdversa.dataRaportare?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReactieAdversa: RestReactieAdversa): IReactieAdversa {
    return {
      ...restReactieAdversa,
      dataRaportare: restReactieAdversa.dataRaportare ? dayjs(restReactieAdversa.dataRaportare) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReactieAdversa>): HttpResponse<IReactieAdversa> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReactieAdversa[]>): HttpResponse<IReactieAdversa[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
