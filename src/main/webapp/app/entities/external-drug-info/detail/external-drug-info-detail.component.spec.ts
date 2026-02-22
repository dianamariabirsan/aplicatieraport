import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExternalDrugInfoDetailComponent } from './external-drug-info-detail.component';

describe('ExternalDrugInfo Management Detail Component', () => {
  let comp: ExternalDrugInfoDetailComponent;
  let fixture: ComponentFixture<ExternalDrugInfoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExternalDrugInfoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./external-drug-info-detail.component').then(m => m.ExternalDrugInfoDetailComponent),
              resolve: { externalDrugInfo: () => of({ id: 7370 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ExternalDrugInfoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExternalDrugInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load externalDrugInfo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ExternalDrugInfoDetailComponent);

      // THEN
      expect(instance.externalDrugInfo()).toEqual(expect.objectContaining({ id: 7370 }));
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
