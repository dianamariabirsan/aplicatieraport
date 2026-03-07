import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LANGUAGES } from 'app/config/language.constants';
import { Authority } from 'app/config/authority.constants';
import { UserManagementService } from '../user-management/service/user-management.service';
import { IUser } from '../user-management/user-management.model';

type StaffRole = 'MEDIC' | 'FARMACIST';

@Component({
  selector: 'jhi-staff-provisioning',
  templateUrl: './staff-provisioning.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule],
})
export default class StaffProvisioningComponent {
  languages = LANGUAGES;
  isSaving = signal(false);
  successMessage: string | null = null;
  errorMessage: string | null = null;

  editForm = new FormGroup({
    login: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(50)],
    }),
    lastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(50)],
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    activated: new FormControl(true, {
      nonNullable: true,
    }),
    langKey: new FormControl('ro', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    role: new FormControl<StaffRole>('MEDIC', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  protected readonly userService = inject(UserManagementService);

  createUser(): void {
    if (this.editForm.invalid || this.isSaving()) {
      this.editForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    this.successMessage = null;
    this.errorMessage = null;

    const formValue = this.editForm.getRawValue();
    const authority = formValue.role === 'FARMACIST' ? Authority.FARMACIST : Authority.MEDIC;

    const payload: IUser = {
      id: null,
      login: formValue.login,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      email: formValue.email,
      activated: formValue.activated,
      langKey: formValue.langKey,
      authorities: [authority],
    };

    this.userService.create(payload).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.successMessage = `Contul de tip ${formValue.role} a fost creat. Utilizatorul va primi emailul de activare / creare cont.`;
        this.editForm.reset({
          login: '',
          firstName: '',
          lastName: '',
          email: '',
          activated: true,
          langKey: 'ro',
          role: formValue.role,
        });
      },
      error: (error: HttpErrorResponse) => {
        this.isSaving.set(false);
        this.errorMessage =
          error.error?.detail ??
          error.error?.message ??
          error.message ??
          'Crearea contului a eșuat. Verifică loginul, emailul și rolul selectat.';
      },
    });
  }
}
