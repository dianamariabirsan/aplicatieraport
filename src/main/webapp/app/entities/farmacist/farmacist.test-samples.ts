import { IFarmacist, NewFarmacist } from './farmacist.model';

export const sampleWithRequiredData: IFarmacist = {
  id: 16500,
  nume: 'humiliating',
  prenume: 'or',
  farmacie: 'wordy',
  email: 'Alexe_Rotaru@gmail.com',
};

export const sampleWithPartialData: IFarmacist = {
  id: 32056,
  nume: 'parallel hm eek',
  prenume: 'for',
  farmacie: 'blah ha',
  email: 'Robertina_Chis37@gmail.com',
  telefon: 'aha quarrelsomely',
};

export const sampleWithFullData: IFarmacist = {
  id: 26286,
  nume: 'mealy amused',
  prenume: 'even wherever',
  farmacie: 'conservation',
  email: 'Nichifor.Iliescu@hotmail.com',
  telefon: 'lest furthermore',
};

export const sampleWithNewData: NewFarmacist = {
  nume: 'shameful',
  prenume: 'that tomorrow sans',
  farmacie: 'focalise beneath',
  email: 'Doriana.Grigoras@hotmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
