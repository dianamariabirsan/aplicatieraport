import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStudiiLiteratura } from '../studii-literatura.model';
import { StudiiLiteraturaService } from '../service/studii-literatura.service';

@Component({
  templateUrl: './studii-literatura-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StudiiLiteraturaDeleteDialogComponent {
  studiiLiteratura?: IStudiiLiteratura;

  protected studiiLiteraturaService = inject(StudiiLiteraturaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studiiLiteraturaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
