import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { MonitorizareService } from '../service/monitorizare.service';
import { IMonitorizare } from '../monitorizare.model';
import { MonitorizareFormService } from './monitorizare-form.service';

import { MonitorizareUpdateComponent } from './monitorizare-update.component';

describe('Monitorizare Management Update Component', () => {
  let comp: MonitorizareUpdateComponent;
  let fixture: ComponentFixture<MonitorizareUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monitorizareFormService: MonitorizareFormService;
  let monitorizareService: MonitorizareService;
  let pacientService: PacientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitorizareUpdateComponent],
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
      .overrideTemplate(MonitorizareUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonitorizareUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monitorizareFormService = TestBed.inject(MonitorizareFormService);
    monitorizareService = TestBed.inject(MonitorizareService);
    pacientService = TestBed.inject(PacientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pacient query and add missing value', () => {
      const monitorizare: IMonitorizare = { id: 2402 };
      const pacient: IPacient = { id: 14998 };
      monitorizare.pacient = pacient;

      const pacientCollection: IPacient[] = [{ id: 14998 }];
      jest.spyOn(pacientService, 'query').mockReturnValue(of(new HttpResponse({ body: pacientCollection })));
      const additionalPacients = [pacient];
      const expectedCollection: IPacient[] = [...additionalPacients, ...pacientCollection];
      jest.spyOn(pacientService, 'addPacientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ monitorizare });
      comp.ngOnInit();

      expect(pacientService.query).toHaveBeenCalled();
      expect(pacientService.addPacientToCollectionIfMissing).toHaveBeenCalledWith(
        pacientCollection,
        ...additionalPacients.map(expect.objectContaining),
      );
      expect(comp.pacientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const monitorizare: IMonitorizare = { id: 2402 };
      const pacient: IPacient = { id: 14998 };
      monitorizare.pacient = pacient;

      activatedRoute.data = of({ monitorizare });
      comp.ngOnInit();

      expect(comp.pacientsSharedCollection).toContainEqual(pacient);
      expect(comp.monitorizare).toEqual(monitorizare);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitorizare>>();
      const monitorizare = { id: 28896 };
      jest.spyOn(monitorizareFormService, 'getMonitorizare').mockReturnValue(monitorizare);
      jest.spyOn(monitorizareService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitorizare });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitorizare }));
      saveSubject.complete();

      // THEN
      expect(monitorizareFormService.getMonitorizare).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monitorizareService.update).toHaveBeenCalledWith(expect.objectContaining(monitorizare));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitorizare>>();
      const monitorizare = { id: 28896 };
      jest.spyOn(monitorizareFormService, 'getMonitorizare').mockReturnValue({ id: null });
      jest.spyOn(monitorizareService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitorizare: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitorizare }));
      saveSubject.complete();

      // THEN
      expect(monitorizareFormService.getMonitorizare).toHaveBeenCalled();
      expect(monitorizareService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitorizare>>();
      const monitorizare = { id: 28896 };
      jest.spyOn(monitorizareService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitorizare });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monitorizareService.update).toHaveBeenCalled();
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
  });
});
