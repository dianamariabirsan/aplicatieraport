import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reactie-adversa.test-samples';

import { ReactieAdversaFormService } from './reactie-adversa-form.service';

describe('ReactieAdversa Form Service', () => {
  let service: ReactieAdversaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReactieAdversaFormService);
  });

  describe('Service methods', () => {
    describe('createReactieAdversaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReactieAdversaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataRaportare: expect.any(Object),
            severitate: expect.any(Object),
            descriere: expect.any(Object),
            evolutie: expect.any(Object),
            raportatDe: expect.any(Object),
            medicament: expect.any(Object),
            pacient: expect.any(Object),
          }),
        );
      });

      it('passing IReactieAdversa should create a new form with FormGroup', () => {
        const formGroup = service.createReactieAdversaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataRaportare: expect.any(Object),
            severitate: expect.any(Object),
            descriere: expect.any(Object),
            evolutie: expect.any(Object),
            raportatDe: expect.any(Object),
            medicament: expect.any(Object),
            pacient: expect.any(Object),
          }),
        );
      });
    });

    describe('getReactieAdversa', () => {
      it('should return NewReactieAdversa for default ReactieAdversa initial value', () => {
        const formGroup = service.createReactieAdversaFormGroup(sampleWithNewData);

        const reactieAdversa = service.getReactieAdversa(formGroup) as any;

        expect(reactieAdversa).toMatchObject(sampleWithNewData);
      });

      it('should return NewReactieAdversa for empty ReactieAdversa initial value', () => {
        const formGroup = service.createReactieAdversaFormGroup();

        const reactieAdversa = service.getReactieAdversa(formGroup) as any;

        expect(reactieAdversa).toMatchObject({});
      });

      it('should return IReactieAdversa', () => {
        const formGroup = service.createReactieAdversaFormGroup(sampleWithRequiredData);

        const reactieAdversa = service.getReactieAdversa(formGroup) as any;

        expect(reactieAdversa).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReactieAdversa should not enable id FormControl', () => {
        const formGroup = service.createReactieAdversaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReactieAdversa should disable id FormControl', () => {
        const formGroup = service.createReactieAdversaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
