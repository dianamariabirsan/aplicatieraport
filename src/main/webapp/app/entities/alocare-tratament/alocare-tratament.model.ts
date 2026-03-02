import dayjs from 'dayjs/esm';
import { IMedic } from 'app/entities/medic/medic.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { IPacient } from 'app/entities/pacient/pacient.model';

export interface IAlocareTratament {
  id: number;
  dataDecizie?: dayjs.Dayjs | null;
  tratamentPropus?: string | null;
  motivDecizie?: string | null;
  scorDecizie?: number | null;
  decizieValidata?: boolean | null;
  medic?: Pick<IMedic, 'id' | 'nume'> | null;
  medicament?: Pick<IMedicament, 'id' | 'denumire'> | null;
  pacient?: Pick<IPacient, 'id' | 'nume'> | null;
}

export type NewAlocareTratament = Omit<IAlocareTratament, 'id'> & { id: null };
