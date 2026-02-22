import { IExternalDrugInfo } from 'app/entities/external-drug-info/external-drug-info.model';

export interface IMedicament {
  id: number;
  denumire?: string | null;
  substanta?: string | null;
  indicatii?: string | null;
  contraindicatii?: string | null;
  interactiuni?: string | null;
  dozaRecomandata?: string | null;
  formaFarmaceutica?: string | null;
  infoExtern?: Pick<IExternalDrugInfo, 'id'> | null;
}

export type NewMedicament = Omit<IMedicament, 'id'> & { id: null };
