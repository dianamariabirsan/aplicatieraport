import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedic } from '../medic.model';
import { MedicService } from '../service/medic.service';
import { MedicFormGroup, MedicFormService } from './medic-form.service';

@Component({
  selector: 'jhi-medic-update',
  templateUrl: './medic-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicUpdateComponent implements OnInit {
  isSaving = false;
  medic: IMedic | null = null;

  protected medicService = inject(MedicService);
  protected medicFormService = inject(MedicFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicFormGroup = this.medicFormService.createMedicFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medic }) => {
      this.medic = medic;
      if (medic) {
        this.updateForm(medic);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medic = this.medicFormService.getMedic(this.editForm);
    if (medic.id !== null) {
      this.subscribeToSaveResponse(this.medicService.update(medic));
    } else {
      this.subscribeToSaveResponse(this.medicService.create(medic));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedic>>): void {
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

  protected updateForm(medic: IMedic): void {
    this.medic = medic;
    this.medicFormService.resetForm(this.editForm, medic);
  }
}
