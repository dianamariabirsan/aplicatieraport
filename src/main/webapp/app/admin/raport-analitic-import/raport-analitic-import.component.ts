import { Component, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICsvRaportAnaliticImportResult } from './raport-analitic-import.model';
import { RaportAnaliticImportService } from './service/raport-analitic-import.service';

@Component({
  selector: 'jhi-raport-analitic-import',
  templateUrl: './raport-analitic-import.component.html',
  imports: [SharedModule, RouterModule],
})
export default class RaportAnaliticImportComponent {
  selectedFile: File | null = null;
  isUploading = false;
  uploadError: string | null = null;
  result: ICsvRaportAnaliticImportResult | null = null;

  protected readonly raportAnaliticImportService = inject(RaportAnaliticImportService);

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;

    this.selectedFile = file;
    this.uploadError = null;
    this.result = null;
  }

  upload(): void {
    if (!this.selectedFile || this.isUploading) {
      return;
    }

    if (!this.selectedFile.name.toLowerCase().endsWith('.csv')) {
      this.uploadError = 'Selectează un fișier CSV valid.';
      return;
    }

    this.isUploading = true;
    this.uploadError = null;
    this.result = null;

    this.raportAnaliticImportService.uploadCsv(this.selectedFile).subscribe({
      next: response => {
        this.result = response;
        this.isUploading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.uploadError =
          error.error?.detail ?? error.error?.message ?? error.message ?? 'Importul CSV a eșuat. Verifică fișierul și încearcă din nou.';
        this.isUploading = false;
      },
    });
  }

  reset(): void {
    this.selectedFile = null;
    this.uploadError = null;
    this.result = null;
  }

  hasErrors(): boolean {
    return !!this.result && Array.isArray(this.result.errors) && this.result.errors.length > 0;
  }
}
