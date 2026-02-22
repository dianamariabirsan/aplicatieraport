import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../administrare.test-samples';

import { AdministrareFormService } from './administrare-form.service';

describe('Administrare Form Service', () => {
  let service: AdministrareFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdministrareFormService);
  });

  describe('Service methods', () => {
    describe('createAdministrareFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdministrareFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataAdministrare: expect.any(Object),
            tipTratament: expect.any(Object),
            doza: expect.any(Object),
            unitate: expect.any(Object),
            modAdministrare: expect.any(Object),
            observatii: expect.any(Object),
            pacient: expect.any(Object),
            farmacist: expect.any(Object),
          }),
        );
      });

      it('passing IAdministrare should create a new form with FormGroup', () => {
        const formGroup = service.createAdministrareFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataAdministrare: expect.any(Object),
            tipTratament: expect.any(Object),
            doza: expect.any(Object),
            unitate: expect.any(Object),
            modAdministrare: expect.any(Object),
            observatii: expect.any(Object),
            pacient: expect.any(Object),
            farmacist: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdministrare', () => {
      it('should return NewAdministrare for default Administrare initial value', () => {
        const formGroup = service.createAdministrareFormGroup(sampleWithNewData);

        const administrare = service.getAdministrare(formGroup) as any;

        expect(administrare).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdministrare for empty Administrare initial value', () => {
        const formGroup = service.createAdministrareFormGroup();

        const administrare = service.getAdministrare(formGroup) as any;

        expect(administrare).toMatchObject({});
      });

      it('should return IAdministrare', () => {
        const formGroup = service.createAdministrareFormGroup(sampleWithRequiredData);

        const administrare = service.getAdministrare(formGroup) as any;

        expect(administrare).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdministrare should not enable id FormControl', () => {
        const formGroup = service.createAdministrareFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdministrare should disable id FormControl', () => {
        const formGroup = service.createAdministrareFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
