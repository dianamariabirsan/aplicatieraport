import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecisionLog } from '../decision-log.model';

type RestOf<T extends IDecisionLog> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestDecisionLog = RestOf<IDecisionLog>;

export type EntityResponseType = HttpResponse<IDecisionLog>;
export type EntityArrayResponseType = HttpResponse<IDecisionLog[]>;

@Injectable({ providedIn: 'root' })
export class DecisionLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/decision-logs');

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

  queryByAlocareId(alocareId: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestDecisionLog[]>(`${this.resourceUrl}/by-alocare/${alocareId}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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
