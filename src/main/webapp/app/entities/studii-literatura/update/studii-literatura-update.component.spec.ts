import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { StudiiLiteraturaService } from '../service/studii-literatura.service';
import { IStudiiLiteratura } from '../studii-literatura.model';
import { StudiiLiteraturaFormService } from './studii-literatura-form.service';

import { StudiiLiteraturaUpdateComponent } from './studii-literatura-update.component';

describe('StudiiLiteratura Management Update Component', () => {
  let comp: StudiiLiteraturaUpdateComponent;
  let fixture: ComponentFixture<StudiiLiteraturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studiiLiteraturaFormService: StudiiLiteraturaFormService;
  let studiiLiteraturaService: StudiiLiteraturaService;
  let medicamentService: MedicamentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StudiiLiteraturaUpdateComponent],
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
      .overrideTemplate(StudiiLiteraturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudiiLiteraturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    studiiLiteraturaFormService = TestBed.inject(StudiiLiteraturaFormService);
    studiiLiteraturaService = TestBed.inject(StudiiLiteraturaService);
    medicamentService = TestBed.inject(MedicamentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Medicament query and add missing value', () => {
      const studiiLiteratura: IStudiiLiteratura = { id: 20506 };
      const medicament: IMedicament = { id: 23749 };
      studiiLiteratura.medicament = medicament;

      const medicamentCollection: IMedicament[] = [{ id: 23749 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const additionalMedicaments = [medicament];
      const expectedCollection: IMedicament[] = [...additionalMedicaments, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ studiiLiteratura });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalled();
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(
        medicamentCollection,
        ...additionalMedicaments.map(expect.objectContaining),
      );
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const studiiLiteratura: IStudiiLiteratura = { id: 20506 };
      const medicament: IMedicament = { id: 23749 };
      studiiLiteratura.medicament = medicament;

      activatedRoute.data = of({ studiiLiteratura });
      comp.ngOnInit();

      expect(comp.medicamentsSharedCollection).toContainEqual(medicament);
      expect(comp.studiiLiteratura).toEqual(studiiLiteratura);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudiiLiteratura>>();
      const studiiLiteratura = { id: 13423 };
      jest.spyOn(studiiLiteraturaFormService, 'getStudiiLiteratura').mockReturnValue(studiiLiteratura);
      jest.spyOn(studiiLiteraturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studiiLiteratura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studiiLiteratura }));
      saveSubject.complete();

      // THEN
      expect(studiiLiteraturaFormService.getStudiiLiteratura).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(studiiLiteraturaService.update).toHaveBeenCalledWith(expect.objectContaining(studiiLiteratura));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudiiLiteratura>>();
      const studiiLiteratura = { id: 13423 };
      jest.spyOn(studiiLiteraturaFormService, 'getStudiiLiteratura').mockReturnValue({ id: null });
      jest.spyOn(studiiLiteraturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studiiLiteratura: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studiiLiteratura }));
      saveSubject.complete();

      // THEN
      expect(studiiLiteraturaFormService.getStudiiLiteratura).toHaveBeenCalled();
      expect(studiiLiteraturaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudiiLiteratura>>();
      const studiiLiteratura = { id: 13423 };
      jest.spyOn(studiiLiteraturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studiiLiteratura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(studiiLiteraturaService.update).toHaveBeenCalled();
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
  });
});
