import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DecisionLogResolve from './route/decision-log-routing-resolve.service';

const decisionLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/decision-log.component').then(m => m.DecisionLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/decision-log-detail.component').then(m => m.DecisionLogDetailComponent),
    resolve: {
      decisionLog: DecisionLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/decision-log-update.component').then(m => m.DecisionLogUpdateComponent),
    resolve: {
      decisionLog: DecisionLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/decision-log-update.component').then(m => m.DecisionLogUpdateComponent),
    resolve: {
      decisionLog: DecisionLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default decisionLogRoute;
