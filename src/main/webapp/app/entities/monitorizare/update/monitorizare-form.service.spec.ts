import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../monitorizare.test-samples';

import { MonitorizareFormService } from './monitorizare-form.service';

describe('Monitorizare Form Service', () => {
  let service: MonitorizareFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MonitorizareFormService);
  });

  describe('Service methods', () => {
    describe('createMonitorizareFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMonitorizareFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataInstant: expect.any(Object),
            tensiuneSist: expect.any(Object),
            tensiuneDiast: expect.any(Object),
            puls: expect.any(Object),
            glicemie: expect.any(Object),
            scorEficacitate: expect.any(Object),
            comentarii: expect.any(Object),
            pacient: expect.any(Object),
          }),
        );
      });

      it('passing IMonitorizare should create a new form with FormGroup', () => {
        const formGroup = service.createMonitorizareFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataInstant: expect.any(Object),
            tensiuneSist: expect.any(Object),
            tensiuneDiast: expect.any(Object),
            puls: expect.any(Object),
            glicemie: expect.any(Object),
            scorEficacitate: expect.any(Object),
            comentarii: expect.any(Object),
            pacient: expect.any(Object),
          }),
        );
      });
    });

    describe('getMonitorizare', () => {
      it('should return NewMonitorizare for default Monitorizare initial value', () => {
        const formGroup = service.createMonitorizareFormGroup(sampleWithNewData);

        const monitorizare = service.getMonitorizare(formGroup) as any;

        expect(monitorizare).toMatchObject(sampleWithNewData);
      });

      it('should return NewMonitorizare for empty Monitorizare initial value', () => {
        const formGroup = service.createMonitorizareFormGroup();

        const monitorizare = service.getMonitorizare(formGroup) as any;

        expect(monitorizare).toMatchObject({});
      });

      it('should return IMonitorizare', () => {
        const formGroup = service.createMonitorizareFormGroup(sampleWithRequiredData);

        const monitorizare = service.getMonitorizare(formGroup) as any;

        expect(monitorizare).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMonitorizare should not enable id FormControl', () => {
        const formGroup = service.createMonitorizareFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMonitorizare should disable id FormControl', () => {
        const formGroup = service.createMonitorizareFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
