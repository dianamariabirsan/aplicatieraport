import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AdministrareDetailComponent } from './administrare-detail.component';

describe('Administrare Management Detail Component', () => {
  let comp: AdministrareDetailComponent;
  let fixture: ComponentFixture<AdministrareDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdministrareDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./administrare-detail.component').then(m => m.AdministrareDetailComponent),
              resolve: { administrare: () => of({ id: 25740 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AdministrareDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdministrareDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load administrare on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AdministrareDetailComponent);

      // THEN
      expect(instance.administrare()).toEqual(expect.objectContaining({ id: 25740 }));
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
