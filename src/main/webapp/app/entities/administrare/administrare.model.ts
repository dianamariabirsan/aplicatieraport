import dayjs from 'dayjs/esm';
import { IPacient } from 'app/entities/pacient/pacient.model';
import { IFarmacist } from 'app/entities/farmacist/farmacist.model';
import { IMedicament } from 'app/entities/medicament/medicament.model';

export interface IAdministrare {
  id: number;
  dataAdministrare?: dayjs.Dayjs | null;
  tipTratament?: string | null;
  doza?: number | null;
  unitate?: string | null;
  modAdministrare?: string | null;
  observatii?: string | null;
  pacient?: Pick<IPacient, 'id' | 'nume'> | null;
  farmacist?: Pick<IFarmacist, 'id'> | null;
  medicament?: Pick<IMedicament, 'id' | 'denumire'> | null;
}

export type NewAdministrare = Omit<IAdministrare, 'id'> & { id: null };
