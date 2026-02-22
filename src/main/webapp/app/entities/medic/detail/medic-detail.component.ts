import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMedic } from '../medic.model';

@Component({
  selector: 'jhi-medic-detail',
  templateUrl: './medic-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MedicDetailComponent {
  medic = input<IMedic | null>(null);

  previousState(): void {
    window.history.back();
  }
}
