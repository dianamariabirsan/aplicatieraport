import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { IFarmacist } from 'app/entities/farmacist/farmacist.model';
import { FarmacistService } from 'app/entities/farmacist/service/farmacist.service';
import { PacientService } from '../service/pacient.service';
import { IPacient } from '../pacient.model';
import { PacientFormGroup, PacientFormService } from './pacient-form.service';

@Component({
  selector: 'jhi-pacient-update',
  templateUrl: './pacient-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PacientUpdateComponent implements OnInit {
  isSaving = false;
  pacient: IPacient | null = null;

  medicsSharedCollection: IMedic[] = [];
  farmacistsSharedCollection: IFarmacist[] = [];

  protected pacientService = inject(PacientService);
  protected pacientFormService = inject(PacientFormService);
  protected medicService = inject(MedicService);
  protected farmacistService = inject(FarmacistService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PacientFormGroup = this.pacientFormService.createPacientFormGroup();

  compareMedic = (o1: IMedic | null, o2: IMedic | null): boolean => this.medicService.compareMedic(o1, o2);

  compareFarmacist = (o1: IFarmacist | null, o2: IFarmacist | null): boolean => this.farmacistService.compareFarmacist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pacient }) => {
      this.pacient = pacient;
      if (pacient) {
        this.updateForm(pacient);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pacient = this.pacientFormService.getPacient(this.editForm);
    if (pacient.id !== null) {
      this.subscribeToSaveResponse(this.pacientService.update(pacient));
    } else {
      this.subscribeToSaveResponse(this.pacientService.create(pacient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPacient>>): void {
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

  protected updateForm(pacient: IPacient): void {
    this.pacient = pacient;
    this.pacientFormService.resetForm(this.editForm, pacient);

    this.medicsSharedCollection = this.medicService.addMedicToCollectionIfMissing<IMedic>(this.medicsSharedCollection, pacient.medic);
    this.farmacistsSharedCollection = this.farmacistService.addFarmacistToCollectionIfMissing<IFarmacist>(
      this.farmacistsSharedCollection,
      pacient.farmacist,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicService
      .query()
      .pipe(map((res: HttpResponse<IMedic[]>) => res.body ?? []))
      .pipe(map((medics: IMedic[]) => this.medicService.addMedicToCollectionIfMissing<IMedic>(medics, this.pacient?.medic)))
      .subscribe((medics: IMedic[]) => (this.medicsSharedCollection = medics));

    this.farmacistService
      .query()
      .pipe(map((res: HttpResponse<IFarmacist[]>) => res.body ?? []))
      .pipe(
        map((farmacists: IFarmacist[]) =>
          this.farmacistService.addFarmacistToCollectionIfMissing<IFarmacist>(farmacists, this.pacient?.farmacist),
        ),
      )
      .subscribe((farmacists: IFarmacist[]) => (this.farmacistsSharedCollection = farmacists));
  }
}
