import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExternalDrugInfo } from 'app/entities/external-drug-info/external-drug-info.model';
import { ExternalDrugInfoService } from 'app/entities/external-drug-info/service/external-drug-info.service';
import { MedicamentService } from '../service/medicament.service';
import { IMedicament } from '../medicament.model';
import { MedicamentFormService } from './medicament-form.service';

import { MedicamentUpdateComponent } from './medicament-update.component';

describe('Medicament Management Update Component', () => {
  let comp: MedicamentUpdateComponent;
  let fixture: ComponentFixture<MedicamentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicamentFormService: MedicamentFormService;
  let medicamentService: MedicamentService;
  let externalDrugInfoService: ExternalDrugInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicamentUpdateComponent],
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
      .overrideTemplate(MedicamentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicamentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicamentFormService = TestBed.inject(MedicamentFormService);
    medicamentService = TestBed.inject(MedicamentService);
    externalDrugInfoService = TestBed.inject(ExternalDrugInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call infoExtern query and add missing value', () => {
      const medicament: IMedicament = { id: 11631 };
      const infoExtern: IExternalDrugInfo = { id: 7370 };
      medicament.infoExtern = infoExtern;

      const infoExternCollection: IExternalDrugInfo[] = [{ id: 7370 }];
      jest.spyOn(externalDrugInfoService, 'query').mockReturnValue(of(new HttpResponse({ body: infoExternCollection })));
      const expectedCollection: IExternalDrugInfo[] = [infoExtern, ...infoExternCollection];
      jest.spyOn(externalDrugInfoService, 'addExternalDrugInfoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      expect(externalDrugInfoService.query).toHaveBeenCalled();
      expect(externalDrugInfoService.addExternalDrugInfoToCollectionIfMissing).toHaveBeenCalledWith(infoExternCollection, infoExtern);
      expect(comp.infoExternsCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const medicament: IMedicament = { id: 11631 };
      const infoExtern: IExternalDrugInfo = { id: 7370 };
      medicament.infoExtern = infoExtern;

      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      expect(comp.infoExternsCollection).toContainEqual(infoExtern);
      expect(comp.medicament).toEqual(medicament);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicament>>();
      const medicament = { id: 23749 };
      jest.spyOn(medicamentFormService, 'getMedicament').mockReturnValue(medicament);
      jest.spyOn(medicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicament }));
      saveSubject.complete();

      // THEN
      expect(medicamentFormService.getMedicament).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicamentService.update).toHaveBeenCalledWith(expect.objectContaining(medicament));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicament>>();
      const medicament = { id: 23749 };
      jest.spyOn(medicamentFormService, 'getMedicament').mockReturnValue({ id: null });
      jest.spyOn(medicamentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicament: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicament }));
      saveSubject.complete();

      // THEN
      expect(medicamentFormService.getMedicament).toHaveBeenCalled();
      expect(medicamentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicament>>();
      const medicament = { id: 23749 };
      jest.spyOn(medicamentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicament });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicamentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExternalDrugInfo', () => {
      it('should forward to externalDrugInfoService', () => {
        const entity = { id: 7370 };
        const entity2 = { id: 14099 };
        jest.spyOn(externalDrugInfoService, 'compareExternalDrugInfo');
        comp.compareExternalDrugInfo(entity, entity2);
        expect(externalDrugInfoService.compareExternalDrugInfo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
