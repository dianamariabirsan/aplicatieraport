import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AlocareTratamentResolve from './route/alocare-tratament-routing-resolve.service';

const alocareTratamentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alocare-tratament.component').then(m => m.AlocareTratamentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alocare-tratament-detail.component').then(m => m.AlocareTratamentDetailComponent),
    resolve: {
      alocareTratament: AlocareTratamentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alocare-tratament-update.component').then(m => m.AlocareTratamentUpdateComponent),
    resolve: {
      alocareTratament: AlocareTratamentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alocare-tratament-update.component').then(m => m.AlocareTratamentUpdateComponent),
    resolve: {
      alocareTratament: AlocareTratamentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alocareTratamentRoute;
