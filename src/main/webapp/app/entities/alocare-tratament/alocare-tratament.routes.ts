import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from 'app/config/authority.constants';
import AlocareTratamentResolve from './route/alocare-tratament-routing-resolve.service';

const alocareTratamentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alocare-tratament.component').then(m => m.AlocareTratamentComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alocare-tratament-detail.component').then(m => m.AlocareTratamentDetailComponent),
    resolve: {
      alocareTratament: AlocareTratamentResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alocare-tratament-update.component').then(m => m.AlocareTratamentUpdateComponent),
    resolve: {
      alocareTratament: AlocareTratamentResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alocare-tratament-update.component').then(m => m.AlocareTratamentUpdateComponent),
    resolve: {
      alocareTratament: AlocareTratamentResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alocareTratamentRoute;
