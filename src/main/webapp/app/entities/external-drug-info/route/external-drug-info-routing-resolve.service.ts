import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExternalDrugInfo } from '../external-drug-info.model';
import { ExternalDrugInfoService } from '../service/external-drug-info.service';

const externalDrugInfoResolve = (route: ActivatedRouteSnapshot): Observable<null | IExternalDrugInfo> => {
  const id = route.params.id;
  if (id) {
    return inject(ExternalDrugInfoService)
      .find(id)
      .pipe(
        mergeMap((externalDrugInfo: HttpResponse<IExternalDrugInfo>) => {
          if (externalDrugInfo.body) {
            return of(externalDrugInfo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default externalDrugInfoResolve;
