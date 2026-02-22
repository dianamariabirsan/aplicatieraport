import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAlocareTratament } from '../alocare-tratament.model';

@Component({
  selector: 'jhi-alocare-tratament-detail',
  templateUrl: './alocare-tratament-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AlocareTratamentDetailComponent {
  alocareTratament = input<IAlocareTratament | null>(null);

  previousState(): void {
    window.history.back();
  }
}
