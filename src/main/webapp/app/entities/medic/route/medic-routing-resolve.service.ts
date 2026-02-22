import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedic } from '../medic.model';
import { MedicService } from '../service/medic.service';

const medicResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedic> => {
  const id = route.params.id;
  if (id) {
    return inject(MedicService)
      .find(id)
      .pipe(
        mergeMap((medic: HttpResponse<IMedic>) => {
          if (medic.body) {
            return of(medic.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default medicResolve;
