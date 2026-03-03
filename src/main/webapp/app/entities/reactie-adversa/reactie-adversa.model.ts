import dayjs from 'dayjs/esm';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { IPacient } from 'app/entities/pacient/pacient.model';
import { SeveritateReactie } from 'app/entities/enumerations/severitate-reactie.model';

export interface IReactieAdversa {
  id: number;
  dataRaportare?: dayjs.Dayjs | null;
  severitate?: keyof typeof SeveritateReactie | null;
  descriere?: string | null;
  evolutie?: string | null;
  raportatDe?: string | null;
  medicament?: Pick<IMedicament, 'id' | 'denumire'> | null;
  pacient?: Pick<IPacient, 'id'> | null;
}

export type NewReactieAdversa = Omit<IReactieAdversa, 'id'> & { id: null };
