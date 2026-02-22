import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRaportAnalitic } from '../raport-analitic.model';
import { RaportAnaliticService } from '../service/raport-analitic.service';

const raportAnaliticResolve = (route: ActivatedRouteSnapshot): Observable<null | IRaportAnalitic> => {
  const id = route.params.id;
  if (id) {
    return inject(RaportAnaliticService)
      .find(id)
      .pipe(
        mergeMap((raportAnalitic: HttpResponse<IRaportAnalitic>) => {
          if (raportAnalitic.body) {
            return of(raportAnalitic.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default raportAnaliticResolve;
