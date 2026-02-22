import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { IFarmacist } from 'app/entities/farmacist/farmacist.model';
import { FarmacistService } from 'app/entities/farmacist/service/farmacist.service';
import { IPacient } from '../pacient.model';
import { PacientService } from '../service/pacient.service';
import { PacientFormService } from './pacient-form.service';

import { PacientUpdateComponent } from './pacient-update.component';

describe('Pacient Management Update Component', () => {
  let comp: PacientUpdateComponent;
  let fixture: ComponentFixture<PacientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pacientFormService: PacientFormService;
  let pacientService: PacientService;
  let medicService: MedicService;
  let farmacistService: FarmacistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PacientUpdateComponent],
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
      .overrideTemplate(PacientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PacientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pacientFormService = TestBed.inject(PacientFormService);
    pacientService = TestBed.inject(PacientService);
    medicService = TestBed.inject(MedicService);
    farmacistService = TestBed.inject(FarmacistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Medic query and add missing value', () => {
      const pacient: IPacient = { id: 10827 };
      const medic: IMedic = { id: 29038 };
      pacient.medic = medic;

      const medicCollection: IMedic[] = [{ id: 29038 }];
      jest.spyOn(medicService, 'query').mockReturnValue(of(new HttpResponse({ body: medicCollection })));
      const additionalMedics = [medic];
      const expectedCollection: IMedic[] = [...additionalMedics, ...medicCollection];
      jest.spyOn(medicService, 'addMedicToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pacient });
      comp.ngOnInit();

      expect(medicService.query).toHaveBeenCalled();
      expect(medicService.addMedicToCollectionIfMissing).toHaveBeenCalledWith(
        medicCollection,
        ...additionalMedics.map(expect.objectContaining),
      );
      expect(comp.medicsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Farmacist query and add missing value', () => {
      const pacient: IPacient = { id: 10827 };
      const farmacist: IFarmacist = { id: 13357 };
      pacient.farmacist = farmacist;

      const farmacistCollection: IFarmacist[] = [{ id: 13357 }];
      jest.spyOn(farmacistService, 'query').mockReturnValue(of(new HttpResponse({ body: farmacistCollection })));
      const additionalFarmacists = [farmacist];
      const expectedCollection: IFarmacist[] = [...additionalFarmacists, ...farmacistCollection];
      jest.spyOn(farmacistService, 'addFarmacistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pacient });
      comp.ngOnInit();

      expect(farmacistService.query).toHaveBeenCalled();
      expect(farmacistService.addFarmacistToCollectionIfMissing).toHaveBeenCalledWith(
        farmacistCollection,
        ...additionalFarmacists.map(expect.objectContaining),
      );
      expect(comp.farmacistsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const pacient: IPacient = { id: 10827 };
      const medic: IMedic = { id: 29038 };
      pacient.medic = medic;
      const farmacist: IFarmacist = { id: 13357 };
      pacient.farmacist = farmacist;

      activatedRoute.data = of({ pacient });
      comp.ngOnInit();

      expect(comp.medicsSharedCollection).toContainEqual(medic);
      expect(comp.farmacistsSharedCollection).toContainEqual(farmacist);
      expect(comp.pacient).toEqual(pacient);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPacient>>();
      const pacient = { id: 14998 };
      jest.spyOn(pacientFormService, 'getPacient').mockReturnValue(pacient);
      jest.spyOn(pacientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pacient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pacient }));
      saveSubject.complete();

      // THEN
      expect(pacientFormService.getPacient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pacientService.update).toHaveBeenCalledWith(expect.objectContaining(pacient));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPacient>>();
      const pacient = { id: 14998 };
      jest.spyOn(pacientFormService, 'getPacient').mockReturnValue({ id: null });
      jest.spyOn(pacientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pacient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pacient }));
      saveSubject.complete();

      // THEN
      expect(pacientFormService.getPacient).toHaveBeenCalled();
      expect(pacientService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPacient>>();
      const pacient = { id: 14998 };
      jest.spyOn(pacientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pacient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pacientService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedic', () => {
      it('should forward to medicService', () => {
        const entity = { id: 29038 };
        const entity2 = { id: 10974 };
        jest.spyOn(medicService, 'compareMedic');
        comp.compareMedic(entity, entity2);
        expect(medicService.compareMedic).toHaveBeenCalledWith(entity, entity2);
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
  });
});
