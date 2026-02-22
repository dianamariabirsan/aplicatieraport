export interface IFarmacist {
  id: number;
  nume?: string | null;
  prenume?: string | null;
  farmacie?: string | null;
  email?: string | null;
  telefon?: string | null;
}

export type NewFarmacist = Omit<IFarmacist, 'id'> & { id: null };
