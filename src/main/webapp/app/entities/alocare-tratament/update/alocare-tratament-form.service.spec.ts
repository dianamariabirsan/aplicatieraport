import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../alocare-tratament.test-samples';

import { AlocareTratamentFormService } from './alocare-tratament-form.service';

describe('AlocareTratament Form Service', () => {
  let service: AlocareTratamentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlocareTratamentFormService);
  });

  describe('Service methods', () => {
    describe('createAlocareTratamentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlocareTratamentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataDecizie: expect.any(Object),
            tratamentPropus: expect.any(Object),
            motivDecizie: expect.any(Object),
            scorDecizie: expect.any(Object),
            decizieValidata: expect.any(Object),
            medic: expect.any(Object),
            medicament: expect.any(Object),
            pacient: expect.any(Object),
          }),
        );
      });

      it('passing IAlocareTratament should create a new form with FormGroup', () => {
        const formGroup = service.createAlocareTratamentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataDecizie: expect.any(Object),
            tratamentPropus: expect.any(Object),
            motivDecizie: expect.any(Object),
            scorDecizie: expect.any(Object),
            decizieValidata: expect.any(Object),
            medic: expect.any(Object),
            medicament: expect.any(Object),
            pacient: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlocareTratament', () => {
      it('should return NewAlocareTratament for default AlocareTratament initial value', () => {
        const formGroup = service.createAlocareTratamentFormGroup(sampleWithNewData);

        const alocareTratament = service.getAlocareTratament(formGroup) as any;

        expect(alocareTratament).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlocareTratament for empty AlocareTratament initial value', () => {
        const formGroup = service.createAlocareTratamentFormGroup();

        const alocareTratament = service.getAlocareTratament(formGroup) as any;

        expect(alocareTratament).toMatchObject({});
      });

      it('should return IAlocareTratament', () => {
        const formGroup = service.createAlocareTratamentFormGroup(sampleWithRequiredData);

        const alocareTratament = service.getAlocareTratament(formGroup) as any;

        expect(alocareTratament).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlocareTratament should not enable id FormControl', () => {
        const formGroup = service.createAlocareTratamentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlocareTratament should disable id FormControl', () => {
        const formGroup = service.createAlocareTratamentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
