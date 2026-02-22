import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PacientDetailComponent } from './pacient-detail.component';

describe('Pacient Management Detail Component', () => {
  let comp: PacientDetailComponent;
  let fixture: ComponentFixture<PacientDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PacientDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./pacient-detail.component').then(m => m.PacientDetailComponent),
              resolve: { pacient: () => of({ id: 14998 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PacientDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PacientDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load pacient on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PacientDetailComponent);

      // THEN
      expect(instance.pacient()).toEqual(expect.objectContaining({ id: 14998 }));
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
