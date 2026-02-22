import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MonitorizareDetailComponent } from './monitorizare-detail.component';

describe('Monitorizare Management Detail Component', () => {
  let comp: MonitorizareDetailComponent;
  let fixture: ComponentFixture<MonitorizareDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonitorizareDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./monitorizare-detail.component').then(m => m.MonitorizareDetailComponent),
              resolve: { monitorizare: () => of({ id: 28896 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MonitorizareDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitorizareDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load monitorizare on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MonitorizareDetailComponent);

      // THEN
      expect(instance.monitorizare()).toEqual(expect.objectContaining({ id: 28896 }));
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
