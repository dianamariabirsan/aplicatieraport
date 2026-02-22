import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFarmacist } from '../farmacist.model';
import { FarmacistService } from '../service/farmacist.service';
import { FarmacistFormGroup, FarmacistFormService } from './farmacist-form.service';

@Component({
  selector: 'jhi-farmacist-update',
  templateUrl: './farmacist-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FarmacistUpdateComponent implements OnInit {
  isSaving = false;
  farmacist: IFarmacist | null = null;

  protected farmacistService = inject(FarmacistService);
  protected farmacistFormService = inject(FarmacistFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FarmacistFormGroup = this.farmacistFormService.createFarmacistFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ farmacist }) => {
      this.farmacist = farmacist;
      if (farmacist) {
        this.updateForm(farmacist);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const farmacist = this.farmacistFormService.getFarmacist(this.editForm);
    if (farmacist.id !== null) {
      this.subscribeToSaveResponse(this.farmacistService.update(farmacist));
    } else {
      this.subscribeToSaveResponse(this.farmacistService.create(farmacist));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFarmacist>>): void {
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

  protected updateForm(farmacist: IFarmacist): void {
    this.farmacist = farmacist;
    this.farmacistFormService.resetForm(this.editForm, farmacist);
  }
}
