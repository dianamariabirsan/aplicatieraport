import dayjs from 'dayjs/esm';
import { IAlocareTratament } from 'app/entities/alocare-tratament/alocare-tratament.model';

export interface IFeedback {
  id: number;
  scor?: number | null;
  comentariu?: string | null;
  dataFeedback?: dayjs.Dayjs | null;
  alocare?: Pick<IAlocareTratament, 'id'> | null;
}

export type NewFeedback = Omit<IFeedback, 'id'> & { id: null };
