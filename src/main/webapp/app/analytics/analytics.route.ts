import { Routes } from '@angular/router';
import { AnalyticsDashboardComponent } from './analytics-dashboard.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';

const routes: Routes = [
  {
    path: '',
    component: AnalyticsDashboardComponent,
    title: 'Analytics',
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default routes;
