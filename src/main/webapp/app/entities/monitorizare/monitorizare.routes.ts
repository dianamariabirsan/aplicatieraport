import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';
import { ASC } from 'app/config/navigation.constants';
import MonitorizareResolve from './route/monitorizare-routing-resolve.service';

const monitorizareRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/monitorizare.component').then(m => m.MonitorizareComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/monitorizare-detail.component').then(m => m.MonitorizareDetailComponent),
    resolve: {
      monitorizare: MonitorizareResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/monitorizare-update.component').then(m => m.MonitorizareUpdateComponent),
    resolve: {
      monitorizare: MonitorizareResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/monitorizare-update.component').then(m => m.MonitorizareUpdateComponent),
    resolve: {
      monitorizare: MonitorizareResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default monitorizareRoute;
