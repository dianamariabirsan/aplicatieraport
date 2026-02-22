import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FarmacistService } from '../service/farmacist.service';
import { IFarmacist } from '../farmacist.model';
import { FarmacistFormService } from './farmacist-form.service';

import { FarmacistUpdateComponent } from './farmacist-update.component';

describe('Farmacist Management Update Component', () => {
  let comp: FarmacistUpdateComponent;
  let fixture: ComponentFixture<FarmacistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let farmacistFormService: FarmacistFormService;
  let farmacistService: FarmacistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FarmacistUpdateComponent],
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
      .overrideTemplate(FarmacistUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FarmacistUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    farmacistFormService = TestBed.inject(FarmacistFormService);
    farmacistService = TestBed.inject(FarmacistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const farmacist: IFarmacist = { id: 23159 };

      activatedRoute.data = of({ farmacist });
      comp.ngOnInit();

      expect(comp.farmacist).toEqual(farmacist);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFarmacist>>();
      const farmacist = { id: 13357 };
      jest.spyOn(farmacistFormService, 'getFarmacist').mockReturnValue(farmacist);
      jest.spyOn(farmacistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ farmacist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: farmacist }));
      saveSubject.complete();

      // THEN
      expect(farmacistFormService.getFarmacist).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(farmacistService.update).toHaveBeenCalledWith(expect.objectContaining(farmacist));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFarmacist>>();
      const farmacist = { id: 13357 };
      jest.spyOn(farmacistFormService, 'getFarmacist').mockReturnValue({ id: null });
      jest.spyOn(farmacistService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ farmacist: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: farmacist }));
      saveSubject.complete();

      // THEN
      expect(farmacistFormService.getFarmacist).toHaveBeenCalled();
      expect(farmacistService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFarmacist>>();
      const farmacist = { id: 13357 };
      jest.spyOn(farmacistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ farmacist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(farmacistService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
