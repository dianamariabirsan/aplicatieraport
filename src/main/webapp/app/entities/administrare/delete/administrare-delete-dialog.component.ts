import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAdministrare } from '../administrare.model';
import { AdministrareService } from '../service/administrare.service';

@Component({
  templateUrl: './administrare-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AdministrareDeleteDialogComponent {
  administrare?: IAdministrare;

  protected administrareService = inject(AdministrareService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.administrareService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
