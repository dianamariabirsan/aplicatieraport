import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IExternalDrugInfo } from '../external-drug-info.model';
import { ExternalDrugInfoService } from '../service/external-drug-info.service';
import { ExternalDrugInfoFormGroup, ExternalDrugInfoFormService } from './external-drug-info-form.service';

@Component({
  selector: 'jhi-external-drug-info-update',
  templateUrl: './external-drug-info-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExternalDrugInfoUpdateComponent implements OnInit {
  isSaving = false;
  externalDrugInfo: IExternalDrugInfo | null = null;

  medicamentsSharedCollection: IMedicament[] = [];

  protected externalDrugInfoService = inject(ExternalDrugInfoService);
  protected externalDrugInfoFormService = inject(ExternalDrugInfoFormService);
  protected medicamentService = inject(MedicamentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExternalDrugInfoFormGroup = this.externalDrugInfoFormService.createExternalDrugInfoFormGroup();

  compareMedicament = (o1: Pick<IMedicament, 'id'> | null, o2: Pick<IMedicament, 'id'> | null): boolean =>
    this.medicamentService.compareMedicament(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ externalDrugInfo }) => {
      this.externalDrugInfo = externalDrugInfo;
      if (externalDrugInfo) {
        this.updateForm(externalDrugInfo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const externalDrugInfo = this.externalDrugInfoFormService.getExternalDrugInfo(this.editForm);
    if (externalDrugInfo.id !== null) {
      this.subscribeToSaveResponse(this.externalDrugInfoService.update(externalDrugInfo));
    } else {
      this.subscribeToSaveResponse(this.externalDrugInfoService.create(externalDrugInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExternalDrugInfo>>): void {
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

  protected updateForm(externalDrugInfo: IExternalDrugInfo): void {
    this.externalDrugInfo = externalDrugInfo;
    this.externalDrugInfoFormService.resetForm(this.editForm, externalDrugInfo);

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(
      this.medicamentsSharedCollection,
      externalDrugInfo.medicament as IMedicament | null | undefined,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicamentService
      .query({ sort: ['denumire,asc'] })
      .pipe(map((res: HttpResponse<IMedicament[]>) => res.body ?? []))
      .pipe(
        map((medicaments: IMedicament[]) =>
          this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(
            medicaments,
            this.externalDrugInfo?.medicament as IMedicament | null | undefined,
          ),
        ),
      )
      .subscribe((medicaments: IMedicament[]) => (this.medicamentsSharedCollection = medicaments));
  }
}
