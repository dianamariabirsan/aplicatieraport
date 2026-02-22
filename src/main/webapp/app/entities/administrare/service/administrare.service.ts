import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdministrare, NewAdministrare } from '../administrare.model';

export type PartialUpdateAdministrare = Partial<IAdministrare> & Pick<IAdministrare, 'id'>;

type RestOf<T extends IAdministrare | NewAdministrare> = Omit<T, 'dataAdministrare'> & {
  dataAdministrare?: string | null;
};

export type RestAdministrare = RestOf<IAdministrare>;

export type NewRestAdministrare = RestOf<NewAdministrare>;

export type PartialUpdateRestAdministrare = RestOf<PartialUpdateAdministrare>;

export type EntityResponseType = HttpResponse<IAdministrare>;
export type EntityArrayResponseType = HttpResponse<IAdministrare[]>;

@Injectable({ providedIn: 'root' })
export class AdministrareService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/administrares');

  create(administrare: NewAdministrare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(administrare);
    return this.http
      .post<RestAdministrare>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(administrare: IAdministrare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(administrare);
    return this.http
      .put<RestAdministrare>(`${this.resourceUrl}/${this.getAdministrareIdentifier(administrare)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(administrare: PartialUpdateAdministrare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(administrare);
    return this.http
      .patch<RestAdministrare>(`${this.resourceUrl}/${this.getAdministrareIdentifier(administrare)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAdministrare>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAdministrare[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdministrareIdentifier(administrare: Pick<IAdministrare, 'id'>): number {
    return administrare.id;
  }

  compareAdministrare(o1: Pick<IAdministrare, 'id'> | null, o2: Pick<IAdministrare, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdministrareIdentifier(o1) === this.getAdministrareIdentifier(o2) : o1 === o2;
  }

  addAdministrareToCollectionIfMissing<Type extends Pick<IAdministrare, 'id'>>(
    administrareCollection: Type[],
    ...administraresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const administrares: Type[] = administraresToCheck.filter(isPresent);
    if (administrares.length > 0) {
      const administrareCollectionIdentifiers = administrareCollection.map(administrareItem =>
        this.getAdministrareIdentifier(administrareItem),
      );
      const administraresToAdd = administrares.filter(administrareItem => {
        const administrareIdentifier = this.getAdministrareIdentifier(administrareItem);
        if (administrareCollectionIdentifiers.includes(administrareIdentifier)) {
          return false;
        }
        administrareCollectionIdentifiers.push(administrareIdentifier);
        return true;
      });
      return [...administraresToAdd, ...administrareCollection];
    }
    return administrareCollection;
  }

  protected convertDateFromClient<T extends IAdministrare | NewAdministrare | PartialUpdateAdministrare>(administrare: T): RestOf<T> {
    return {
      ...administrare,
      dataAdministrare: administrare.dataAdministrare?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAdministrare: RestAdministrare): IAdministrare {
    return {
      ...restAdministrare,
      dataAdministrare: restAdministrare.dataAdministrare ? dayjs(restAdministrare.dataAdministrare) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAdministrare>): HttpResponse<IAdministrare> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAdministrare[]>): HttpResponse<IAdministrare[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
