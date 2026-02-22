import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAdministrare, NewAdministrare } from '../administrare.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdministrare for edit and NewAdministrareFormGroupInput for create.
 */
type AdministrareFormGroupInput = IAdministrare | PartialWithRequiredKeyOf<NewAdministrare>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAdministrare | NewAdministrare> = Omit<T, 'dataAdministrare'> & {
  dataAdministrare?: string | null;
};

type AdministrareFormRawValue = FormValueOf<IAdministrare>;

type NewAdministrareFormRawValue = FormValueOf<NewAdministrare>;

type AdministrareFormDefaults = Pick<NewAdministrare, 'id' | 'dataAdministrare'>;

type AdministrareFormGroupContent = {
  id: FormControl<AdministrareFormRawValue['id'] | NewAdministrare['id']>;
  dataAdministrare: FormControl<AdministrareFormRawValue['dataAdministrare']>;
  tipTratament: FormControl<AdministrareFormRawValue['tipTratament']>;
  doza: FormControl<AdministrareFormRawValue['doza']>;
  unitate: FormControl<AdministrareFormRawValue['unitate']>;
  modAdministrare: FormControl<AdministrareFormRawValue['modAdministrare']>;
  observatii: FormControl<AdministrareFormRawValue['observatii']>;
  pacient: FormControl<AdministrareFormRawValue['pacient']>;
  farmacist: FormControl<AdministrareFormRawValue['farmacist']>;
};

export type AdministrareFormGroup = FormGroup<AdministrareFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdministrareFormService {
  createAdministrareFormGroup(administrare: AdministrareFormGroupInput = { id: null }): AdministrareFormGroup {
    const administrareRawValue = this.convertAdministrareToAdministrareRawValue({
      ...this.getFormDefaults(),
      ...administrare,
    });
    return new FormGroup<AdministrareFormGroupContent>({
      id: new FormControl(
        { value: administrareRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataAdministrare: new FormControl(administrareRawValue.dataAdministrare, {
        validators: [Validators.required],
      }),
      tipTratament: new FormControl(administrareRawValue.tipTratament, {
        validators: [Validators.required],
      }),
      doza: new FormControl(administrareRawValue.doza),
      unitate: new FormControl(administrareRawValue.unitate),
      modAdministrare: new FormControl(administrareRawValue.modAdministrare),
      observatii: new FormControl(administrareRawValue.observatii),
      pacient: new FormControl(administrareRawValue.pacient),
      farmacist: new FormControl(administrareRawValue.farmacist),
    });
  }

  getAdministrare(form: AdministrareFormGroup): IAdministrare | NewAdministrare {
    return this.convertAdministrareRawValueToAdministrare(form.getRawValue() as AdministrareFormRawValue | NewAdministrareFormRawValue);
  }

  resetForm(form: AdministrareFormGroup, administrare: AdministrareFormGroupInput): void {
    const administrareRawValue = this.convertAdministrareToAdministrareRawValue({ ...this.getFormDefaults(), ...administrare });
    form.reset(
      {
        ...administrareRawValue,
        id: { value: administrareRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdministrareFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataAdministrare: currentTime,
    };
  }

  private convertAdministrareRawValueToAdministrare(
    rawAdministrare: AdministrareFormRawValue | NewAdministrareFormRawValue,
  ): IAdministrare | NewAdministrare {
    return {
      ...rawAdministrare,
      dataAdministrare: dayjs(rawAdministrare.dataAdministrare, DATE_TIME_FORMAT),
    };
  }

  private convertAdministrareToAdministrareRawValue(
    administrare: IAdministrare | (Partial<NewAdministrare> & AdministrareFormDefaults),
  ): AdministrareFormRawValue | PartialWithRequiredKeyOf<NewAdministrareFormRawValue> {
    return {
      ...administrare,
      dataAdministrare: administrare.dataAdministrare ? administrare.dataAdministrare.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
