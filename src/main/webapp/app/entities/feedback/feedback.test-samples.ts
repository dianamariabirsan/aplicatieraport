import dayjs from 'dayjs/esm';

import { IFeedback, NewFeedback } from './feedback.model';

export const sampleWithRequiredData: IFeedback = {
  id: 16297,
  scor: 3,
};

export const sampleWithPartialData: IFeedback = {
  id: 30079,
  scor: 7,
};

export const sampleWithFullData: IFeedback = {
  id: 18312,
  scor: 7,
  comentariu: 'fiercely',
  dataFeedback: dayjs('2026-02-20T09:13'),
};

export const sampleWithNewData: NewFeedback = {
  scor: 3,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
