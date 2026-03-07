import dayjs from 'dayjs/esm';

import { IMedicament } from 'app/entities/medicament/medicament.model';

export interface IExternalDrugInfo {
  id: number;
  source?: string | null;
  productSummary?: string | null;
  lastUpdated?: dayjs.Dayjs | null;
  sourceUrl?: string | null;
  medicament?: Pick<IMedicament, 'id' | 'denumire'> | null;
}

export type NewExternalDrugInfo = Omit<IExternalDrugInfo, 'id'> & { id: null };
