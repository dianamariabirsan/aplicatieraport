import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExternalDrugInfoResolve from './route/external-drug-info-routing-resolve.service';

const externalDrugInfoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/external-drug-info.component').then(m => m.ExternalDrugInfoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/external-drug-info-detail.component').then(m => m.ExternalDrugInfoDetailComponent),
    resolve: {
      externalDrugInfo: ExternalDrugInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/external-drug-info-update.component').then(m => m.ExternalDrugInfoUpdateComponent),
    resolve: {
      externalDrugInfo: ExternalDrugInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/external-drug-info-update.component').then(m => m.ExternalDrugInfoUpdateComponent),
    resolve: {
      externalDrugInfo: ExternalDrugInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default externalDrugInfoRoute;
