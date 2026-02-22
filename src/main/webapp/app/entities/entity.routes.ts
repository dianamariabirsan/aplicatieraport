import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'healthappApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'medic',
    data: { pageTitle: 'healthappApp.medic.home.title' },
    loadChildren: () => import('./medic/medic.routes'),
  },
  {
    path: 'farmacist',
    data: { pageTitle: 'healthappApp.farmacist.home.title' },
    loadChildren: () => import('./farmacist/farmacist.routes'),
  },
  {
    path: 'pacient',
    data: { pageTitle: 'healthappApp.pacient.home.title' },
    loadChildren: () => import('./pacient/pacient.routes'),
  },
  {
    path: 'medicament',
    data: { pageTitle: 'healthappApp.medicament.home.title' },
    loadChildren: () => import('./medicament/medicament.routes'),
  },
  {
    path: 'external-drug-info',
    data: { pageTitle: 'healthappApp.externalDrugInfo.home.title' },
    loadChildren: () => import('./external-drug-info/external-drug-info.routes'),
  },
  {
    path: 'studii-literatura',
    data: { pageTitle: 'healthappApp.studiiLiteratura.home.title' },
    loadChildren: () => import('./studii-literatura/studii-literatura.routes'),
  },
  {
    path: 'alocare-tratament',
    data: { pageTitle: 'healthappApp.alocareTratament.home.title' },
    loadChildren: () => import('./alocare-tratament/alocare-tratament.routes'),
  },
  {
    path: 'administrare',
    data: { pageTitle: 'healthappApp.administrare.home.title' },
    loadChildren: () => import('./administrare/administrare.routes'),
  },
  {
    path: 'reactie-adversa',
    data: { pageTitle: 'healthappApp.reactieAdversa.home.title' },
    loadChildren: () => import('./reactie-adversa/reactie-adversa.routes'),
  },
  {
    path: 'monitorizare',
    data: { pageTitle: 'healthappApp.monitorizare.home.title' },
    loadChildren: () => import('./monitorizare/monitorizare.routes'),
  },
  {
    path: 'decision-log',
    data: { pageTitle: 'healthappApp.decisionLog.home.title' },
    loadChildren: () => import('./decision-log/decision-log.routes'),
  },
  {
    path: 'feedback',
    data: { pageTitle: 'healthappApp.feedback.home.title' },
    loadChildren: () => import('./feedback/feedback.routes'),
  },
  {
    path: 'raport-analitic',
    data: { pageTitle: 'healthappApp.raportAnalitic.home.title' },
    loadChildren: () => import('./raport-analitic/raport-analitic.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
