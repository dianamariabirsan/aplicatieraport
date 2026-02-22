import dayjs from 'dayjs/esm';

import { IExternalDrugInfo, NewExternalDrugInfo } from './external-drug-info.model';

export const sampleWithRequiredData: IExternalDrugInfo = {
  id: 20940,
  source: 'drat brr but',
};

export const sampleWithPartialData: IExternalDrugInfo = {
  id: 6030,
  source: 'proper ack blaspheme',
  sourceUrl: 'gadzooks',
};

export const sampleWithFullData: IExternalDrugInfo = {
  id: 29857,
  source: 'at',
  productSummary: 'spirit',
  lastUpdated: dayjs('2026-02-20T00:44'),
  sourceUrl: 'resort',
};

export const sampleWithNewData: NewExternalDrugInfo = {
  source: 'basket',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
