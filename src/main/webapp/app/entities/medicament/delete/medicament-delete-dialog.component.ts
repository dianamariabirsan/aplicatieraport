import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMedicament } from '../medicament.model';
import { MedicamentService } from '../service/medicament.service';

@Component({
  templateUrl: './medicament-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MedicamentDeleteDialogComponent {
  medicament?: IMedicament;

  protected medicamentService = inject(MedicamentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicamentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
