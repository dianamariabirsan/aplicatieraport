import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../decision-log.test-samples';

import { DecisionLogFormService } from './decision-log-form.service';

describe('DecisionLog Form Service', () => {
  let service: DecisionLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DecisionLogFormService);
  });

  describe('Service methods', () => {
    describe('createDecisionLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDecisionLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            timestamp: expect.any(Object),
            actorType: expect.any(Object),
            recomandare: expect.any(Object),
            modelScore: expect.any(Object),
            reguliTriggered: expect.any(Object),
            externalChecks: expect.any(Object),
            alocare: expect.any(Object),
          }),
        );
      });

      it('passing IDecisionLog should create a new form with FormGroup', () => {
        const formGroup = service.createDecisionLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            timestamp: expect.any(Object),
            actorType: expect.any(Object),
            recomandare: expect.any(Object),
            modelScore: expect.any(Object),
            reguliTriggered: expect.any(Object),
            externalChecks: expect.any(Object),
            alocare: expect.any(Object),
          }),
        );
      });
    });

    describe('getDecisionLog', () => {
      it('should return NewDecisionLog for default DecisionLog initial value', () => {
        const formGroup = service.createDecisionLogFormGroup(sampleWithNewData);

        const decisionLog = service.getDecisionLog(formGroup) as any;

        expect(decisionLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewDecisionLog for empty DecisionLog initial value', () => {
        const formGroup = service.createDecisionLogFormGroup();

        const decisionLog = service.getDecisionLog(formGroup) as any;

        expect(decisionLog).toMatchObject({});
      });

      it('should return IDecisionLog', () => {
        const formGroup = service.createDecisionLogFormGroup(sampleWithRequiredData);

        const decisionLog = service.getDecisionLog(formGroup) as any;

        expect(decisionLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDecisionLog should not enable id FormControl', () => {
        const formGroup = service.createDecisionLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDecisionLog should disable id FormControl', () => {
        const formGroup = service.createDecisionLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
