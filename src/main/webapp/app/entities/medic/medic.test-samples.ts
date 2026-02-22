import { IMedic, NewMedic } from './medic.model';

export const sampleWithRequiredData: IMedic = {
  id: 13071,
  nume: 'importance sick under',
  prenume: 'though',
  specializare: 'since tensely below',
  email: 'Vincentiu.Ivan@hotmail.com',
};

export const sampleWithPartialData: IMedic = {
  id: 12425,
  nume: 'incidentally sit or',
  prenume: 'expatiate provided sedately',
  specializare: 'pfft athwart gruesome',
  email: 'Agnos_Serban76@yahoo.com',
  telefon: 'crackle',
  cabinet: 'rag sidetrack instead',
};

export const sampleWithFullData: IMedic = {
  id: 8109,
  nume: 'charlatan',
  prenume: 'odd elegantly fedora',
  specializare: 'brr when artistic',
  email: 'Olimpiu.Oancea2@hotmail.com',
  telefon: 'optimistically',
  cabinet: 'back untrue',
};

export const sampleWithNewData: NewMedic = {
  nume: 'certainly ew',
  prenume: 'incidentally putrefy',
  specializare: 'adaptation likewise lamp',
  email: 'Marinela62@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
