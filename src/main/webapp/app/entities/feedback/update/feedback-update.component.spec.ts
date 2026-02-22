import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAlocareTratament } from 'app/entities/alocare-tratament/alocare-tratament.model';
import { AlocareTratamentService } from 'app/entities/alocare-tratament/service/alocare-tratament.service';
import { FeedbackService } from '../service/feedback.service';
import { IFeedback } from '../feedback.model';
import { FeedbackFormService } from './feedback-form.service';

import { FeedbackUpdateComponent } from './feedback-update.component';

describe('Feedback Management Update Component', () => {
  let comp: FeedbackUpdateComponent;
  let fixture: ComponentFixture<FeedbackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackFormService: FeedbackFormService;
  let feedbackService: FeedbackService;
  let alocareTratamentService: AlocareTratamentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedbackUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FeedbackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackFormService = TestBed.inject(FeedbackFormService);
    feedbackService = TestBed.inject(FeedbackService);
    alocareTratamentService = TestBed.inject(AlocareTratamentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AlocareTratament query and add missing value', () => {
      const feedback: IFeedback = { id: 1452 };
      const alocare: IAlocareTratament = { id: 13651 };
      feedback.alocare = alocare;

      const alocareTratamentCollection: IAlocareTratament[] = [{ id: 13651 }];
      jest.spyOn(alocareTratamentService, 'query').mockReturnValue(of(new HttpResponse({ body: alocareTratamentCollection })));
      const additionalAlocareTrataments = [alocare];
      const expectedCollection: IAlocareTratament[] = [...additionalAlocareTrataments, ...alocareTratamentCollection];
      jest.spyOn(alocareTratamentService, 'addAlocareTratamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(alocareTratamentService.query).toHaveBeenCalled();
      expect(alocareTratamentService.addAlocareTratamentToCollectionIfMissing).toHaveBeenCalledWith(
        alocareTratamentCollection,
        ...additionalAlocareTrataments.map(expect.objectContaining),
      );
      expect(comp.alocareTratamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const feedback: IFeedback = { id: 1452 };
      const alocare: IAlocareTratament = { id: 13651 };
      feedback.alocare = alocare;

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(comp.alocareTratamentsSharedCollection).toContainEqual(alocare);
      expect(comp.feedback).toEqual(feedback);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 10592 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue(feedback);
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackService.update).toHaveBeenCalledWith(expect.objectContaining(feedback));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 10592 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue({ id: null });
      jest.spyOn(feedbackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(feedbackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 10592 };
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAlocareTratament', () => {
      it('should forward to alocareTratamentService', () => {
        const entity = { id: 13651 };
        const entity2 = { id: 1228 };
        jest.spyOn(alocareTratamentService, 'compareAlocareTratament');
        comp.compareAlocareTratament(entity, entity2);
        expect(alocareTratamentService.compareAlocareTratament).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
