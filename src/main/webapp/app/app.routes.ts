import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component'),
    title: 'home.title',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  {
    path: 'pacient/reactii',
    loadComponent: () => import('./pacient/reactii/pacient-reactii.component').then(m => m.PacientReactiiComponent),
    data: {
      authorities: [Authority.ADMIN, Authority.PACIENT],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'farmacist/reactii',
    loadComponent: () => import('./farmacist/reactii/farmacist-reactii.component').then(m => m.FarmacistReactiiComponent),
    data: {
      authorities: [Authority.ADMIN, Authority.FARMACIST],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'medic/inbox',
    loadComponent: () => import('./medic/inbox/medic-inbox.component').then(m => m.MedicInboxComponent),
    data: {
      authorities: [Authority.ADMIN, Authority.MEDIC],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'analytics',
    loadChildren: () => import('./analytics/analytics.route'),
  },
  ...errorRoute,
];

export default routes;
