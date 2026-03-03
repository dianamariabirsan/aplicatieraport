import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { SeveritateReactie } from 'app/entities/enumerations/severitate-reactie.model';
import { ReactieAdversaService } from '../service/reactie-adversa.service';
import { IReactieAdversa } from '../reactie-adversa.model';
import { ReactieAdversaFormGroup, ReactieAdversaFormService } from './reactie-adversa-form.service';

@Component({
  selector: 'jhi-reactie-adversa-update',
  templateUrl: './reactie-adversa-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReactieAdversaUpdateComponent implements OnInit {
  isSaving = false;
  reactieAdversa: IReactieAdversa | null = null;

  severitateReactieValues = Object.keys(SeveritateReactie);

  medicamentsSharedCollection: IMedicament[] = [];
  pacientsSharedCollection: IPacient[] = [];

  protected reactieAdversaService = inject(ReactieAdversaService);
  protected reactieAdversaFormService = inject(ReactieAdversaFormService);
  protected medicamentService = inject(MedicamentService);
  protected pacientService = inject(PacientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReactieAdversaFormGroup = this.reactieAdversaFormService.createReactieAdversaFormGroup();

  compareMedicament = (o1: IMedicament | null, o2: IMedicament | null): boolean => this.medicamentService.compareMedicament(o1, o2);

  comparePacient = (o1: IPacient | null, o2: IPacient | null): boolean => this.pacientService.comparePacient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reactieAdversa }) => {
      this.reactieAdversa = reactieAdversa;
      if (reactieAdversa) {
        this.updateForm(reactieAdversa);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reactieAdversa = this.reactieAdversaFormService.getReactieAdversa(this.editForm);
    if (reactieAdversa.id !== null) {
      this.subscribeToSaveResponse(this.reactieAdversaService.update(reactieAdversa));
    } else {
      this.subscribeToSaveResponse(this.reactieAdversaService.create(reactieAdversa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReactieAdversa>>): void {
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

  protected updateForm(reactieAdversa: IReactieAdversa): void {
    this.reactieAdversa = reactieAdversa;
    this.reactieAdversaFormService.resetForm(this.editForm, reactieAdversa);

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(
      this.medicamentsSharedCollection,
      reactieAdversa.medicament,
    );
    this.pacientsSharedCollection = this.pacientService.addPacientToCollectionIfMissing<IPacient>(
      this.pacientsSharedCollection,
      reactieAdversa.pacient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicamentService
      .query()
      .pipe(map((res: HttpResponse<IMedicament[]>) => res.body ?? []))
      .pipe(
        map((medicaments: IMedicament[]) =>
          this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(medicaments, this.reactieAdversa?.medicament),
        ),
      )
      .subscribe((medicaments: IMedicament[]) => (this.medicamentsSharedCollection = medicaments));

    this.pacientService
      .query()
      .pipe(map((res: HttpResponse<IPacient[]>) => res.body ?? []))
      .pipe(
        map((pacients: IPacient[]) =>
          this.pacientService.addPacientToCollectionIfMissing<IPacient>(pacients, this.reactieAdversa?.pacient),
        ),
      )
      .subscribe((pacients: IPacient[]) => (this.pacientsSharedCollection = pacients));
  }
}
