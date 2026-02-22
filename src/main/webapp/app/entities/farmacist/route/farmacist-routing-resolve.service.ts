import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFarmacist } from '../farmacist.model';
import { FarmacistService } from '../service/farmacist.service';

const farmacistResolve = (route: ActivatedRouteSnapshot): Observable<null | IFarmacist> => {
  const id = route.params.id;
  if (id) {
    return inject(FarmacistService)
      .find(id)
      .pipe(
        mergeMap((farmacist: HttpResponse<IFarmacist>) => {
          if (farmacist.body) {
            return of(farmacist.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default farmacistResolve;
