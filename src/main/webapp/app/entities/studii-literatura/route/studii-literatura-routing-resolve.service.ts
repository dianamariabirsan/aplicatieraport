import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudiiLiteratura } from '../studii-literatura.model';
import { StudiiLiteraturaService } from '../service/studii-literatura.service';

const studiiLiteraturaResolve = (route: ActivatedRouteSnapshot): Observable<null | IStudiiLiteratura> => {
  const id = route.params.id;
  if (id) {
    return inject(StudiiLiteraturaService)
      .find(id)
      .pipe(
        mergeMap((studiiLiteratura: HttpResponse<IStudiiLiteratura>) => {
          if (studiiLiteratura.body) {
            return of(studiiLiteratura.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default studiiLiteraturaResolve;
