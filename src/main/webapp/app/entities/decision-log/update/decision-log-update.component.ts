import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAlocareTratament } from 'app/entities/alocare-tratament/alocare-tratament.model';
import { AlocareTratamentService } from 'app/entities/alocare-tratament/service/alocare-tratament.service';
import { ActorType } from 'app/entities/enumerations/actor-type.model';
import { DecisionLogService } from '../service/decision-log.service';
import { IDecisionLog } from '../decision-log.model';
import { DecisionLogFormGroup, DecisionLogFormService } from './decision-log-form.service';

@Component({
  selector: 'jhi-decision-log-update',
  templateUrl: './decision-log-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DecisionLogUpdateComponent implements OnInit {
  isSaving = false;
  decisionLog: IDecisionLog | null = null;
  actorTypeValues = Object.keys(ActorType);

  alocareTratamentsSharedCollection: IAlocareTratament[] = [];

  protected decisionLogService = inject(DecisionLogService);
  protected decisionLogFormService = inject(DecisionLogFormService);
  protected alocareTratamentService = inject(AlocareTratamentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DecisionLogFormGroup = this.decisionLogFormService.createDecisionLogFormGroup();

  compareAlocareTratament = (o1: IAlocareTratament | null, o2: IAlocareTratament | null): boolean =>
    this.alocareTratamentService.compareAlocareTratament(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decisionLog }) => {
      this.decisionLog = decisionLog;
      if (decisionLog) {
        this.updateForm(decisionLog);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decisionLog = this.decisionLogFormService.getDecisionLog(this.editForm);
    if (decisionLog.id !== null) {
      this.subscribeToSaveResponse(this.decisionLogService.update(decisionLog));
    } else {
      this.subscribeToSaveResponse(this.decisionLogService.create(decisionLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecisionLog>>): void {
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

  protected updateForm(decisionLog: IDecisionLog): void {
    this.decisionLog = decisionLog;
    this.decisionLogFormService.resetForm(this.editForm, decisionLog);

    this.alocareTratamentsSharedCollection = this.alocareTratamentService.addAlocareTratamentToCollectionIfMissing<IAlocareTratament>(
      this.alocareTratamentsSharedCollection,
      decisionLog.alocare,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alocareTratamentService
      .query()
      .pipe(map((res: HttpResponse<IAlocareTratament[]>) => res.body ?? []))
      .pipe(
        map((alocareTrataments: IAlocareTratament[]) =>
          this.alocareTratamentService.addAlocareTratamentToCollectionIfMissing<IAlocareTratament>(
            alocareTrataments,
            this.decisionLog?.alocare,
          ),
        ),
      )
      .subscribe((alocareTrataments: IAlocareTratament[]) => (this.alocareTratamentsSharedCollection = alocareTrataments));
  }
}
