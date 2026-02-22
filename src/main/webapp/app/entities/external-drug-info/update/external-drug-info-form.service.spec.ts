import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../external-drug-info.test-samples';

import { ExternalDrugInfoFormService } from './external-drug-info-form.service';

describe('ExternalDrugInfo Form Service', () => {
  let service: ExternalDrugInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExternalDrugInfoFormService);
  });

  describe('Service methods', () => {
    describe('createExternalDrugInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExternalDrugInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            source: expect.any(Object),
            productSummary: expect.any(Object),
            lastUpdated: expect.any(Object),
            sourceUrl: expect.any(Object),
          }),
        );
      });

      it('passing IExternalDrugInfo should create a new form with FormGroup', () => {
        const formGroup = service.createExternalDrugInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            source: expect.any(Object),
            productSummary: expect.any(Object),
            lastUpdated: expect.any(Object),
            sourceUrl: expect.any(Object),
          }),
        );
      });
    });

    describe('getExternalDrugInfo', () => {
      it('should return NewExternalDrugInfo for default ExternalDrugInfo initial value', () => {
        const formGroup = service.createExternalDrugInfoFormGroup(sampleWithNewData);

        const externalDrugInfo = service.getExternalDrugInfo(formGroup) as any;

        expect(externalDrugInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewExternalDrugInfo for empty ExternalDrugInfo initial value', () => {
        const formGroup = service.createExternalDrugInfoFormGroup();

        const externalDrugInfo = service.getExternalDrugInfo(formGroup) as any;

        expect(externalDrugInfo).toMatchObject({});
      });

      it('should return IExternalDrugInfo', () => {
        const formGroup = service.createExternalDrugInfoFormGroup(sampleWithRequiredData);

        const externalDrugInfo = service.getExternalDrugInfo(formGroup) as any;

        expect(externalDrugInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExternalDrugInfo should not enable id FormControl', () => {
        const formGroup = service.createExternalDrugInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExternalDrugInfo should disable id FormControl', () => {
        const formGroup = service.createExternalDrugInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
