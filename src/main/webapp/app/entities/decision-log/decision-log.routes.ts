import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';
import { ASC } from 'app/config/navigation.constants';
import DecisionLogResolve from './route/decision-log-routing-resolve.service';

const decisionLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/decision-log.component').then(m => m.DecisionLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/decision-log-detail.component').then(m => m.DecisionLogDetailComponent),
    resolve: {
      decisionLog: DecisionLogResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default decisionLogRoute;
