import dayjs from 'dayjs/esm';

import { IMonitorizare, NewMonitorizare } from './monitorizare.model';

export const sampleWithRequiredData: IMonitorizare = {
  id: 18128,
  dataInstant: dayjs('2026-02-20T02:39'),
};

export const sampleWithPartialData: IMonitorizare = {
  id: 16819,
  dataInstant: dayjs('2026-02-20T04:57'),
  tensiuneSist: 14181.27,
  tensiuneDiast: 2991.33,
  puls: 570,
  glicemie: 4986.44,
  scorEficacitate: 2297.05,
};

export const sampleWithFullData: IMonitorizare = {
  id: 20604,
  dataInstant: dayjs('2026-02-20T17:58'),
  tensiuneSist: 20241.39,
  tensiuneDiast: 9279.95,
  puls: 25725,
  glicemie: 8920.1,
  scorEficacitate: 1902.56,
  comentarii: 'orchestrate massage',
};

export const sampleWithNewData: NewMonitorizare = {
  dataInstant: dayjs('2026-02-20T16:59'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
