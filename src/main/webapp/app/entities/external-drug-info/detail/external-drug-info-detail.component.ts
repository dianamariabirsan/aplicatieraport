import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IExternalDrugInfo } from '../external-drug-info.model';

@Component({
  selector: 'jhi-external-drug-info-detail',
  templateUrl: './external-drug-info-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ExternalDrugInfoDetailComponent {
  externalDrugInfo = input<IExternalDrugInfo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
