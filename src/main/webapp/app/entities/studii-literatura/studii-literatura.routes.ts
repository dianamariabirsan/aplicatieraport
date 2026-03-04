import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';
import { ASC } from 'app/config/navigation.constants';
import StudiiLiteraturaResolve from './route/studii-literatura-routing-resolve.service';

const studiiLiteraturaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/studii-literatura.component').then(m => m.StudiiLiteraturaComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/studii-literatura-detail.component').then(m => m.StudiiLiteraturaDetailComponent),
    resolve: {
      studiiLiteratura: StudiiLiteraturaResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/studii-literatura-update.component').then(m => m.StudiiLiteraturaUpdateComponent),
    resolve: {
      studiiLiteratura: StudiiLiteraturaResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/studii-literatura-update.component').then(m => m.StudiiLiteraturaUpdateComponent),
    resolve: {
      studiiLiteratura: StudiiLiteraturaResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default studiiLiteraturaRoute;
