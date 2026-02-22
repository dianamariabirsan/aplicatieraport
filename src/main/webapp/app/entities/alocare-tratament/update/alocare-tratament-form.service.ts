import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlocareTratament, NewAlocareTratament } from '../alocare-tratament.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlocareTratament for edit and NewAlocareTratamentFormGroupInput for create.
 */
type AlocareTratamentFormGroupInput = IAlocareTratament | PartialWithRequiredKeyOf<NewAlocareTratament>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlocareTratament | NewAlocareTratament> = Omit<T, 'dataDecizie'> & {
  dataDecizie?: string | null;
};

type AlocareTratamentFormRawValue = FormValueOf<IAlocareTratament>;

type NewAlocareTratamentFormRawValue = FormValueOf<NewAlocareTratament>;

type AlocareTratamentFormDefaults = Pick<NewAlocareTratament, 'id' | 'dataDecizie' | 'decizieValidata'>;

type AlocareTratamentFormGroupContent = {
  id: FormControl<AlocareTratamentFormRawValue['id'] | NewAlocareTratament['id']>;
  dataDecizie: FormControl<AlocareTratamentFormRawValue['dataDecizie']>;
  tratamentPropus: FormControl<AlocareTratamentFormRawValue['tratamentPropus']>;
  motivDecizie: FormControl<AlocareTratamentFormRawValue['motivDecizie']>;
  scorDecizie: FormControl<AlocareTratamentFormRawValue['scorDecizie']>;
  decizieValidata: FormControl<AlocareTratamentFormRawValue['decizieValidata']>;
  medic: FormControl<AlocareTratamentFormRawValue['medic']>;
  medicament: FormControl<AlocareTratamentFormRawValue['medicament']>;
  pacient: FormControl<AlocareTratamentFormRawValue['pacient']>;
};

export type AlocareTratamentFormGroup = FormGroup<AlocareTratamentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlocareTratamentFormService {
  createAlocareTratamentFormGroup(alocareTratament: AlocareTratamentFormGroupInput = { id: null }): AlocareTratamentFormGroup {
    const alocareTratamentRawValue = this.convertAlocareTratamentToAlocareTratamentRawValue({
      ...this.getFormDefaults(),
      ...alocareTratament,
    });
    return new FormGroup<AlocareTratamentFormGroupContent>({
      id: new FormControl(
        { value: alocareTratamentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataDecizie: new FormControl(alocareTratamentRawValue.dataDecizie, {
        validators: [Validators.required],
      }),
      tratamentPropus: new FormControl(alocareTratamentRawValue.tratamentPropus, {
        validators: [Validators.required],
      }),
      motivDecizie: new FormControl(alocareTratamentRawValue.motivDecizie),
      scorDecizie: new FormControl(alocareTratamentRawValue.scorDecizie),
      decizieValidata: new FormControl(alocareTratamentRawValue.decizieValidata),
      medic: new FormControl(alocareTratamentRawValue.medic),
      medicament: new FormControl(alocareTratamentRawValue.medicament),
      pacient: new FormControl(alocareTratamentRawValue.pacient),
    });
  }

  getAlocareTratament(form: AlocareTratamentFormGroup): IAlocareTratament | NewAlocareTratament {
    return this.convertAlocareTratamentRawValueToAlocareTratament(
      form.getRawValue() as AlocareTratamentFormRawValue | NewAlocareTratamentFormRawValue,
    );
  }

  resetForm(form: AlocareTratamentFormGroup, alocareTratament: AlocareTratamentFormGroupInput): void {
    const alocareTratamentRawValue = this.convertAlocareTratamentToAlocareTratamentRawValue({
      ...this.getFormDefaults(),
      ...alocareTratament,
    });
    form.reset(
      {
        ...alocareTratamentRawValue,
        id: { value: alocareTratamentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlocareTratamentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataDecizie: currentTime,
      decizieValidata: false,
    };
  }

  private convertAlocareTratamentRawValueToAlocareTratament(
    rawAlocareTratament: AlocareTratamentFormRawValue | NewAlocareTratamentFormRawValue,
  ): IAlocareTratament | NewAlocareTratament {
    return {
      ...rawAlocareTratament,
      dataDecizie: dayjs(rawAlocareTratament.dataDecizie, DATE_TIME_FORMAT),
    };
  }

  private convertAlocareTratamentToAlocareTratamentRawValue(
    alocareTratament: IAlocareTratament | (Partial<NewAlocareTratament> & AlocareTratamentFormDefaults),
  ): AlocareTratamentFormRawValue | PartialWithRequiredKeyOf<NewAlocareTratamentFormRawValue> {
    return {
      ...alocareTratament,
      dataDecizie: alocareTratament.dataDecizie ? alocareTratament.dataDecizie.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
