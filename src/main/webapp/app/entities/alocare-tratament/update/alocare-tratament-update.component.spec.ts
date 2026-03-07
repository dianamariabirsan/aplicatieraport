import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { IDecisionLog } from 'app/entities/decision-log/decision-log.model';
import { DecisionLogService } from 'app/entities/decision-log/service/decision-log.service';
import { IAlocareTratament } from '../alocare-tratament.model';
import { AlocareTratamentService } from '../service/alocare-tratament.service';
import { AlocareTratamentFormService } from './alocare-tratament-form.service';

import { AlocareTratamentUpdateComponent } from './alocare-tratament-update.component';

describe('AlocareTratament Management Update Component', () => {
  let comp: AlocareTratamentUpdateComponent;
  let fixture: ComponentFixture<AlocareTratamentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alocareTratamentFormService: AlocareTratamentFormService;
  let alocareTratamentService: AlocareTratamentService;
  let medicService: MedicService;
  let medicamentService: MedicamentService;
  let pacientService: PacientService;
  let decisionLogService: DecisionLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlocareTratamentUpdateComponent],
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
      .overrideTemplate(AlocareTratamentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlocareTratamentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alocareTratamentFormService = TestBed.inject(AlocareTratamentFormService);
    alocareTratamentService = TestBed.inject(AlocareTratamentService);
    medicService = TestBed.inject(MedicService);
    medicamentService = TestBed.inject(MedicamentService);
    pacientService = TestBed.inject(PacientService);
    decisionLogService = TestBed.inject(DecisionLogService);

    jest.spyOn(decisionLogService, 'queryByAlocareId').mockReturnValue(of(new HttpResponse<IDecisionLog[]>({ body: [] })));

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Medic query and add missing value', () => {
      const alocareTratament: IAlocareTratament = { id: 1228 };
      const medic: IMedic = { id: 29038 };
      alocareTratament.medic = medic;

      const medicCollection: IMedic[] = [{ id: 29038 }];
      jest.spyOn(medicService, 'query').mockReturnValue(of(new HttpResponse({ body: medicCollection })));
      const additionalMedics = [medic];
      const expectedCollection: IMedic[] = [...additionalMedics, ...medicCollection];
      jest.spyOn(medicService, 'addMedicToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alocareTratament });
      comp.ngOnInit();

      expect(medicService.query).toHaveBeenCalled();
      expect(medicService.addMedicToCollectionIfMissing).toHaveBeenCalledWith(
        medicCollection,
        ...additionalMedics.map(expect.objectContaining),
      );
      expect(comp.medicsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Medicament query and add missing value', () => {
      const alocareTratament: IAlocareTratament = { id: 1228 };
      const medicament: IMedicament = { id: 23749 };
      alocareTratament.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 23749 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alocareTratament });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(
        medicamentCollection,
        ...additionalMedicaments.map(expect.objectContaining),
      );
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Pacient query and add missing value', () => {
      const alocareTratament: IAlocareTratament = { id: 1228 };
      const pacient: IPacient = { id: 14998 };
      alocareTratament.pacient = pacient;

      const pacientCollection: IPacient[] = [{ id: 14998 }];
      jest.spyOn(pacientService, 'query').mockReturnValue(of(new HttpResponse({ body: pacientCollection })));
      const additionalPacients = [pacient];
      const expectedCollection: IPacient[] = [...additionalPacients, ...pacientCollection];
      jest.spyOn(pacientService, 'addPacientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alocareTratament });
      comp.ngOnInit();

      expect(pacientService.query).toHaveBeenCalled();
      expect(pacientService.addPacientToCollectionIfMissing).toHaveBeenCalledWith(
        pacientCollection,
        ...additionalPacients.map(expect.objectContaining),
      );
      expect(comp.pacientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const alocareTratament: IAlocareTratament = { id: 1228 };
      const medic: IMedic = { id: 29038 };
      alocareTratament.medic = medic;
      const medicament: IMedicament = { id: 23749 };
      alocareTratament.medicament = medicament;
      const pacient: IPacient = { id: 14998 };
      alocareTratament.pacient = pacient;

      activatedRoute.data = of({ alocareTratament });
      comp.ngOnInit();

      expect(comp.medicsSharedCollection).toContainEqual(medic);
      expect(comp.medicamentsSharedCollection).toContainEqual(medicament);
      expect(comp.pacientsSharedCollection).toContainEqual(pacient);
      expect(comp.alocareTratament).toEqual(alocareTratament);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlocareTratament>>();
      const alocareTratament = { id: 13651 };
      jest.spyOn(alocareTratamentFormService, 'getAlocareTratament').mockReturnValue(alocareTratament);
      jest.spyOn(alocareTratamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alocareTratament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alocareTratament }));
      saveSubject.complete();

      // THEN
      expect(alocareTratamentFormService.getAlocareTratament).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alocareTratamentService.update).toHaveBeenCalledWith(expect.objectContaining(alocareTratament));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlocareTratament>>();
      const alocareTratament = { id: 13651 };
      jest.spyOn(alocareTratamentFormService, 'getAlocareTratament').mockReturnValue({ id: null });
      jest.spyOn(alocareTratamentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alocareTratament: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alocareTratament }));
      saveSubject.complete();

      // THEN
      expect(alocareTratamentFormService.getAlocareTratament).toHaveBeenCalled();
      expect(alocareTratamentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlocareTratament>>();
      const alocareTratament = { id: 13651 };
      jest.spyOn(alocareTratamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alocareTratament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alocareTratamentService.update).toHaveBeenCalled();
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

    describe('compareMedicament', () => {
      it('should forward to medicamentService', () => {
        const entity = { id: 23749 };
        const entity2 = { id: 11631 };
        jest.spyOn(medicamentService, 'compareMedicament');
        comp.compareMedicament(entity, entity2);
        expect(medicamentService.compareMedicament).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePacient', () => {
      it('should forward to pacientService', () => {
        const entity = { id: 14998 };
        const entity2 = { id: 10827 };
        jest.spyOn(pacientService, 'comparePacient');
        comp.comparePacient(entity, entity2);
        expect(pacientService.comparePacient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });

  describe('medicament sync', () => {
    it('should auto-fill tratamentPropus when medicament changes', () => {
      activatedRoute.data = of({ alocareTratament: null });
      comp.ngOnInit();

      const medicament: IMedicament = { id: 1, denumire: 'Wegovy' };
      comp.editForm.get('medicament')?.setValue(medicament);

      expect(comp.editForm.get('tratamentPropus')?.value).toEqual('Wegovy');
    });

    it('should clear tratamentPropus when medicament is cleared', () => {
      activatedRoute.data = of({ alocareTratament: null });
      comp.ngOnInit();

      comp.editForm.get('medicament')?.setValue({ id: 1, denumire: 'Wegovy' });
      comp.editForm.get('medicament')?.setValue(null);

      expect(comp.editForm.get('tratamentPropus')?.value).toEqual('');
    });

    it('should set tratamentPropus from medicament when loading existing entity', () => {
      const alocareTratament: IAlocareTratament = {
        id: 1228,
        medicament: { id: 1, denumire: 'Ozempic' },
        tratamentPropus: 'old value',
      };

      activatedRoute.data = of({ alocareTratament });
      comp.ngOnInit();

      expect(comp.editForm.get('tratamentPropus')?.value).toEqual('Ozempic');
    });
  });
});
