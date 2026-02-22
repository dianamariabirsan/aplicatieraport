import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../medicament.test-samples';

import { MedicamentFormService } from './medicament-form.service';

describe('Medicament Form Service', () => {
  let service: MedicamentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicamentFormService);
  });

  describe('Service methods', () => {
    describe('createMedicamentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicamentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            denumire: expect.any(Object),
            substanta: expect.any(Object),
            indicatii: expect.any(Object),
            contraindicatii: expect.any(Object),
            interactiuni: expect.any(Object),
            dozaRecomandata: expect.any(Object),
            formaFarmaceutica: expect.any(Object),
            infoExtern: expect.any(Object),
          }),
        );
      });

      it('passing IMedicament should create a new form with FormGroup', () => {
        const formGroup = service.createMedicamentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            denumire: expect.any(Object),
            substanta: expect.any(Object),
            indicatii: expect.any(Object),
            contraindicatii: expect.any(Object),
            interactiuni: expect.any(Object),
            dozaRecomandata: expect.any(Object),
            formaFarmaceutica: expect.any(Object),
            infoExtern: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicament', () => {
      it('should return NewMedicament for default Medicament initial value', () => {
        const formGroup = service.createMedicamentFormGroup(sampleWithNewData);

        const medicament = service.getMedicament(formGroup) as any;

        expect(medicament).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicament for empty Medicament initial value', () => {
        const formGroup = service.createMedicamentFormGroup();

        const medicament = service.getMedicament(formGroup) as any;

        expect(medicament).toMatchObject({});
      });

      it('should return IMedicament', () => {
        const formGroup = service.createMedicamentFormGroup(sampleWithRequiredData);

        const medicament = service.getMedicament(formGroup) as any;

        expect(medicament).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicament should not enable id FormControl', () => {
        const formGroup = service.createMedicamentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicament should disable id FormControl', () => {
        const formGroup = service.createMedicamentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
