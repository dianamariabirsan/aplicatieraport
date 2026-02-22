import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMonitorizare } from '../monitorizare.model';

@Component({
  selector: 'jhi-monitorizare-detail',
  templateUrl: './monitorizare-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MonitorizareDetailComponent {
  monitorizare = input<IMonitorizare | null>(null);

  previousState(): void {
    window.history.back();
  }
}
