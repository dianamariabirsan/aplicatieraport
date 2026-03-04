import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';
import { ASC } from 'app/config/navigation.constants';
import MedicResolve from './route/medic-routing-resolve.service';

const medicRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medic.component').then(m => m.MedicComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/medic-detail.component').then(m => m.MedicDetailComponent),
    resolve: {
      medic: MedicResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/medic-update.component').then(m => m.MedicUpdateComponent),
    resolve: {
      medic: MedicResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/medic-update.component').then(m => m.MedicUpdateComponent),
    resolve: {
      medic: MedicResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicRoute;
