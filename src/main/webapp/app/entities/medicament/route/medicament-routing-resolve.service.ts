import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicament } from '../medicament.model';
import { MedicamentService } from '../service/medicament.service';

const medicamentResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedicament> => {
  const id = route.params.id;
  if (id) {
    return inject(MedicamentService)
      .find(id)
      .pipe(
        mergeMap((medicament: HttpResponse<IMedicament>) => {
          if (medicament.body) {
            return of(medicament.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default medicamentResolve;
