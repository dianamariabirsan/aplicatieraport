import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPacient } from '../pacient.model';
import { PacientService } from '../service/pacient.service';

@Component({
  templateUrl: './pacient-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PacientDeleteDialogComponent {
  pacient?: IPacient;

  protected pacientService = inject(PacientService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pacientService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
