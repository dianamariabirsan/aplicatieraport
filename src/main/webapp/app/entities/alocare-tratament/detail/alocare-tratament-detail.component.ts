import { Component, OnInit, inject, input } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import dayjs from 'dayjs/esm';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAlocareTratament } from '../alocare-tratament.model';
import { IDecisionLog } from 'app/entities/decision-log/decision-log.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Component({
  selector: 'jhi-alocare-tratament-detail',
  templateUrl: './alocare-tratament-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AlocareTratamentDetailComponent implements OnInit {
  alocareTratament = input<IAlocareTratament | null>(null);
  decisionLogs: IDecisionLog[] = [];

  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  ngOnInit(): void {
    const id = this.alocareTratament()?.id;
    if (id) {
      const url = this.applicationConfigService.getEndpointFor(`api/alocare-trataments/${id}/decision-logs`);
      this.http.get<any[]>(url).subscribe({
        next: logs =>
          (this.decisionLogs = logs.map(log => ({
            ...log,
            timestamp: log.timestamp ? dayjs(log.timestamp) : null,
          }))),
      });
    }
  }

  previousState(): void {
    window.history.back();
  }
}
