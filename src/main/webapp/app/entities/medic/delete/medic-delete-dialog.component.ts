import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedic } from '../medic.model';
import { MedicService } from '../service/medic.service';

@Component({
  templateUrl: './medic-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicDeleteDialogComponent {
  medic?: IMedic;

  protected medicService = inject(MedicService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
