import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { computed } from '@angular/core';

import SharedModule from 'app/shared/shared.module';
import { IPacient } from '../pacient.model';

@Component({
  selector: 'jhi-pacient-detail',
  templateUrl: './pacient-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PacientDetailComponent {
  pacient = input<IPacient | null>(null);
  readonly pacientRef = computed(() => this.pacient());
  previousState(): void {
    window.history.back();
  }
}
