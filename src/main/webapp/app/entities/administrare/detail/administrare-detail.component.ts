import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAdministrare } from '../administrare.model';

@Component({
  selector: 'jhi-administrare-detail',
  templateUrl: './administrare-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AdministrareDetailComponent {
  administrare = input<IAdministrare | null>(null);

  previousState(): void {
    window.history.back();
  }
}
