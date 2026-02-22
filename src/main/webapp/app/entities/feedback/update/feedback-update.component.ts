import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAlocareTratament } from 'app/entities/alocare-tratament/alocare-tratament.model';
import { AlocareTratamentService } from 'app/entities/alocare-tratament/service/alocare-tratament.service';
import { IFeedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';
import { FeedbackFormGroup, FeedbackFormService } from './feedback-form.service';

@Component({
  selector: 'jhi-feedback-update',
  templateUrl: './feedback-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedbackUpdateComponent implements OnInit {
  isSaving = false;
  feedback: IFeedback | null = null;

  alocareTratamentsSharedCollection: IAlocareTratament[] = [];

  protected feedbackService = inject(FeedbackService);
  protected feedbackFormService = inject(FeedbackFormService);
  protected alocareTratamentService = inject(AlocareTratamentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedbackFormGroup = this.feedbackFormService.createFeedbackFormGroup();

  compareAlocareTratament = (o1: IAlocareTratament | null, o2: IAlocareTratament | null): boolean =>
    this.alocareTratamentService.compareAlocareTratament(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedback }) => {
      this.feedback = feedback;
      if (feedback) {
        this.updateForm(feedback);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedback = this.feedbackFormService.getFeedback(this.editForm);
    if (feedback.id !== null) {
      this.subscribeToSaveResponse(this.feedbackService.update(feedback));
    } else {
      this.subscribeToSaveResponse(this.feedbackService.create(feedback));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedback>>): void {
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

  protected updateForm(feedback: IFeedback): void {
    this.feedback = feedback;
    this.feedbackFormService.resetForm(this.editForm, feedback);

    this.alocareTratamentsSharedCollection = this.alocareTratamentService.addAlocareTratamentToCollectionIfMissing<IAlocareTratament>(
      this.alocareTratamentsSharedCollection,
      feedback.alocare,
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
            this.feedback?.alocare,
          ),
        ),
      )
      .subscribe((alocareTrataments: IAlocareTratament[]) => (this.alocareTratamentsSharedCollection = alocareTrataments));
  }
}
