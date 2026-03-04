import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';
import { ASC } from 'app/config/navigation.constants';
import PacientResolve from './route/pacient-routing-resolve.service';

const pacientRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pacient.component').then(m => m.PacientComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pacient-detail.component').then(m => m.PacientDetailComponent),
    resolve: {
      pacient: PacientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pacient-update.component').then(m => m.PacientUpdateComponent),
    resolve: {
      pacient: PacientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pacient-update.component').then(m => m.PacientUpdateComponent),
    resolve: {
      pacient: PacientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pacientRoute;
