import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMedic, NewMedic } from '../medic.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedic for edit and NewMedicFormGroupInput for create.
 */
type MedicFormGroupInput = IMedic | PartialWithRequiredKeyOf<NewMedic>;

type MedicFormDefaults = Pick<NewMedic, 'id'>;

type MedicFormGroupContent = {
  id: FormControl<IMedic['id'] | NewMedic['id']>;
  nume: FormControl<IMedic['nume']>;
  prenume: FormControl<IMedic['prenume']>;
  specializare: FormControl<IMedic['specializare']>;
  email: FormControl<IMedic['email']>;
  telefon: FormControl<IMedic['telefon']>;
  cabinet: FormControl<IMedic['cabinet']>;
};

export type MedicFormGroup = FormGroup<MedicFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicFormService {
  createMedicFormGroup(medic: MedicFormGroupInput = { id: null }): MedicFormGroup {
    const medicRawValue = {
      ...this.getFormDefaults(),
      ...medic,
    };
    return new FormGroup<MedicFormGroupContent>({
      id: new FormControl(
        { value: medicRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nume: new FormControl(medicRawValue.nume, {
        validators: [Validators.required],
      }),
      prenume: new FormControl(medicRawValue.prenume, {
        validators: [Validators.required],
      }),
      specializare: new FormControl(medicRawValue.specializare, {
        validators: [Validators.required],
      }),
      email: new FormControl(medicRawValue.email, {
        validators: [Validators.required],
      }),
      telefon: new FormControl(medicRawValue.telefon),
      cabinet: new FormControl(medicRawValue.cabinet),
    });
  }

  getMedic(form: MedicFormGroup): IMedic | NewMedic {
    return form.getRawValue() as IMedic | NewMedic;
  }

  resetForm(form: MedicFormGroup, medic: MedicFormGroupInput): void {
    const medicRawValue = { ...this.getFormDefaults(), ...medic };
    form.reset(
      {
        ...medicRawValue,
        id: { value: medicRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicFormDefaults {
    return {
      id: null,
    };
  }
}
