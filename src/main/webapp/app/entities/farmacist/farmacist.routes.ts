import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';
import { ASC } from 'app/config/navigation.constants';
import FarmacistResolve from './route/farmacist-routing-resolve.service';

const farmacistRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/farmacist.component').then(m => m.FarmacistComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/farmacist-detail.component').then(m => m.FarmacistDetailComponent),
    resolve: {
      farmacist: FarmacistResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/farmacist-update.component').then(m => m.FarmacistUpdateComponent),
    resolve: {
      farmacist: FarmacistResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/farmacist-update.component').then(m => m.FarmacistUpdateComponent),
    resolve: {
      farmacist: FarmacistResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default farmacistRoute;
