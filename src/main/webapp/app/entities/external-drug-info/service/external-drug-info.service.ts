import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExternalDrugInfo, NewExternalDrugInfo } from '../external-drug-info.model';

export type PartialUpdateExternalDrugInfo = Partial<IExternalDrugInfo> & Pick<IExternalDrugInfo, 'id'>;

type RestOf<T extends IExternalDrugInfo | NewExternalDrugInfo> = Omit<T, 'lastUpdated'> & {
  lastUpdated?: string | null;
};

export type RestExternalDrugInfo = RestOf<IExternalDrugInfo>;

export type NewRestExternalDrugInfo = RestOf<NewExternalDrugInfo>;

export type PartialUpdateRestExternalDrugInfo = RestOf<PartialUpdateExternalDrugInfo>;

export type EntityResponseType = HttpResponse<IExternalDrugInfo>;
export type EntityArrayResponseType = HttpResponse<IExternalDrugInfo[]>;

@Injectable({ providedIn: 'root' })
export class ExternalDrugInfoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/external-drug-infos');

  create(externalDrugInfo: NewExternalDrugInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(externalDrugInfo);
    return this.http
      .post<RestExternalDrugInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(externalDrugInfo: IExternalDrugInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(externalDrugInfo);
    return this.http
      .put<RestExternalDrugInfo>(`${this.resourceUrl}/${this.getExternalDrugInfoIdentifier(externalDrugInfo)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(externalDrugInfo: PartialUpdateExternalDrugInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(externalDrugInfo);
    return this.http
      .patch<RestExternalDrugInfo>(`${this.resourceUrl}/${this.getExternalDrugInfoIdentifier(externalDrugInfo)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExternalDrugInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExternalDrugInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExternalDrugInfoIdentifier(externalDrugInfo: Pick<IExternalDrugInfo, 'id'>): number {
    return externalDrugInfo.id;
  }

  compareExternalDrugInfo(o1: Pick<IExternalDrugInfo, 'id'> | null, o2: Pick<IExternalDrugInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getExternalDrugInfoIdentifier(o1) === this.getExternalDrugInfoIdentifier(o2) : o1 === o2;
  }

  addExternalDrugInfoToCollectionIfMissing<Type extends Pick<IExternalDrugInfo, 'id'>>(
    externalDrugInfoCollection: Type[],
    ...externalDrugInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const externalDrugInfos: Type[] = externalDrugInfosToCheck.filter(isPresent);
    if (externalDrugInfos.length > 0) {
      const externalDrugInfoCollectionIdentifiers = externalDrugInfoCollection.map(externalDrugInfoItem =>
        this.getExternalDrugInfoIdentifier(externalDrugInfoItem),
      );
      const externalDrugInfosToAdd = externalDrugInfos.filter(externalDrugInfoItem => {
        const externalDrugInfoIdentifier = this.getExternalDrugInfoIdentifier(externalDrugInfoItem);
        if (externalDrugInfoCollectionIdentifiers.includes(externalDrugInfoIdentifier)) {
          return false;
        }
        externalDrugInfoCollectionIdentifiers.push(externalDrugInfoIdentifier);
        return true;
      });
      return [...externalDrugInfosToAdd, ...externalDrugInfoCollection];
    }
    return externalDrugInfoCollection;
  }

  protected convertDateFromClient<T extends IExternalDrugInfo | NewExternalDrugInfo | PartialUpdateExternalDrugInfo>(
    externalDrugInfo: T,
  ): RestOf<T> {
    return {
      ...externalDrugInfo,
      lastUpdated: externalDrugInfo.lastUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExternalDrugInfo: RestExternalDrugInfo): IExternalDrugInfo {
    return {
      ...restExternalDrugInfo,
      lastUpdated: restExternalDrugInfo.lastUpdated ? dayjs(restExternalDrugInfo.lastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExternalDrugInfo>): HttpResponse<IExternalDrugInfo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExternalDrugInfo[]>): HttpResponse<IExternalDrugInfo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
