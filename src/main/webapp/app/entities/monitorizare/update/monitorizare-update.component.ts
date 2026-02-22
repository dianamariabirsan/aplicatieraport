import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { IMonitorizare } from '../monitorizare.model';
import { MonitorizareService } from '../service/monitorizare.service';
import { MonitorizareFormGroup, MonitorizareFormService } from './monitorizare-form.service';

@Component({
  selector: 'jhi-monitorizare-update',
  templateUrl: './monitorizare-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MonitorizareUpdateComponent implements OnInit {
  isSaving = false;
  monitorizare: IMonitorizare | null = null;

  pacientsSharedCollection: IPacient[] = [];

  protected monitorizareService = inject(MonitorizareService);
  protected monitorizareFormService = inject(MonitorizareFormService);
  protected pacientService = inject(PacientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MonitorizareFormGroup = this.monitorizareFormService.createMonitorizareFormGroup();

  comparePacient = (o1: IPacient | null, o2: IPacient | null): boolean => this.pacientService.comparePacient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monitorizare }) => {
      this.monitorizare = monitorizare;
      if (monitorizare) {
        this.updateForm(monitorizare);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const monitorizare = this.monitorizareFormService.getMonitorizare(this.editForm);
    if (monitorizare.id !== null) {
      this.subscribeToSaveResponse(this.monitorizareService.update(monitorizare));
    } else {
      this.subscribeToSaveResponse(this.monitorizareService.create(monitorizare));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonitorizare>>): void {
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

  protected updateForm(monitorizare: IMonitorizare): void {
    this.monitorizare = monitorizare;
    this.monitorizareFormService.resetForm(this.editForm, monitorizare);

    this.pacientsSharedCollection = this.pacientService.addPacientToCollectionIfMissing<IPacient>(
      this.pacientsSharedCollection,
      monitorizare.pacient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pacientService
      .query()
      .pipe(map((res: HttpResponse<IPacient[]>) => res.body ?? []))
      .pipe(
        map((pacients: IPacient[]) => this.pacientService.addPacientToCollectionIfMissing<IPacient>(pacients, this.monitorizare?.pacient)),
      )
      .subscribe((pacients: IPacient[]) => (this.pacientsSharedCollection = pacients));
  }
}
