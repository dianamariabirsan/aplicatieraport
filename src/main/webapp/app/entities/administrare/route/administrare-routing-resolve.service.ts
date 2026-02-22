import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdministrare } from '../administrare.model';
import { AdministrareService } from '../service/administrare.service';

const administrareResolve = (route: ActivatedRouteSnapshot): Observable<null | IAdministrare> => {
  const id = route.params.id;
  if (id) {
    return inject(AdministrareService)
      .find(id)
      .pipe(
        mergeMap((administrare: HttpResponse<IAdministrare>) => {
          if (administrare.body) {
            return of(administrare.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default administrareResolve;
