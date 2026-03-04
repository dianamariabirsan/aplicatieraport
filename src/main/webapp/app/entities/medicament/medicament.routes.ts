import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';
import { ASC } from 'app/config/navigation.constants';
import MedicamentResolve from './route/medicament-routing-resolve.service';

const medicamentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medicament.component').then(m => m.MedicamentComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/medicament-detail.component').then(m => m.MedicamentDetailComponent),
    resolve: {
      medicament: MedicamentResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/medicament-update.component').then(m => m.MedicamentUpdateComponent),
    resolve: {
      medicament: MedicamentResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/medicament-update.component').then(m => m.MedicamentUpdateComponent),
    resolve: {
      medicament: MedicamentResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicamentRoute;
