import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMonitorizare, NewMonitorizare } from '../monitorizare.model';

export type PartialUpdateMonitorizare = Partial<IMonitorizare> & Pick<IMonitorizare, 'id'>;

type RestOf<T extends IMonitorizare | NewMonitorizare> = Omit<T, 'dataInstant'> & {
  dataInstant?: string | null;
};

export type RestMonitorizare = RestOf<IMonitorizare>;

export type NewRestMonitorizare = RestOf<NewMonitorizare>;

export type PartialUpdateRestMonitorizare = RestOf<PartialUpdateMonitorizare>;

export type EntityResponseType = HttpResponse<IMonitorizare>;
export type EntityArrayResponseType = HttpResponse<IMonitorizare[]>;

@Injectable({ providedIn: 'root' })
export class MonitorizareService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monitorizares');

  create(monitorizare: NewMonitorizare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitorizare);
    return this.http
      .post<RestMonitorizare>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(monitorizare: IMonitorizare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitorizare);
    return this.http
      .put<RestMonitorizare>(`${this.resourceUrl}/${this.getMonitorizareIdentifier(monitorizare)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(monitorizare: PartialUpdateMonitorizare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitorizare);
    return this.http
      .patch<RestMonitorizare>(`${this.resourceUrl}/${this.getMonitorizareIdentifier(monitorizare)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMonitorizare>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMonitorizare[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMonitorizareIdentifier(monitorizare: Pick<IMonitorizare, 'id'>): number {
    return monitorizare.id;
  }

  compareMonitorizare(o1: Pick<IMonitorizare, 'id'> | null, o2: Pick<IMonitorizare, 'id'> | null): boolean {
    return o1 && o2 ? this.getMonitorizareIdentifier(o1) === this.getMonitorizareIdentifier(o2) : o1 === o2;
  }

  addMonitorizareToCollectionIfMissing<Type extends Pick<IMonitorizare, 'id'>>(
    monitorizareCollection: Type[],
    ...monitorizaresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monitorizares: Type[] = monitorizaresToCheck.filter(isPresent);
    if (monitorizares.length > 0) {
      const monitorizareCollectionIdentifiers = monitorizareCollection.map(monitorizareItem =>
        this.getMonitorizareIdentifier(monitorizareItem),
      );
      const monitorizaresToAdd = monitorizares.filter(monitorizareItem => {
        const monitorizareIdentifier = this.getMonitorizareIdentifier(monitorizareItem);
        if (monitorizareCollectionIdentifiers.includes(monitorizareIdentifier)) {
          return false;
        }
        monitorizareCollectionIdentifiers.push(monitorizareIdentifier);
        return true;
      });
      return [...monitorizaresToAdd, ...monitorizareCollection];
    }
    return monitorizareCollection;
  }

  protected convertDateFromClient<T extends IMonitorizare | NewMonitorizare | PartialUpdateMonitorizare>(monitorizare: T): RestOf<T> {
    return {
      ...monitorizare,
      dataInstant: monitorizare.dataInstant?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMonitorizare: RestMonitorizare): IMonitorizare {
    return {
      ...restMonitorizare,
      dataInstant: restMonitorizare.dataInstant ? dayjs(restMonitorizare.dataInstant) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMonitorizare>): HttpResponse<IMonitorizare> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMonitorizare[]>): HttpResponse<IMonitorizare[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
