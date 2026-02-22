import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { IReactieAdversa } from '../reactie-adversa.model';
import { ReactieAdversaService } from '../service/reactie-adversa.service';
import { ReactieAdversaFormService } from './reactie-adversa-form.service';

import { ReactieAdversaUpdateComponent } from './reactie-adversa-update.component';

describe('ReactieAdversa Management Update Component', () => {
  let comp: ReactieAdversaUpdateComponent;
  let fixture: ComponentFixture<ReactieAdversaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reactieAdversaFormService: ReactieAdversaFormService;
  let reactieAdversaService: ReactieAdversaService;
  let medicamentService: MedicamentService;
  let pacientService: PacientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactieAdversaUpdateComponent],
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
      .overrideTemplate(ReactieAdversaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReactieAdversaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reactieAdversaFormService = TestBed.inject(ReactieAdversaFormService);
    reactieAdversaService = TestBed.inject(ReactieAdversaService);
    medicamentService = TestBed.inject(MedicamentService);
    pacientService = TestBed.inject(PacientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Medicament query and add missing value', () => {
      const reactieAdversa: IReactieAdversa = { id: 23843 };
      const medicament: IMedicament = { id: 23749 };
      reactieAdversa.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 23749 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reactieAdversa });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(
        medicamentCollection,
        ...additionalMedicaments.map(expect.objectContaining),
      );
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Pacient query and add missing value', () => {
      const reactieAdversa: IReactieAdversa = { id: 23843 };
      const pacient: IPacient = { id: 14998 };
      reactieAdversa.pacient = pacient;

      const pacientCollection: IPacient[] = [{ id: 14998 }];
      jest.spyOn(pacientService, 'query').mockReturnValue(of(new HttpResponse({ body: pacientCollection })));
      const additionalPacients = [pacient];
      const expectedCollection: IPacient[] = [...additionalPacients, ...pacientCollection];
      jest.spyOn(pacientService, 'addPacientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reactieAdversa });
      comp.ngOnInit();

      expect(pacientService.query).toHaveBeenCalled();
      expect(pacientService.addPacientToCollectionIfMissing).toHaveBeenCalledWith(
        pacientCollection,
        ...additionalPacients.map(expect.objectContaining),
      );
      expect(comp.pacientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const reactieAdversa: IReactieAdversa = { id: 23843 };
      const medicament: IMedicament = { id: 23749 };
      reactieAdversa.medicament = medicament;
      const pacient: IPacient = { id: 14998 };
      reactieAdversa.pacient = pacient;

      activatedRoute.data = of({ reactieAdversa });
      comp.ngOnInit();

      expect(comp.medicamentsSharedCollection).toContainEqual(medicament);
      expect(comp.pacientsSharedCollection).toContainEqual(pacient);
      expect(comp.reactieAdversa).toEqual(reactieAdversa);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReactieAdversa>>();
      const reactieAdversa = { id: 1613 };
      jest.spyOn(reactieAdversaFormService, 'getReactieAdversa').mockReturnValue(reactieAdversa);
      jest.spyOn(reactieAdversaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reactieAdversa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reactieAdversa }));
      saveSubject.complete();

      // THEN
      expect(reactieAdversaFormService.getReactieAdversa).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reactieAdversaService.update).toHaveBeenCalledWith(expect.objectContaining(reactieAdversa));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReactieAdversa>>();
      const reactieAdversa = { id: 1613 };
      jest.spyOn(reactieAdversaFormService, 'getReactieAdversa').mockReturnValue({ id: null });
      jest.spyOn(reactieAdversaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reactieAdversa: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reactieAdversa }));
      saveSubject.complete();

      // THEN
      expect(reactieAdversaFormService.getReactieAdversa).toHaveBeenCalled();
      expect(reactieAdversaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReactieAdversa>>();
      const reactieAdversa = { id: 1613 };
      jest.spyOn(reactieAdversaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reactieAdversa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reactieAdversaService.update).toHaveBeenCalled();
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
});
