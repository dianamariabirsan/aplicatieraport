import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFarmacist, NewFarmacist } from '../farmacist.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFarmacist for edit and NewFarmacistFormGroupInput for create.
 */
type FarmacistFormGroupInput = IFarmacist | PartialWithRequiredKeyOf<NewFarmacist>;

type FarmacistFormDefaults = Pick<NewFarmacist, 'id'>;

type FarmacistFormGroupContent = {
  id: FormControl<IFarmacist['id'] | NewFarmacist['id']>;
  nume: FormControl<IFarmacist['nume']>;
  prenume: FormControl<IFarmacist['prenume']>;
  farmacie: FormControl<IFarmacist['farmacie']>;
  email: FormControl<IFarmacist['email']>;
  telefon: FormControl<IFarmacist['telefon']>;
};

export type FarmacistFormGroup = FormGroup<FarmacistFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FarmacistFormService {
  createFarmacistFormGroup(farmacist: FarmacistFormGroupInput = { id: null }): FarmacistFormGroup {
    const farmacistRawValue = {
      ...this.getFormDefaults(),
      ...farmacist,
    };
    return new FormGroup<FarmacistFormGroupContent>({
      id: new FormControl(
        { value: farmacistRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nume: new FormControl(farmacistRawValue.nume, {
        validators: [Validators.required],
      }),
      prenume: new FormControl(farmacistRawValue.prenume, {
        validators: [Validators.required],
      }),
      farmacie: new FormControl(farmacistRawValue.farmacie, {
        validators: [Validators.required],
      }),
      email: new FormControl(farmacistRawValue.email, {
        validators: [Validators.required],
      }),
      telefon: new FormControl(farmacistRawValue.telefon),
    });
  }

  getFarmacist(form: FarmacistFormGroup): IFarmacist | NewFarmacist {
    return form.getRawValue() as IFarmacist | NewFarmacist;
  }

  resetForm(form: FarmacistFormGroup, farmacist: FarmacistFormGroupInput): void {
    const farmacistRawValue = { ...this.getFormDefaults(), ...farmacist };
    form.reset(
      {
        ...farmacistRawValue,
        id: { value: farmacistRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FarmacistFormDefaults {
    return {
      id: null,
    };
  }
}
