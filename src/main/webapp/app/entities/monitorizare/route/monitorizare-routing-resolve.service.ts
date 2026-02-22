import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonitorizare } from '../monitorizare.model';
import { MonitorizareService } from '../service/monitorizare.service';

const monitorizareResolve = (route: ActivatedRouteSnapshot): Observable<null | IMonitorizare> => {
  const id = route.params.id;
  if (id) {
    return inject(MonitorizareService)
      .find(id)
      .pipe(
        mergeMap((monitorizare: HttpResponse<IMonitorizare>) => {
          if (monitorizare.body) {
            return of(monitorizare.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default monitorizareResolve;
