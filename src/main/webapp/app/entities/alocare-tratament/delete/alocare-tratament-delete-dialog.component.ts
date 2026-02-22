import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAlocareTratament } from '../alocare-tratament.model';
import { AlocareTratamentService } from '../service/alocare-tratament.service';

@Component({
  templateUrl: './alocare-tratament-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AlocareTratamentDeleteDialogComponent {
  alocareTratament?: IAlocareTratament;

  protected alocareTratamentService = inject(AlocareTratamentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alocareTratamentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
