import dayjs from 'dayjs/esm';

export interface IExternalDrugInfo {
  id: number;
  source?: string | null;
  productSummary?: string | null;
  lastUpdated?: dayjs.Dayjs | null;
  sourceUrl?: string | null;
}

export type NewExternalDrugInfo = Omit<IExternalDrugInfo, 'id'> & { id: null };
