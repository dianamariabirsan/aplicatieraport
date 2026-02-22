import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecisionLog, NewDecisionLog } from '../decision-log.model';

export type PartialUpdateDecisionLog = Partial<IDecisionLog> & Pick<IDecisionLog, 'id'>;

type RestOf<T extends IDecisionLog | NewDecisionLog> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestDecisionLog = RestOf<IDecisionLog>;

export type NewRestDecisionLog = RestOf<NewDecisionLog>;

export type PartialUpdateRestDecisionLog = RestOf<PartialUpdateDecisionLog>;

export type EntityResponseType = HttpResponse<IDecisionLog>;
export type EntityArrayResponseType = HttpResponse<IDecisionLog[]>;

@Injectable({ providedIn: 'root' })
export class DecisionLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/decision-logs');

  create(decisionLog: NewDecisionLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisionLog);
    return this.http
      .post<RestDecisionLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(decisionLog: IDecisionLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisionLog);
    return this.http
      .put<RestDecisionLog>(`${this.resourceUrl}/${this.getDecisionLogIdentifier(decisionLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(decisionLog: PartialUpdateDecisionLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decisionLog);
    return this.http
      .patch<RestDecisionLog>(`${this.resourceUrl}/${this.getDecisionLogIdentifier(decisionLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDecisionLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDecisionLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDecisionLogIdentifier(decisionLog: Pick<IDecisionLog, 'id'>): number {
    return decisionLog.id;
  }

  compareDecisionLog(o1: Pick<IDecisionLog, 'id'> | null, o2: Pick<IDecisionLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getDecisionLogIdentifier(o1) === this.getDecisionLogIdentifier(o2) : o1 === o2;
  }

  addDecisionLogToCollectionIfMissing<Type extends Pick<IDecisionLog, 'id'>>(
    decisionLogCollection: Type[],
    ...decisionLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const decisionLogs: Type[] = decisionLogsToCheck.filter(isPresent);
    if (decisionLogs.length > 0) {
      const decisionLogCollectionIdentifiers = decisionLogCollection.map(decisionLogItem => this.getDecisionLogIdentifier(decisionLogItem));
      const decisionLogsToAdd = decisionLogs.filter(decisionLogItem => {
        const decisionLogIdentifier = this.getDecisionLogIdentifier(decisionLogItem);
        if (decisionLogCollectionIdentifiers.includes(decisionLogIdentifier)) {
          return false;
        }
        decisionLogCollectionIdentifiers.push(decisionLogIdentifier);
        return true;
      });
      return [...decisionLogsToAdd, ...decisionLogCollection];
    }
    return decisionLogCollection;
  }

  protected convertDateFromClient<T extends IDecisionLog | NewDecisionLog | PartialUpdateDecisionLog>(decisionLog: T): RestOf<T> {
    return {
      ...decisionLog,
      timestamp: decisionLog.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDecisionLog: RestDecisionLog): IDecisionLog {
    return {
      ...restDecisionLog,
      timestamp: restDecisionLog.timestamp ? dayjs(restDecisionLog.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDecisionLog>): HttpResponse<IDecisionLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDecisionLog[]>): HttpResponse<IDecisionLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
