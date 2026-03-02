import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { IFarmacist } from 'app/entities/farmacist/farmacist.model';
import { FarmacistService } from 'app/entities/farmacist/service/farmacist.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IAdministrare } from '../administrare.model';
import { AdministrareService } from '../service/administrare.service';
import { AdministrareFormService } from './administrare-form.service';

import { AdministrareUpdateComponent } from './administrare-update.component';

describe('Administrare Management Update Component', () => {
  let comp: AdministrareUpdateComponent;
  let fixture: ComponentFixture<AdministrareUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let administrareFormService: AdministrareFormService;
  let administrareService: AdministrareService;
  let pacientService: PacientService;
  let farmacistService: FarmacistService;
  let medicamentService: MedicamentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdministrareUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AdministrareUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdministrareUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    administrareFormService = TestBed.inject(AdministrareFormService);
    administrareService = TestBed.inject(AdministrareService);
    pacientService = TestBed.inject(PacientService);
    farmacistService = TestBed.inject(FarmacistService);
    medicamentService = TestBed.inject(MedicamentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pacient query and add missing value', () => {
      const administrare: IAdministrare = { id: 22046 };
      const pacient: IPacient = { id: 14998 };
      administrare.pacient = pacient;

      const pacientCollection: IPacient[] = [{ id: 14998 }];
      jest.spyOn(pacientService, 'query').mockReturnValue(of(new HttpResponse({ body: pacientCollection })));
      const additionalPacients = [pacient];
      const expectedCollection: IPacient[] = [...additionalPacients, ...pacientCollection];
      jest.spyOn(pacientService, 'addPacientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ administrare });
      comp.ngOnInit();

      expect(pacientService.query).toHaveBeenCalled();
      expect(pacientService.addPacientToCollectionIfMissing).toHaveBeenCalledWith(
        pacientCollection,
        ...additionalPacients.map(expect.objectContaining),
      );
      expect(comp.pacientsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Farmacist query and add missing value', () => {
      const administrare: IAdministrare = { id: 22046 };
      const farmacist: IFarmacist = { id: 13357 };
      administrare.farmacist = farmacist;

      const farmacistCollection: IFarmacist[] = [{ id: 13357 }];
      jest.spyOn(farmacistService, 'query').mockReturnValue(of(new HttpResponse({ body: farmacistCollection })));
      const additionalFarmacists = [farmacist];
      const expectedCollection: IFarmacist[] = [...additionalFarmacists, ...farmacistCollection];
      jest.spyOn(farmacistService, 'addFarmacistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ administrare });
      comp.ngOnInit();

      expect(farmacistService.query).toHaveBeenCalled();
      expect(farmacistService.addFarmacistToCollectionIfMissing).toHaveBeenCalledWith(
        farmacistCollection,
        ...additionalFarmacists.map(expect.objectContaining),
      );
      expect(comp.farmacistsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Medicament query and add missing value', () => {
      const administrare: IAdministrare = { id: 22046 };
      const medicament: IMedicament = { id: 9901, denumire: 'Wegovy' };
      administrare.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 9901, denumire: 'Wegovy' }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ administrare });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(
        medicamentCollection,
        ...additionalMedicaments.map(expect.objectContaining),
      );
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const administrare: IAdministrare = { id: 22046 };
      const pacient: IPacient = { id: 14998 };
      administrare.pacient = pacient;
      const farmacist: IFarmacist = { id: 13357 };
      administrare.farmacist = farmacist;

      activatedRoute.data = of({ administrare });
      comp.ngOnInit();

      expect(comp.pacientsSharedCollection).toContainEqual(pacient);
      expect(comp.farmacistsSharedCollection).toContainEqual(farmacist);
      expect(comp.administrare).toEqual(administrare);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrare>>();
      const administrare = { id: 25740 };
      jest.spyOn(administrareFormService, 'getAdministrare').mockReturnValue(administrare);
      jest.spyOn(administrareService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrare });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: administrare }));
      saveSubject.complete();

      // THEN
      expect(administrareFormService.getAdministrare).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(administrareService.update).toHaveBeenCalledWith(expect.objectContaining(administrare));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrare>>();
      const administrare = { id: 25740 };
      jest.spyOn(administrareFormService, 'getAdministrare').mockReturnValue({ id: null });
      jest.spyOn(administrareService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrare: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: administrare }));
      saveSubject.complete();

      // THEN
      expect(administrareFormService.getAdministrare).toHaveBeenCalled();
      expect(administrareService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdministrare>>();
      const administrare = { id: 25740 };
      jest.spyOn(administrareService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ administrare });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(administrareService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePacient', () => {
      it('should forward to pacientService', () => {
        const entity = { id: 14998 };
        const entity2 = { id: 10827 };
        jest.spyOn(pacientService, 'comparePacient');
        comp.comparePacient(entity, entity2);
        expect(pacientService.comparePacient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFarmacist', () => {
      it('should forward to farmacistService', () => {
        const entity = { id: 13357 };
        const entity2 = { id: 23159 };
        jest.spyOn(farmacistService, 'compareFarmacist');
        comp.compareFarmacist(entity, entity2);
        expect(farmacistService.compareFarmacist).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMedicament', () => {
      it('should forward to medicamentService', () => {
        const entity = { id: 9901 };
        const entity2 = { id: 9902 };
        jest.spyOn(medicamentService, 'compareMedicament');
        comp.compareMedicament(entity, entity2);
        expect(medicamentService.compareMedicament).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
