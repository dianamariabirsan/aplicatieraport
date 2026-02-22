import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRaportAnalitic } from '../raport-analitic.model';
import { RaportAnaliticService } from '../service/raport-analitic.service';

@Component({
  templateUrl: './raport-analitic-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RaportAnaliticDeleteDialogComponent {
  raportAnalitic?: IRaportAnalitic;

  protected raportAnaliticService = inject(RaportAnaliticService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.raportAnaliticService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
