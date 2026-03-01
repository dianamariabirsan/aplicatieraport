import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormatMediumDatetimePipe } from 'app/shared/date';

import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { IDecisionLog } from 'app/entities/decision-log/decision-log.model';
import { DecisionLogService } from 'app/entities/decision-log/service/decision-log.service';
import { AlocareTratamentService } from '../service/alocare-tratament.service';
import { IAlocareTratament } from '../alocare-tratament.model';
import { AlocareTratamentFormGroup, AlocareTratamentFormService } from './alocare-tratament-form.service';

@Component({
  selector: 'jhi-alocare-tratament-update',
  templateUrl: './alocare-tratament-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, FormatMediumDatetimePipe],
})
export class AlocareTratamentUpdateComponent implements OnInit {
  isSaving = false;
  alocareTratament: IAlocareTratament | null = null;
  decisionLogs: IDecisionLog[] = [];

  medicsSharedCollection: IMedic[] = [];
  medicamentsSharedCollection: IMedicament[] = [];
  pacientsSharedCollection: IPacient[] = [];

  protected alocareTratamentService = inject(AlocareTratamentService);
  protected alocareTratamentFormService = inject(AlocareTratamentFormService);
  protected medicService = inject(MedicService);
  protected medicamentService = inject(MedicamentService);
  protected pacientService = inject(PacientService);
  protected decisionLogService = inject(DecisionLogService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlocareTratamentFormGroup = this.alocareTratamentFormService.createAlocareTratamentFormGroup();

  compareMedic = (o1: IMedic | null, o2: IMedic | null): boolean => this.medicService.compareMedic(o1, o2);

  compareMedicament = (o1: IMedicament | null, o2: IMedicament | null): boolean => this.medicamentService.compareMedicament(o1, o2);

  comparePacient = (o1: IPacient | null, o2: IPacient | null): boolean => this.pacientService.comparePacient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alocareTratament }) => {
      this.alocareTratament = alocareTratament;
      if (alocareTratament) {
        this.updateForm(alocareTratament);
        if (alocareTratament.id) {
          this.decisionLogService.queryByAlocareId(alocareTratament.id).subscribe({
            next: res => (this.decisionLogs = res.body ?? []),
          });
        }
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alocareTratament = this.alocareTratamentFormService.getAlocareTratament(this.editForm);
    if (alocareTratament.id !== null) {
      this.subscribeToSaveResponse(this.alocareTratamentService.update(alocareTratament));
    } else {
      this.subscribeToSaveResponse(this.alocareTratamentService.create(alocareTratament));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlocareTratament>>): void {
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

  protected updateForm(alocareTratament: IAlocareTratament): void {
    this.alocareTratament = alocareTratament;
    this.alocareTratamentFormService.resetForm(this.editForm, alocareTratament);

    this.medicsSharedCollection = this.medicService.addMedicToCollectionIfMissing<IMedic>(
      this.medicsSharedCollection,
      alocareTratament.medic,
    );
    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(
      this.medicamentsSharedCollection,
      alocareTratament.medicament,
    );
    this.pacientsSharedCollection = this.pacientService.addPacientToCollectionIfMissing<IPacient>(
      this.pacientsSharedCollection,
      alocareTratament.pacient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicService
      .query()
      .pipe(map((res: HttpResponse<IMedic[]>) => res.body ?? []))
      .pipe(map((medics: IMedic[]) => this.medicService.addMedicToCollectionIfMissing<IMedic>(medics, this.alocareTratament?.medic)))
      .subscribe((medics: IMedic[]) => (this.medicsSharedCollection = medics));

    this.medicamentService
      .query()
      .pipe(map((res: HttpResponse<IMedicament[]>) => res.body ?? []))
      .pipe(
        map((medicaments: IMedicament[]) =>
          this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(medicaments, this.alocareTratament?.medicament),
        ),
      )
      .subscribe((medicaments: IMedicament[]) => (this.medicamentsSharedCollection = medicaments));

    this.pacientService
      .query()
      .pipe(map((res: HttpResponse<IPacient[]>) => res.body ?? []))
      .pipe(
        map((pacients: IPacient[]) =>
          this.pacientService.addPacientToCollectionIfMissing<IPacient>(pacients, this.alocareTratament?.pacient),
        ),
      )
      .subscribe((pacients: IPacient[]) => (this.pacientsSharedCollection = pacients));
  }
}
