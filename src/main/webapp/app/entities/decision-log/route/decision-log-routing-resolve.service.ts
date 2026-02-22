import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDecisionLog } from '../decision-log.model';
import { DecisionLogService } from '../service/decision-log.service';

const decisionLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IDecisionLog> => {
  const id = route.params.id;
  if (id) {
    return inject(DecisionLogService)
      .find(id)
      .pipe(
        mergeMap((decisionLog: HttpResponse<IDecisionLog>) => {
          if (decisionLog.body) {
            return of(decisionLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default decisionLogResolve;
