import { IStudiiLiteratura, NewStudiiLiteratura } from './studii-literatura.model';

export const sampleWithRequiredData: IStudiiLiteratura = {
  id: 28015,
  titlu: 'esteemed garrote versus',
};

export const sampleWithPartialData: IStudiiLiteratura = {
  id: 2174,
  titlu: 'ew uh-huh',
  anul: 7242,
  tipStudiu: 'hm likewise like',
  substanta: 'early helplessly as',
  concluzie: 'needily trial for',
  link: 'duh upliftingly forenenst',
};

export const sampleWithFullData: IStudiiLiteratura = {
  id: 20176,
  titlu: 'quarrel coincide',
  autori: 'amongst sadly gosh',
  anul: 12969,
  tipStudiu: 'provided',
  substanta: 'grade fashion',
  concluzie: 'quietly hm',
  link: 'round innocent gadzooks',
};

export const sampleWithNewData: NewStudiiLiteratura = {
  titlu: 'evenly worthy glittering',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
