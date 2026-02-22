import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../studii-literatura.test-samples';

import { StudiiLiteraturaFormService } from './studii-literatura-form.service';

describe('StudiiLiteratura Form Service', () => {
  let service: StudiiLiteraturaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudiiLiteraturaFormService);
  });

  describe('Service methods', () => {
    describe('createStudiiLiteraturaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStudiiLiteraturaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titlu: expect.any(Object),
            autori: expect.any(Object),
            anul: expect.any(Object),
            tipStudiu: expect.any(Object),
            substanta: expect.any(Object),
            concluzie: expect.any(Object),
            link: expect.any(Object),
            medicament: expect.any(Object),
          }),
        );
      });

      it('passing IStudiiLiteratura should create a new form with FormGroup', () => {
        const formGroup = service.createStudiiLiteraturaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titlu: expect.any(Object),
            autori: expect.any(Object),
            anul: expect.any(Object),
            tipStudiu: expect.any(Object),
            substanta: expect.any(Object),
            concluzie: expect.any(Object),
            link: expect.any(Object),
            medicament: expect.any(Object),
          }),
        );
      });
    });

    describe('getStudiiLiteratura', () => {
      it('should return NewStudiiLiteratura for default StudiiLiteratura initial value', () => {
        const formGroup = service.createStudiiLiteraturaFormGroup(sampleWithNewData);

        const studiiLiteratura = service.getStudiiLiteratura(formGroup) as any;

        expect(studiiLiteratura).toMatchObject(sampleWithNewData);
      });

      it('should return NewStudiiLiteratura for empty StudiiLiteratura initial value', () => {
        const formGroup = service.createStudiiLiteraturaFormGroup();

        const studiiLiteratura = service.getStudiiLiteratura(formGroup) as any;

        expect(studiiLiteratura).toMatchObject({});
      });

      it('should return IStudiiLiteratura', () => {
        const formGroup = service.createStudiiLiteraturaFormGroup(sampleWithRequiredData);

        const studiiLiteratura = service.getStudiiLiteratura(formGroup) as any;

        expect(studiiLiteratura).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStudiiLiteratura should not enable id FormControl', () => {
        const formGroup = service.createStudiiLiteraturaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStudiiLiteratura should disable id FormControl', () => {
        const formGroup = service.createStudiiLiteraturaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
