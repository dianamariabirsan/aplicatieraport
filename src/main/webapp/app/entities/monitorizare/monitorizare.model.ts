import dayjs from 'dayjs/esm';
import { IPacient } from 'app/entities/pacient/pacient.model';

export interface IMonitorizare {
  id: number;
  dataInstant?: dayjs.Dayjs | null;
  tensiuneSist?: number | null;
  tensiuneDiast?: number | null;
  puls?: number | null;
  glicemie?: number | null;
  scorEficacitate?: number | null;
  comentarii?: string | null;
  pacient?: Pick<IPacient, 'id' | 'nume' | 'prenume'> | null;
}

export type NewMonitorizare = Omit<IMonitorizare, 'id'> & { id: null };
