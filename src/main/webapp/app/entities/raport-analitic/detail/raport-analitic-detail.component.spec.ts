import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RaportAnaliticDetailComponent } from './raport-analitic-detail.component';

describe('RaportAnalitic Management Detail Component', () => {
  let comp: RaportAnaliticDetailComponent;
  let fixture: ComponentFixture<RaportAnaliticDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RaportAnaliticDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./raport-analitic-detail.component').then(m => m.RaportAnaliticDetailComponent),
              resolve: { raportAnalitic: () => of({ id: 6838 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RaportAnaliticDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RaportAnaliticDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load raportAnalitic on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RaportAnaliticDetailComponent);

      // THEN
      expect(instance.raportAnalitic()).toEqual(expect.objectContaining({ id: 6838 }));
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
