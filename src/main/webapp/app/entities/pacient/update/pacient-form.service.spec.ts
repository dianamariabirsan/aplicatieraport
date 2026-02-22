import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pacient.test-samples';

import { PacientFormService } from './pacient-form.service';

describe('Pacient Form Service', () => {
  let service: PacientFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PacientFormService);
  });

  describe('Service methods', () => {
    describe('createPacientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPacientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nume: expect.any(Object),
            prenume: expect.any(Object),
            sex: expect.any(Object),
            varsta: expect.any(Object),
            greutate: expect.any(Object),
            inaltime: expect.any(Object),
            circumferintaAbdominala: expect.any(Object),
            cnp: expect.any(Object),
            comorbiditati: expect.any(Object),
            gradSedentarism: expect.any(Object),
            istoricTratament: expect.any(Object),
            toleranta: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
            medic: expect.any(Object),
            farmacist: expect.any(Object),
          }),
        );
      });

      it('passing IPacient should create a new form with FormGroup', () => {
        const formGroup = service.createPacientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nume: expect.any(Object),
            prenume: expect.any(Object),
            sex: expect.any(Object),
            varsta: expect.any(Object),
            greutate: expect.any(Object),
            inaltime: expect.any(Object),
            circumferintaAbdominala: expect.any(Object),
            cnp: expect.any(Object),
            comorbiditati: expect.any(Object),
            gradSedentarism: expect.any(Object),
            istoricTratament: expect.any(Object),
            toleranta: expect.any(Object),
            email: expect.any(Object),
            telefon: expect.any(Object),
            medic: expect.any(Object),
            farmacist: expect.any(Object),
          }),
        );
      });
    });

    describe('getPacient', () => {
      it('should return NewPacient for default Pacient initial value', () => {
        const formGroup = service.createPacientFormGroup(sampleWithNewData);

        const pacient = service.getPacient(formGroup) as any;

        expect(pacient).toMatchObject(sampleWithNewData);
      });

      it('should return NewPacient for empty Pacient initial value', () => {
        const formGroup = service.createPacientFormGroup();

        const pacient = service.getPacient(formGroup) as any;

        expect(pacient).toMatchObject({});
      });

      it('should return IPacient', () => {
        const formGroup = service.createPacientFormGroup(sampleWithRequiredData);

        const pacient = service.getPacient(formGroup) as any;

        expect(pacient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPacient should not enable id FormControl', () => {
        const formGroup = service.createPacientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPacient should disable id FormControl', () => {
        const formGroup = service.createPacientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
