import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReactieAdversaDetailComponent } from './reactie-adversa-detail.component';

describe('ReactieAdversa Management Detail Component', () => {
  let comp: ReactieAdversaDetailComponent;
  let fixture: ComponentFixture<ReactieAdversaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactieAdversaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./reactie-adversa-detail.component').then(m => m.ReactieAdversaDetailComponent),
              resolve: { reactieAdversa: () => of({ id: 1613 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReactieAdversaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReactieAdversaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load reactieAdversa on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReactieAdversaDetailComponent);

      // THEN
      expect(instance.reactieAdversa()).toEqual(expect.objectContaining({ id: 1613 }));
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
