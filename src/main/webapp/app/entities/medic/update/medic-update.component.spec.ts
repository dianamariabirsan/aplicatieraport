import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MedicService } from '../service/medic.service';
import { IMedic } from '../medic.model';
import { MedicFormService } from './medic-form.service';

import { MedicUpdateComponent } from './medic-update.component';

describe('Medic Management Update Component', () => {
  let comp: MedicUpdateComponent;
  let fixture: ComponentFixture<MedicUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicFormService: MedicFormService;
  let medicService: MedicService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicUpdateComponent],
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
      .overrideTemplate(MedicUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicFormService = TestBed.inject(MedicFormService);
    medicService = TestBed.inject(MedicService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const medic: IMedic = { id: 10974 };

      activatedRoute.data = of({ medic });
      comp.ngOnInit();

      expect(comp.medic).toEqual(medic);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedic>>();
      const medic = { id: 29038 };
      jest.spyOn(medicFormService, 'getMedic').mockReturnValue(medic);
      jest.spyOn(medicService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medic }));
      saveSubject.complete();

      // THEN
      expect(medicFormService.getMedic).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicService.update).toHaveBeenCalledWith(expect.objectContaining(medic));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedic>>();
      const medic = { id: 29038 };
      jest.spyOn(medicFormService, 'getMedic').mockReturnValue({ id: null });
      jest.spyOn(medicService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medic: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medic }));
      saveSubject.complete();

      // THEN
      expect(medicFormService.getMedic).toHaveBeenCalled();
      expect(medicService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedic>>();
      const medic = { id: 29038 };
      jest.spyOn(medicService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
