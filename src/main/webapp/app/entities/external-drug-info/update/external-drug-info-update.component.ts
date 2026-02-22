import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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

  protected externalDrugInfoService = inject(ExternalDrugInfoService);
  protected externalDrugInfoFormService = inject(ExternalDrugInfoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExternalDrugInfoFormGroup = this.externalDrugInfoFormService.createExternalDrugInfoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ externalDrugInfo }) => {
      this.externalDrugInfo = externalDrugInfo;
      if (externalDrugInfo) {
        this.updateForm(externalDrugInfo);
      }
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
  }
}
