import dayjs from 'dayjs/esm';
import { IAlocareTratament } from 'app/entities/alocare-tratament/alocare-tratament.model';
import { ActorType } from 'app/entities/enumerations/actor-type.model';

export interface IDecisionLog {
  id: number;
  timestamp?: dayjs.Dayjs | null;
  actorType?: keyof typeof ActorType | null;
  recomandare?: string | null;
  modelScore?: number | null;
  reguliTriggered?: string | null;
  externalChecks?: string | null;
  finalDecision?: string | null;
  decisionSource?: keyof typeof ActorType | null;
  overrideReason?: string | null;
  actiuneDescriere?: string | null;
  alocare?: Pick<IAlocareTratament, 'id'> | null;
}

export type NewDecisionLog = Omit<IDecisionLog, 'id'> & { id: null };
