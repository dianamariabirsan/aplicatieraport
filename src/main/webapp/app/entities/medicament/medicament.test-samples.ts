import { IMedicament, NewMedicament } from './medicament.model';

export const sampleWithRequiredData: IMedicament = {
  id: 1757,
  denumire: 'punctual upright',
  substanta: 'apropos dusk chilly',
};

export const sampleWithPartialData: IMedicament = {
  id: 22204,
  denumire: 'treasure whose till',
  substanta: 'creaking aha circa',
  contraindicatii: 'husk',
  interactiuni: 'um tame',
  dozaRecomandata: 'ack or',
};

export const sampleWithFullData: IMedicament = {
  id: 31895,
  denumire: 'mentor whoa',
  substanta: 'boo instead',
  indicatii: 'spectacles meh',
  contraindicatii: 'normal different',
  interactiuni: 'equally before yippee',
  dozaRecomandata: 'facilitate',
  formaFarmaceutica: 'save dull along',
};

export const sampleWithNewData: NewMedicament = {
  denumire: 'openly',
  substanta: 'collaborate airman beside',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
