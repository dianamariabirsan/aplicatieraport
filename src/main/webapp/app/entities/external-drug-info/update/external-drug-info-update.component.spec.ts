import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedicament } from 'app/entities/medicament/medicament.model';
import { MedicamentService } from 'app/entities/medicament/service/medicament.service';
import { ExternalDrugInfoService } from '../service/external-drug-info.service';
import { IExternalDrugInfo } from '../external-drug-info.model';
import { ExternalDrugInfoFormService } from './external-drug-info-form.service';

import { ExternalDrugInfoUpdateComponent } from './external-drug-info-update.component';

describe('ExternalDrugInfo Management Update Component', () => {
  let comp: ExternalDrugInfoUpdateComponent;
  let fixture: ComponentFixture<ExternalDrugInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let externalDrugInfoFormService: ExternalDrugInfoFormService;
  let externalDrugInfoService: ExternalDrugInfoService;
  let medicamentService: MedicamentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExternalDrugInfoUpdateComponent],
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
      .overrideTemplate(ExternalDrugInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExternalDrugInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    externalDrugInfoFormService = TestBed.inject(ExternalDrugInfoFormService);
    externalDrugInfoService = TestBed.inject(ExternalDrugInfoService);
    medicamentService = TestBed.inject(MedicamentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Medicament query and add missing value', () => {
      const medicament: IMedicament = { id: 27128 };
      const externalDrugInfo: IExternalDrugInfo = { id: 14099, medicament };

      const medicamentCollection: IMedicament[] = [{ id: 24081 }];
      jest.spyOn(medicamentService, 'query').mockReturnValue(of(new HttpResponse({ body: medicamentCollection })));
      const expectedCollection: IMedicament[] = [medicament, ...medicamentCollection];
      jest.spyOn(medicamentService, 'addMedicamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ externalDrugInfo });
      comp.ngOnInit();

      expect(medicamentService.query).toHaveBeenCalledWith({ sort: ['denumire,asc'] });
      expect(medicamentService.addMedicamentToCollectionIfMissing).toHaveBeenCalledWith(
        medicamentCollection,
        expect.objectContaining(medicament),
      );
      expect(comp.medicamentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const externalDrugInfo: IExternalDrugInfo = { id: 14099 };

      activatedRoute.data = of({ externalDrugInfo });
      comp.ngOnInit();

      expect(comp.externalDrugInfo).toEqual(externalDrugInfo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExternalDrugInfo>>();
      const externalDrugInfo = { id: 7370 };
      jest.spyOn(externalDrugInfoFormService, 'getExternalDrugInfo').mockReturnValue(externalDrugInfo);
      jest.spyOn(externalDrugInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ externalDrugInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: externalDrugInfo }));
      saveSubject.complete();

      // THEN
      expect(externalDrugInfoFormService.getExternalDrugInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(externalDrugInfoService.update).toHaveBeenCalledWith(expect.objectContaining(externalDrugInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExternalDrugInfo>>();
      const externalDrugInfo = { id: 7370 };
      jest.spyOn(externalDrugInfoFormService, 'getExternalDrugInfo').mockReturnValue({ id: null });
      jest.spyOn(externalDrugInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ externalDrugInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: externalDrugInfo }));
      saveSubject.complete();

      // THEN
      expect(externalDrugInfoFormService.getExternalDrugInfo).toHaveBeenCalled();
      expect(externalDrugInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExternalDrugInfo>>();
      const externalDrugInfo = { id: 7370 };
      jest.spyOn(externalDrugInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ externalDrugInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(externalDrugInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
