import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMedicament } from '../medicament.model';

@Component({
  selector: 'jhi-medicament-detail',
  templateUrl: './medicament-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MedicamentDetailComponent {
  medicament = input<IMedicament | null>(null);

  previousState(): void {
    window.history.back();
  }
}
