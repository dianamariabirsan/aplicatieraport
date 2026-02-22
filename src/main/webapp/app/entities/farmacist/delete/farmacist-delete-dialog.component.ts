import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFarmacist } from '../farmacist.model';
import { FarmacistService } from '../service/farmacist.service';

@Component({
  templateUrl: './farmacist-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FarmacistDeleteDialogComponent {
  farmacist?: IFarmacist;

  protected farmacistService = inject(FarmacistService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.farmacistService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
