import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDecisionLog } from '../decision-log.model';

@Component({
  selector: 'jhi-decision-log-detail',
  templateUrl: './decision-log-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DecisionLogDetailComponent {
  decisionLog = input<IDecisionLog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
