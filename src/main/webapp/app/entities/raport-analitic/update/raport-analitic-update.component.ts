import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { RaportAnaliticService } from '../service/raport-analitic.service';
import { IRaportAnalitic } from '../raport-analitic.model';
import { RaportAnaliticFormGroup, RaportAnaliticFormService } from './raport-analitic-form.service';

@Component({
  selector: 'jhi-raport-analitic-update',
  templateUrl: './raport-analitic-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RaportAnaliticUpdateComponent implements OnInit {
  isSaving = false;
  raportAnalitic: IRaportAnalitic | null = null;

  medicamentsSharedCollection: IMedicament[] = [];
  medicsSharedCollection: IMedic[] = [];

  protected raportAnaliticService = inject(RaportAnaliticService);
  protected raportAnaliticFormService = inject(RaportAnaliticFormService);
  protected medicamentService = inject(MedicamentService);
  protected medicService = inject(MedicService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RaportAnaliticFormGroup = this.raportAnaliticFormService.createRaportAnaliticFormGroup();

  compareMedicament = (o1: IMedicament | null, o2: IMedicament | null): boolean => this.medicamentService.compareMedicament(o1, o2);

  compareMedic = (o1: IMedic | null, o2: IMedic | null): boolean => this.medicService.compareMedic(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raportAnalitic }) => {
      this.raportAnalitic = raportAnalitic;
      if (raportAnalitic) {
        this.updateForm(raportAnalitic);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const raportAnalitic = this.raportAnaliticFormService.getRaportAnalitic(this.editForm);
    if (raportAnalitic.id !== null) {
      this.subscribeToSaveResponse(this.raportAnaliticService.update(raportAnalitic));
    } else {
      this.subscribeToSaveResponse(this.raportAnaliticService.create(raportAnalitic));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaportAnalitic>>): void {
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

  protected updateForm(raportAnalitic: IRaportAnalitic): void {
    this.raportAnalitic = raportAnalitic;
    this.raportAnaliticFormService.resetForm(this.editForm, raportAnalitic);

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(
      this.medicamentsSharedCollection,
      raportAnalitic.medicament,
    );
    this.medicsSharedCollection = this.medicService.addMedicToCollectionIfMissing<IMedic>(
      this.medicsSharedCollection,
      raportAnalitic.medic,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicamentService
      .query()
      .pipe(map((res: HttpResponse<IMedicament[]>) => res.body ?? []))
      .pipe(
        map((medicaments: IMedicament[]) =>
          this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(medicaments, this.raportAnalitic?.medicament),
        ),
      )
      .subscribe((medicaments: IMedicament[]) => (this.medicamentsSharedCollection = medicaments));

    this.medicService
      .query()
      .pipe(map((res: HttpResponse<IMedic[]>) => res.body ?? []))
      .pipe(map((medics: IMedic[]) => this.medicService.addMedicToCollectionIfMissing<IMedic>(medics, this.raportAnalitic?.medic)))
      .subscribe((medics: IMedic[]) => (this.medicsSharedCollection = medics));
  }
}
