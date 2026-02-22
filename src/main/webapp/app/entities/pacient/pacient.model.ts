import { IMedic } from 'app/entities/medic/medic.model';
import { IFarmacist } from 'app/entities/farmacist/farmacist.model';

export interface IPacient {
  id: number;
  nume?: string | null;
  prenume?: string | null;
  sex?: string | null;
  varsta?: number | null;
  greutate?: number | null;
  inaltime?: number | null;
  circumferintaAbdominala?: number | null;
  cnp?: string | null;
  comorbiditati?: string | null;
  gradSedentarism?: string | null;
  istoricTratament?: string | null;
  toleranta?: string | null;
  email?: string | null;
  telefon?: string | null;
  medic?: Pick<IMedic, 'id' | 'nume'> | null;
  farmacist?: Pick<IFarmacist, 'id' | 'nume'> | null;
}

export type NewPacient = Omit<IPacient, 'id'> & { id: null };
