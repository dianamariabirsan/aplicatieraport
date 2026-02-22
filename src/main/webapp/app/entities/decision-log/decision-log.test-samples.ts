import dayjs from 'dayjs/esm';

import { IDecisionLog, NewDecisionLog } from './decision-log.model';

export const sampleWithRequiredData: IDecisionLog = {
  id: 2741,
  timestamp: dayjs('2026-02-20T04:43'),
  actorType: 'SISTEM_AI',
};

export const sampleWithPartialData: IDecisionLog = {
  id: 23389,
  timestamp: dayjs('2026-02-20T12:23'),
  actorType: 'VALIDATOR_EXTERN',
  modelScore: 3632.59,
};

export const sampleWithFullData: IDecisionLog = {
  id: 20390,
  timestamp: dayjs('2026-02-20T13:21'),
  actorType: 'MEDIC',
  recomandare: 'gee ick',
  modelScore: 26378.57,
  reguliTriggered: 'hm',
  externalChecks: 'hydrolyse ruddy',
};

export const sampleWithNewData: NewDecisionLog = {
  timestamp: dayjs('2026-02-20T14:11'),
  actorType: 'MEDIC',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
