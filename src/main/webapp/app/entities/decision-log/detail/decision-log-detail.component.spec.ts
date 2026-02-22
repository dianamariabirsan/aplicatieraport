import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DecisionLogDetailComponent } from './decision-log-detail.component';

describe('DecisionLog Management Detail Component', () => {
  let comp: DecisionLogDetailComponent;
  let fixture: ComponentFixture<DecisionLogDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DecisionLogDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./decision-log-detail.component').then(m => m.DecisionLogDetailComponent),
              resolve: { decisionLog: () => of({ id: 11733 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DecisionLogDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load decisionLog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DecisionLogDetailComponent);

      // THEN
      expect(instance.decisionLog()).toEqual(expect.objectContaining({ id: 11733 }));
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
