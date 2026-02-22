import { IMedicament } from 'app/entities/medicament/medicament.model';

export interface IStudiiLiteratura {
  id: number;
  titlu?: string | null;
  autori?: string | null;
  anul?: number | null;
  tipStudiu?: string | null;
  substanta?: string | null;
  concluzie?: string | null;
  link?: string | null;
  medicament?: Pick<IMedicament, 'id'> | null;
}

export type NewStudiiLiteratura = Omit<IStudiiLiteratura, 'id'> & { id: null };
