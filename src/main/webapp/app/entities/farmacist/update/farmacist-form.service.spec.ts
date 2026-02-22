import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../farmacist.test-samples';

import { FarmacistFormService } from './farmacist-form.service';

describe('Farmacist Form Service', () => {
  let service: FarmacistFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FarmacistFormService);
  });

  describe('Service methods', () => {
    describe('createFarmacistFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFarmacistFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nume: expect.any(Object),
            prenume: expect.any(Object),
            farmacie: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
          }),
        );
      });

      it('passing IFarmacist should create a new form with FormGroup', () => {
        const formGroup = service.createFarmacistFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nume: expect.any(Object),
            prenume: expect.any(Object),
            farmacie: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
          }),
        );
      });
    });

    describe('getFarmacist', () => {
      it('should return NewFarmacist for default Farmacist initial value', () => {
        const formGroup = service.createFarmacistFormGroup(sampleWithNewData);

        const farmacist = service.getFarmacist(formGroup) as any;

        expect(farmacist).toMatchObject(sampleWithNewData);
      });

      it('should return NewFarmacist for empty Farmacist initial value', () => {
        const formGroup = service.createFarmacistFormGroup();

        const farmacist = service.getFarmacist(formGroup) as any;

        expect(farmacist).toMatchObject({});
      });

      it('should return IFarmacist', () => {
        const formGroup = service.createFarmacistFormGroup(sampleWithRequiredData);

        const farmacist = service.getFarmacist(formGroup) as any;

        expect(farmacist).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFarmacist should not enable id FormControl', () => {
        const formGroup = service.createFarmacistFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFarmacist should disable id FormControl', () => {
        const formGroup = service.createFarmacistFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
