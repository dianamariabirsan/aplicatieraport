import dayjs from 'dayjs/esm';

import { IRaportAnalitic, NewRaportAnalitic } from './raport-analitic.model';

export const sampleWithRequiredData: IRaportAnalitic = {
  id: 26837,
};

export const sampleWithPartialData: IRaportAnalitic = {
  id: 21596,
  perioadaEnd: dayjs('2026-02-20T21:37'),
  observatii: 'astonishing nectarine whoever',
};

export const sampleWithFullData: IRaportAnalitic = {
  id: 16294,
  perioadaStart: dayjs('2026-02-20T03:56'),
  perioadaEnd: dayjs('2026-02-20T15:54'),
  eficientaMedie: 30159.71,
  rataReactiiAdverse: 10511.08,
  observatii: 'as advertisement',
  concluzii: 'jittery frantically whereas',
};

export const sampleWithNewData: NewRaportAnalitic = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
