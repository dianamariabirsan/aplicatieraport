import dayjs from 'dayjs/esm';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { IMedic } from 'app/entities/medic/medic.model';

export interface IRaportAnalitic {
  id: number;
  perioadaStart?: dayjs.Dayjs | null;
  perioadaEnd?: dayjs.Dayjs | null;
  eficientaMedie?: number | null;
  rataReactiiAdverse?: number | null;
  observatii?: string | null;
  concluzii?: string | null;
  medicament?: Pick<IMedicament, 'id' | 'denumire'> | null;
  medic?: Pick<IMedic, 'id' | 'nume'> | null;
}

export type NewRaportAnalitic = Omit<IRaportAnalitic, 'id'> & { id: null };
