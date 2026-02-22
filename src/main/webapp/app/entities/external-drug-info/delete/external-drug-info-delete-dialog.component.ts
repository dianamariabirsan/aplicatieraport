import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExternalDrugInfo } from '../external-drug-info.model';
import { ExternalDrugInfoService } from '../service/external-drug-info.service';

@Component({
  templateUrl: './external-drug-info-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExternalDrugInfoDeleteDialogComponent {
  externalDrugInfo?: IExternalDrugInfo;

  protected externalDrugInfoService = inject(ExternalDrugInfoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.externalDrugInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
