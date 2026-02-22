import { IPacient, NewPacient } from './pacient.model';

export const sampleWithRequiredData: IPacient = {
  id: 9135,
  nume: 'corrupt ha',
  prenume: 'giggle gigantic',
  sex: 'major excess',
  varsta: 17433,
};

export const sampleWithPartialData: IPacient = {
  id: 17640,
  nume: 'circa character',
  prenume: 'between repeat',
  sex: 'charming frilly but',
  varsta: 17143,
  inaltime: 8840.64,
  circumferintaAbdominala: 13333.97,
  cnp: 'besidesXXXXXX',
  email: 'Ileana.Niculescu@yahoo.com',
  telefon: 'propound gadzooks hundred',
};

export const sampleWithFullData: IPacient = {
  id: 5710,
  nume: 'although prestigious',
  prenume: 'publicity qua knuckle',
  sex: 'across',
  varsta: 1339,
  greutate: 826.11,
  inaltime: 27252.96,
  circumferintaAbdominala: 2750.8,
  cnp: 'wherever regu',
  comorbiditati: 'pish fencing meanwhile',
  gradSedentarism: 'aha forgather',
  istoricTratament: 'before whoa',
  toleranta: 'like connect reservation',
  email: 'Francisc.Dinu@hotmail.com',
  telefon: 'greatly times embalm',
};

export const sampleWithNewData: NewPacient = {
  nume: 'gripper breakable draft',
  prenume: 'acidly excepting',
  sex: 'until antelope',
  varsta: 23536,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
