import { Component, OnInit, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExternalDrugInfo } from 'app/entities/external-drug-info/external-drug-info.model';
import { ExternalDrugInfoService } from 'app/entities/external-drug-info/service/external-drug-info.service';
import { IMedicament } from '../medicament.model';
import { MedicamentService } from '../service/medicament.service';
import { MedicamentFormGroup, MedicamentFormService } from './medicament-form.service';

@Component({
  selector: 'jhi-medicament-update',
  templateUrl: './medicament-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicamentUpdateComponent implements OnInit {
  isSaving = false;
  medicament: IMedicament | null = null;
  smpcUrl = '';

  infoExternsCollection: IExternalDrugInfo[] = [];

  protected medicamentService = inject(MedicamentService);
  protected medicamentFormService = inject(MedicamentFormService);
  protected externalDrugInfoService = inject(ExternalDrugInfoService);
  protected activatedRoute = inject(ActivatedRoute);
  protected http = inject(HttpClient);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicamentFormGroup = this.medicamentFormService.createMedicamentFormGroup();

  compareExternalDrugInfo = (o1: IExternalDrugInfo | null, o2: IExternalDrugInfo | null): boolean =>
    this.externalDrugInfoService.compareExternalDrugInfo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicament }) => {
      this.medicament = medicament;
      if (medicament) {
        this.updateForm(medicament);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicament = this.medicamentFormService.getMedicament(this.editForm);
    if (medicament.id !== null) {
      this.subscribeToSaveResponse(this.medicamentService.update(medicament));
    } else {
      this.subscribeToSaveResponse(this.medicamentService.create(medicament));
    }
  }

  onSmpcFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];
    const id = this.editForm.get('id')!.value;
    if (!id) return;
    const formData = new FormData();
    formData.append('file', file);
    this.http.post<IMedicament>(`/api/medicamente/${id}/smpc/upload`, formData).subscribe({
      next: updated => this.patchSmpcFields(updated),
      error: () => alert('Eroare la încărcarea PDF-ului SmPC.'),
    });
  }

  syncSmpcFromUrl(): void {
    const id = this.editForm.get('id')!.value;
    if (!id || !this.smpcUrl) return;
    this.http.post<IMedicament>(`/api/medicamente/${id}/smpc/url`, { url: this.smpcUrl }).subscribe({
      next: updated => this.patchSmpcFields(updated),
      error: () => alert('Eroare la sincronizarea SmPC din URL. Verificați URL-ul și încercați din nou.'),
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicament>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medicament: IMedicament): void {
    this.medicament = medicament;
    this.medicamentFormService.resetForm(this.editForm, medicament);

    this.infoExternsCollection = this.externalDrugInfoService.addExternalDrugInfoToCollectionIfMissing<IExternalDrugInfo>(
      this.infoExternsCollection,
      medicament.infoExtern,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.externalDrugInfoService
      .query({ 'medicamentId.specified': 'false' })
      .pipe(map((res: HttpResponse<IExternalDrugInfo[]>) => res.body ?? []))
      .pipe(
        map((externalDrugInfos: IExternalDrugInfo[]) =>
          this.externalDrugInfoService.addExternalDrugInfoToCollectionIfMissing<IExternalDrugInfo>(
            externalDrugInfos,
            this.medicament?.infoExtern,
          ),
        ),
      )
      .subscribe((externalDrugInfos: IExternalDrugInfo[]) => (this.infoExternsCollection = externalDrugInfos));
  }

  private patchSmpcFields(updated: IMedicament): void {
    this.editForm.patchValue({
      contraindicatii: updated.contraindicatii ?? null,
      interactiuni: updated.interactiuni ?? null,
      avertizari: updated.avertizari ?? null,
    });
  }
}
