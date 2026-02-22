import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReactieAdversa } from '../reactie-adversa.model';
import { ReactieAdversaService } from '../service/reactie-adversa.service';

const reactieAdversaResolve = (route: ActivatedRouteSnapshot): Observable<null | IReactieAdversa> => {
  const id = route.params.id;
  if (id) {
    return inject(ReactieAdversaService)
      .find(id)
      .pipe(
        mergeMap((reactieAdversa: HttpResponse<IReactieAdversa>) => {
          if (reactieAdversa.body) {
            return of(reactieAdversa.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reactieAdversaResolve;
