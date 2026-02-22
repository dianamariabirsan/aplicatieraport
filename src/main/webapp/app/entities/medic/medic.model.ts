export interface IMedic {
  id: number;
  nume?: string | null;
  prenume?: string | null;
  specializare?: string | null;
  email?: string | null;
  telefon?: string | null;
  cabinet?: string | null;
}

export type NewMedic = Omit<IMedic, 'id'> & { id: null };
