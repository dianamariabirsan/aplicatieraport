import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { IRaportAnalitic } from '../raport-analitic.model';
import { RaportAnaliticService } from '../service/raport-analitic.service';
import { RaportAnaliticFormService } from './raport-analitic-form.service';

import { RaportAnaliticUpdateComponent } from './raport-analitic-update.component';

describe('RaportAnalitic Management Update Component', () => {
  let comp: RaportAnaliticUpdateComponent;
  let fixture: ComponentFixture<RaportAnaliticUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let raportAnaliticFormService: RaportAnaliticFormService;
  let raportAnaliticService: RaportAnaliticService;
  let medicamentService: MedicamentService;
  let medicService: MedicService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RaportAnaliticUpdateComponent],
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
      .overrideTemplate(RaportAnaliticUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RaportAnaliticUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    raportAnaliticFormService = TestBed.inject(RaportAnaliticFormService);
    raportAnaliticService = TestBed.inject(RaportAnaliticService);
    medicamentService = TestBed.inject(MedicamentService);
    medicService = TestBed.inject(MedicService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Medicament query and add missing value', () => {
      const raportAnalitic: IRaportAnalitic = { id: 16031 };
      const medicament: IMedicament = { id: 23749 };
      raportAnalitic.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 23749 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ raportAnalitic });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(
        medicamentCollection,
        ...additionalMedicaments.map(expect.objectContaining),
      );
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Medic query and add missing value', () => {
      const raportAnalitic: IRaportAnalitic = { id: 16031 };
      const medic: IMedic = { id: 29038 };
      raportAnalitic.medic = medic;

      const medicCollection: IMedic[] = [{ id: 29038 }];
      jest.spyOn(medicService, 'query').mockReturnValue(of(new HttpResponse({ body: medicCollection })));
      const additionalMedics = [medic];
      const expectedCollection: IMedic[] = [...additionalMedics, ...medicCollection];
      jest.spyOn(medicService, 'addMedicToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ raportAnalitic });
      comp.ngOnInit();

      expect(medicService.query).toHaveBeenCalled();
      expect(medicService.addMedicToCollectionIfMissing).toHaveBeenCalledWith(
        medicCollection,
        ...additionalMedics.map(expect.objectContaining),
      );
      expect(comp.medicsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const raportAnalitic: IRaportAnalitic = { id: 16031 };
      const medicament: IMedicament = { id: 23749 };
      raportAnalitic.medicament = medicament;
      const medic: IMedic = { id: 29038 };
      raportAnalitic.medic = medic;

      activatedRoute.data = of({ raportAnalitic });
      comp.ngOnInit();

      expect(comp.medicamentsSharedCollection).toContainEqual(medicament);
      expect(comp.medicsSharedCollection).toContainEqual(medic);
      expect(comp.raportAnalitic).toEqual(raportAnalitic);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRaportAnalitic>>();
      const raportAnalitic = { id: 6838 };
      jest.spyOn(raportAnaliticFormService, 'getRaportAnalitic').mockReturnValue(raportAnalitic);
      jest.spyOn(raportAnaliticService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raportAnalitic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raportAnalitic }));
      saveSubject.complete();

      // THEN
      expect(raportAnaliticFormService.getRaportAnalitic).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(raportAnaliticService.update).toHaveBeenCalledWith(expect.objectContaining(raportAnalitic));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRaportAnalitic>>();
      const raportAnalitic = { id: 6838 };
      jest.spyOn(raportAnaliticFormService, 'getRaportAnalitic').mockReturnValue({ id: null });
      jest.spyOn(raportAnaliticService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raportAnalitic: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raportAnalitic }));
      saveSubject.complete();

      // THEN
      expect(raportAnaliticFormService.getRaportAnalitic).toHaveBeenCalled();
      expect(raportAnaliticService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRaportAnalitic>>();
      const raportAnalitic = { id: 6838 };
      jest.spyOn(raportAnaliticService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raportAnalitic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(raportAnaliticService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedicament', () => {
      it('should forward to medicamentService', () => {
        const entity = { id: 23749 };
        const entity2 = { id: 11631 };
        jest.spyOn(medicamentService, 'compareMedicament');
        comp.compareMedicament(entity, entity2);
        expect(medicamentService.compareMedicament).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMedic', () => {
      it('should forward to medicService', () => {
        const entity = { id: 29038 };
        const entity2 = { id: 10974 };
        jest.spyOn(medicService, 'compareMedic');
        comp.compareMedic(entity, entity2);
        expect(medicService.compareMedic).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
