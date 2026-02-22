import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StudiiLiteraturaDetailComponent } from './studii-literatura-detail.component';

describe('StudiiLiteratura Management Detail Component', () => {
  let comp: StudiiLiteraturaDetailComponent;
  let fixture: ComponentFixture<StudiiLiteraturaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudiiLiteraturaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./studii-literatura-detail.component').then(m => m.StudiiLiteraturaDetailComponent),
              resolve: { studiiLiteratura: () => of({ id: 13423 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StudiiLiteraturaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StudiiLiteraturaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load studiiLiteratura on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StudiiLiteraturaDetailComponent);

      // THEN
      expect(instance.studiiLiteratura()).toEqual(expect.objectContaining({ id: 13423 }));
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
