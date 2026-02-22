import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MedicDetailComponent } from './medic-detail.component';

describe('Medic Management Detail Component', () => {
  let comp: MedicDetailComponent;
  let fixture: ComponentFixture<MedicDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedicDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./medic-detail.component').then(m => m.MedicDetailComponent),
              resolve: { medic: () => of({ id: 29038 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MedicDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load medic on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MedicDetailComponent);

      // THEN
      expect(instance.medic()).toEqual(expect.objectContaining({ id: 29038 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
