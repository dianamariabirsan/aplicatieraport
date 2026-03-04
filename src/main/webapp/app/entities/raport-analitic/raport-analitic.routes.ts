import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from 'app/config/authority.constants';
import RaportAnaliticResolve from './route/raport-analitic-routing-resolve.service';

const raportAnaliticRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/raport-analitic.component').then(m => m.RaportAnaliticComponent),
    data: {
      defaultSort: `id,${ASC}`,
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/raport-analitic-detail.component').then(m => m.RaportAnaliticDetailComponent),
    resolve: {
      raportAnalitic: RaportAnaliticResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/raport-analitic-update.component').then(m => m.RaportAnaliticUpdateComponent),
    resolve: {
      raportAnalitic: RaportAnaliticResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/raport-analitic-update.component').then(m => m.RaportAnaliticUpdateComponent),
    resolve: {
      raportAnalitic: RaportAnaliticResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default raportAnaliticRoute;
