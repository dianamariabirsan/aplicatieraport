import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../raport-analitic.test-samples';

import { RaportAnaliticFormService } from './raport-analitic-form.service';

describe('RaportAnalitic Form Service', () => {
  let service: RaportAnaliticFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RaportAnaliticFormService);
  });

  describe('Service methods', () => {
    describe('createRaportAnaliticFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRaportAnaliticFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            perioadaStart: expect.any(Object),
            perioadaEnd: expect.any(Object),
            eficientaMedie: expect.any(Object),
            rataReactiiAdverse: expect.any(Object),
            observatii: expect.any(Object),
            concluzii: expect.any(Object),
            medicament: expect.any(Object),
            medic: expect.any(Object),
          }),
        );
      });

      it('passing IRaportAnalitic should create a new form with FormGroup', () => {
        const formGroup = service.createRaportAnaliticFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            perioadaStart: expect.any(Object),
            perioadaEnd: expect.any(Object),
            eficientaMedie: expect.any(Object),
            rataReactiiAdverse: expect.any(Object),
            observatii: expect.any(Object),
            concluzii: expect.any(Object),
            medicament: expect.any(Object),
            medic: expect.any(Object),
          }),
        );
      });
    });

    describe('getRaportAnalitic', () => {
      it('should return NewRaportAnalitic for default RaportAnalitic initial value', () => {
        const formGroup = service.createRaportAnaliticFormGroup(sampleWithNewData);

        const raportAnalitic = service.getRaportAnalitic(formGroup) as any;

        expect(raportAnalitic).toMatchObject(sampleWithNewData);
      });

      it('should return NewRaportAnalitic for empty RaportAnalitic initial value', () => {
        const formGroup = service.createRaportAnaliticFormGroup();

        const raportAnalitic = service.getRaportAnalitic(formGroup) as any;

        expect(raportAnalitic).toMatchObject({});
      });

      it('should return IRaportAnalitic', () => {
        const formGroup = service.createRaportAnaliticFormGroup(sampleWithRequiredData);

        const raportAnalitic = service.getRaportAnalitic(formGroup) as any;

        expect(raportAnalitic).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRaportAnalitic should not enable id FormControl', () => {
        const formGroup = service.createRaportAnaliticFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRaportAnalitic should disable id FormControl', () => {
        const formGroup = service.createRaportAnaliticFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
