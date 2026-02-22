import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FarmacistDetailComponent } from './farmacist-detail.component';

describe('Farmacist Management Detail Component', () => {
  let comp: FarmacistDetailComponent;
  let fixture: ComponentFixture<FarmacistDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FarmacistDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./farmacist-detail.component').then(m => m.FarmacistDetailComponent),
              resolve: { farmacist: () => of({ id: 13357 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FarmacistDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FarmacistDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load farmacist on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FarmacistDetailComponent);

      // THEN
      expect(instance.farmacist()).toEqual(expect.objectContaining({ id: 13357 }));
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
