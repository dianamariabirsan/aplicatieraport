import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IStudiiLiteratura } from '../studii-literatura.model';

@Component({
  selector: 'jhi-studii-literatura-detail',
  templateUrl: './studii-literatura-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class StudiiLiteraturaDetailComponent {
  studiiLiteratura = input<IStudiiLiteratura | null>(null);

  previousState(): void {
    window.history.back();
  }
}
