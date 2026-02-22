import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IRaportAnalitic } from '../raport-analitic.model';

@Component({
  selector: 'jhi-raport-analitic-detail',
  templateUrl: './raport-analitic-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class RaportAnaliticDetailComponent {
  raportAnalitic = input<IRaportAnalitic | null>(null);

  previousState(): void {
    window.history.back();
  }
}
