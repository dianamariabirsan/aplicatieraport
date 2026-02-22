import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlocareTratament } from '../alocare-tratament.model';
import { AlocareTratamentService } from '../service/alocare-tratament.service';

const alocareTratamentResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlocareTratament> => {
  const id = route.params.id;
  if (id) {
    return inject(AlocareTratamentService)
      .find(id)
      .pipe(
        mergeMap((alocareTratament: HttpResponse<IAlocareTratament>) => {
          if (alocareTratament.body) {
            return of(alocareTratament.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default alocareTratamentResolve;
