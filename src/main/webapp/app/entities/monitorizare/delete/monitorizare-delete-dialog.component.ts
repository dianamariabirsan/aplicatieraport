import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMonitorizare } from '../monitorizare.model';
import { MonitorizareService } from '../service/monitorizare.service';

@Component({
  templateUrl: './monitorizare-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MonitorizareDeleteDialogComponent {
  monitorizare?: IMonitorizare;

  protected monitorizareService = inject(MonitorizareService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monitorizareService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
