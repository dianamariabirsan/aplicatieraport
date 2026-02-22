import dayjs from 'dayjs/esm';

import { IAdministrare, NewAdministrare } from './administrare.model';

export const sampleWithRequiredData: IAdministrare = {
  id: 9713,
  dataAdministrare: dayjs('2026-02-20T18:09'),
  tipTratament: 'and incidentally incidentally',
};

export const sampleWithPartialData: IAdministrare = {
  id: 1603,
  dataAdministrare: dayjs('2026-02-20T21:17'),
  tipTratament: 'irritably surprise how',
};

export const sampleWithFullData: IAdministrare = {
  id: 25805,
  dataAdministrare: dayjs('2026-02-20T09:11'),
  tipTratament: 'for scaffold oof',
  doza: 7927.98,
  unitate: 'that willing ugh',
  modAdministrare: 'chime partial drat',
  observatii: 'whose after atop',
};

export const sampleWithNewData: NewAdministrare = {
  dataAdministrare: dayjs('2026-02-20T00:05'),
  tipTratament: 'futon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
