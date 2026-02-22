import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IReactieAdversa } from '../reactie-adversa.model';

@Component({
  selector: 'jhi-reactie-adversa-detail',
  templateUrl: './reactie-adversa-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ReactieAdversaDetailComponent {
  reactieAdversa = input<IReactieAdversa | null>(null);

  previousState(): void {
    window.history.back();
  }
}
