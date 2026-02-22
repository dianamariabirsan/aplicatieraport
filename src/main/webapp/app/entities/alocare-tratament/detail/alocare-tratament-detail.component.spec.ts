import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AlocareTratamentDetailComponent } from './alocare-tratament-detail.component';

describe('AlocareTratament Management Detail Component', () => {
  let comp: AlocareTratamentDetailComponent;
  let fixture: ComponentFixture<AlocareTratamentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlocareTratamentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./alocare-tratament-detail.component').then(m => m.AlocareTratamentDetailComponent),
              resolve: { alocareTratament: () => of({ id: 13651 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AlocareTratamentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlocareTratamentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load alocareTratament on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AlocareTratamentDetailComponent);

      // THEN
      expect(instance.alocareTratament()).toEqual(expect.objectContaining({ id: 13651 }));
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
