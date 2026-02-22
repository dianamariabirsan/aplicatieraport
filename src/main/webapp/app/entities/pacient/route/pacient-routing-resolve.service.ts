import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPacient } from '../pacient.model';
import { PacientService } from '../service/pacient.service';

const pacientResolve = (route: ActivatedRouteSnapshot): Observable<null | IPacient> => {
  const id = route.params.id;
  if (id) {
    return inject(PacientService)
      .find(id)
      .pipe(
        mergeMap((pacient: HttpResponse<IPacient>) => {
          if (pacient.body) {
            return of(pacient.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default pacientResolve;
