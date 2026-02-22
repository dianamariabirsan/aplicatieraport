import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReactieAdversaResolve from './route/reactie-adversa-routing-resolve.service';

const reactieAdversaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reactie-adversa.component').then(m => m.ReactieAdversaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reactie-adversa-detail.component').then(m => m.ReactieAdversaDetailComponent),
    resolve: {
      reactieAdversa: ReactieAdversaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reactie-adversa-update.component').then(m => m.ReactieAdversaUpdateComponent),
    resolve: {
      reactieAdversa: ReactieAdversaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reactie-adversa-update.component').then(m => m.ReactieAdversaUpdateComponent),
    resolve: {
      reactieAdversa: ReactieAdversaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reactieAdversaRoute;
