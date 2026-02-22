import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IStudiiLiteratura } from '../studii-literatura.model';
import { StudiiLiteraturaService } from '../service/studii-literatura.service';
import { StudiiLiteraturaFormGroup, StudiiLiteraturaFormService } from './studii-literatura-form.service';

@Component({
  selector: 'jhi-studii-literatura-update',
  templateUrl: './studii-literatura-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StudiiLiteraturaUpdateComponent implements OnInit {
  isSaving = false;
  studiiLiteratura: IStudiiLiteratura | null = null;

  medicamentsSharedCollection: IMedicament[] = [];

  protected studiiLiteraturaService = inject(StudiiLiteraturaService);
  protected studiiLiteraturaFormService = inject(StudiiLiteraturaFormService);
  protected medicamentService = inject(MedicamentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StudiiLiteraturaFormGroup = this.studiiLiteraturaFormService.createStudiiLiteraturaFormGroup();

  compareMedicament = (o1: IMedicament | null, o2: IMedicament | null): boolean => this.medicamentService.compareMedicament(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studiiLiteratura }) => {
      this.studiiLiteratura = studiiLiteratura;
      if (studiiLiteratura) {
        this.updateForm(studiiLiteratura);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studiiLiteratura = this.studiiLiteraturaFormService.getStudiiLiteratura(this.editForm);
    if (studiiLiteratura.id !== null) {
      this.subscribeToSaveResponse(this.studiiLiteraturaService.update(studiiLiteratura));
    } else {
      this.subscribeToSaveResponse(this.studiiLiteraturaService.create(studiiLiteratura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudiiLiteratura>>): void {
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

  protected updateForm(studiiLiteratura: IStudiiLiteratura): void {
    this.studiiLiteratura = studiiLiteratura;
    this.studiiLiteraturaFormService.resetForm(this.editForm, studiiLiteratura);

    this.medicamentsSharedCollection = this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(
      this.medicamentsSharedCollection,
      studiiLiteratura.medicament,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicamentService
      .query()
      .pipe(map((res: HttpResponse<IMedicament[]>) => res.body ?? []))
      .pipe(
        map((medicaments: IMedicament[]) =>
          this.medicamentService.addMedicamentToCollectionIfMissing<IMedicament>(medicaments, this.studiiLiteratura?.medicament),
        ),
      )
      .subscribe((medicaments: IMedicament[]) => (this.medicamentsSharedCollection = medicaments));
  }
}
