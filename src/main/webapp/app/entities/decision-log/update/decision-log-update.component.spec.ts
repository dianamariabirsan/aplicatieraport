import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { from, of } from 'rxjs';

import { IAlocareTratament } from 'app/entities/alocare-tratament/alocare-tratament.model';
import { AlocareTratamentService } from 'app/entities/alocare-tratament/service/alocare-tratament.service';
import { DecisionLogService } from '../service/decision-log.service';
import { IDecisionLog } from '../decision-log.model';
import { DecisionLogFormService } from './decision-log-form.service';

import { DecisionLogUpdateComponent } from './decision-log-update.component';

describe('DecisionLog Management Update Component', () => {
  let comp: DecisionLogUpdateComponent;
  let fixture: ComponentFixture<DecisionLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let decisionLogFormService: DecisionLogFormService;
  let decisionLogService: DecisionLogService;
  let alocareTratamentService: AlocareTratamentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DecisionLogUpdateComponent],
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
      .overrideTemplate(DecisionLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DecisionLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    decisionLogFormService = TestBed.inject(DecisionLogFormService);
    decisionLogService = TestBed.inject(DecisionLogService);
    alocareTratamentService = TestBed.inject(AlocareTratamentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AlocareTratament query and add missing value', () => {
      const decisionLog: IDecisionLog = { id: 1703 };
      const alocare: IAlocareTratament = { id: 13651 };
      decisionLog.alocare = alocare;

      const alocareTratamentCollection: IAlocareTratament[] = [{ id: 13651 }];
      jest.spyOn(alocareTratamentService, 'query').mockReturnValue(of(new HttpResponse({ body: alocareTratamentCollection })));
      const additionalAlocareTrataments = [alocare];
      const expectedCollection: IAlocareTratament[] = [...additionalAlocareTrataments, ...alocareTratamentCollection];
      jest.spyOn(alocareTratamentService, 'addAlocareTratamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ decisionLog });
      comp.ngOnInit();

      expect(alocareTratamentService.query).toHaveBeenCalled();
      expect(alocareTratamentService.addAlocareTratamentToCollectionIfMissing).toHaveBeenCalledWith(
        alocareTratamentCollection,
        ...additionalAlocareTrataments.map(expect.objectContaining),
      );
      expect(comp.alocareTratamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const decisionLog: IDecisionLog = { id: 1703 };
      const alocare: IAlocareTratament = { id: 13651 };
      decisionLog.alocare = alocare;

      activatedRoute.data = of({ decisionLog });
      comp.ngOnInit();

      expect(comp.alocareTratamentsSharedCollection).toContainEqual(alocare);
      expect(comp.decisionLog).toEqual(decisionLog);
    });
  });

  describe('save', () => {
    it('should call previousState on save without invoking any write service methods', () => {
      // GIVEN
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ decisionLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
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
