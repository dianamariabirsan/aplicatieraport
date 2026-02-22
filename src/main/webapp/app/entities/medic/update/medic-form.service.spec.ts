import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../medic.test-samples';

import { MedicFormService } from './medic-form.service';

describe('Medic Form Service', () => {
  let service: MedicFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicFormService);
  });

  describe('Service methods', () => {
    describe('createMedicFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nume: expect.any(Object),
            prenume: expect.any(Object),
            specializare: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
            cabinet: expect.any(Object),
          }),
        );
      });

      it('passing IMedic should create a new form with FormGroup', () => {
        const formGroup = service.createMedicFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nume: expect.any(Object),
            prenume: expect.any(Object),
            specializare: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
            cabinet: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedic', () => {
      it('should return NewMedic for default Medic initial value', () => {
        const formGroup = service.createMedicFormGroup(sampleWithNewData);

        const medic = service.getMedic(formGroup) as any;

        expect(medic).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedic for empty Medic initial value', () => {
        const formGroup = service.createMedicFormGroup();

        const medic = service.getMedic(formGroup) as any;

        expect(medic).toMatchObject({});
      });

      it('should return IMedic', () => {
        const formGroup = service.createMedicFormGroup(sampleWithRequiredData);

        const medic = service.getMedic(formGroup) as any;

        expect(medic).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedic should not enable id FormControl', () => {
        const formGroup = service.createMedicFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedic should disable id FormControl', () => {
        const formGroup = service.createMedicFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
