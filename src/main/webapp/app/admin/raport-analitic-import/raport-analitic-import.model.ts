export interface ICsvRaportAnaliticImportRowError {
  rowNumber: number;
  message: string;
}

export interface ICsvRaportAnaliticImportResult {
  createdCount: number;
  updatedCount: number;
  failedCount: number;
  errors: ICsvRaportAnaliticImportRowError[];
}
