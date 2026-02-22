import dayjs from 'dayjs/esm';

import { IAlocareTratament, NewAlocareTratament } from './alocare-tratament.model';

export const sampleWithRequiredData: IAlocareTratament = {
  id: 22250,
  dataDecizie: dayjs('2026-02-20T01:58'),
  tratamentPropus: 'radiant during muffled',
};

export const sampleWithPartialData: IAlocareTratament = {
  id: 12095,
  dataDecizie: dayjs('2026-02-20T01:41'),
  tratamentPropus: 'of',
  motivDecizie: 'which sorrowful',
  decizieValidata: true,
};

export const sampleWithFullData: IAlocareTratament = {
  id: 2357,
  dataDecizie: dayjs('2026-02-20T13:43'),
  tratamentPropus: 'yum',
  motivDecizie: 'generally',
  scorDecizie: 24292.09,
  decizieValidata: false,
};

export const sampleWithNewData: NewAlocareTratament = {
  dataDecizie: dayjs('2026-02-20T09:05'),
  tratamentPropus: 'jot far anenst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
