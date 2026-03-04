import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from 'app/config/authority.constants';
import AdministrareResolve from './route/administrare-routing-resolve.service';

const administrareRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/administrare.component').then(m => m.AdministrareComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.FARMACIST, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/administrare-detail.component').then(m => m.AdministrareDetailComponent),
    resolve: {
      administrare: AdministrareResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.FARMACIST, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/administrare-update.component').then(m => m.AdministrareUpdateComponent),
    resolve: {
      administrare: AdministrareResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/administrare-update.component').then(m => m.AdministrareUpdateComponent),
    resolve: {
      administrare: AdministrareResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default administrareRoute;
