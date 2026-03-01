export interface IEvaluareMedicament {
  denumire?: string | null;
  rezumatSiguranta?: string | null;
  sursaUrl?: string | null;
  scor?: number | null;
}

export interface IStudiiCliniceStatistica {
  interogare?: string | null;
  linkClinicalTrials?: string | null;
  numarStudii?: number | null;
}

export interface ISigurantaExterna {
  contraindicatii?: string[] | null;
  interactiuni?: string[] | null;
  reactiiAdverse?: string[] | null;
  avertizari?: string[] | null;
}

export interface IEvaluareDecizieRezultat {
  recomandare?: string | null;
  motivare?: string | null;
  evaluareA?: IEvaluareMedicament | null;
  evaluareB?: IEvaluareMedicament | null;
  studiiClinice?: IStudiiCliniceStatistica | null;
  sigurantaExterna?: ISigurantaExterna | null;
  linkuri?: string[] | null;
}

export interface IEvaluareDecizieCerere {
  pacientId: number;
  tratamentA?: string | null;
  tratamentB?: string | null;
}
