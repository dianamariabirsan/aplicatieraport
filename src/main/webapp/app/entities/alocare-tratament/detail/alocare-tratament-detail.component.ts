import { Component, OnInit, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAlocareTratament } from '../alocare-tratament.model';
import { IDecisionLog } from 'app/entities/decision-log/decision-log.model';
import { DecisionLogService } from 'app/entities/decision-log/service/decision-log.service';

@Component({
  selector: 'jhi-alocare-tratament-detail',
  templateUrl: './alocare-tratament-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AlocareTratamentDetailComponent implements OnInit {
  alocareTratament = input<IAlocareTratament | null>(null);
  decisionLogs: IDecisionLog[] = [];

  protected decisionLogService = inject(DecisionLogService);

  ngOnInit(): void {
    const id = this.alocareTratament()?.id;
    if (id) {
      this.decisionLogService.queryByAlocareId(id).subscribe({
        next: res => (this.decisionLogs = res.body ?? []),
      });
    }
  }

  previousState(): void {
    window.history.back();
  }
}
