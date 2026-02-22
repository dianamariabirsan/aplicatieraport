import dayjs from 'dayjs/esm';

import { IReactieAdversa, NewReactieAdversa } from './reactie-adversa.model';

export const sampleWithRequiredData: IReactieAdversa = {
  id: 30792,
  dataRaportare: dayjs('2026-02-20T05:35'),
  descriere: 'amid yuck',
};

export const sampleWithPartialData: IReactieAdversa = {
  id: 32251,
  dataRaportare: dayjs('2026-02-20T04:04'),
  severitate: 'boldly since opera',
  descriere: 'spherical anaesthetise after',
  raportatDe: 'blah',
};

export const sampleWithFullData: IReactieAdversa = {
  id: 13673,
  dataRaportare: dayjs('2026-02-20T03:47'),
  severitate: 'reassemble oof unwelcome',
  descriere: 'table clueless',
  evolutie: 'procrastinate or cinema',
  raportatDe: 'second-hand expansion duh',
};

export const sampleWithNewData: NewReactieAdversa = {
  dataRaportare: dayjs('2026-02-20T05:44'),
  descriere: 'freely home velvety',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
