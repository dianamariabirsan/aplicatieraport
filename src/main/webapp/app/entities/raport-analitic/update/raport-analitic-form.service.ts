import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRaportAnalitic, NewRaportAnalitic } from '../raport-analitic.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRaportAnalitic for edit and NewRaportAnaliticFormGroupInput for create.
 */
type RaportAnaliticFormGroupInput = IRaportAnalitic | PartialWithRequiredKeyOf<NewRaportAnalitic>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRaportAnalitic | NewRaportAnalitic> = Omit<T, 'perioadaStart' | 'perioadaEnd'> & {
  perioadaStart?: string | null;
  perioadaEnd?: string | null;
};

type RaportAnaliticFormRawValue = FormValueOf<IRaportAnalitic>;

type NewRaportAnaliticFormRawValue = FormValueOf<NewRaportAnalitic>;

type RaportAnaliticFormDefaults = Pick<NewRaportAnalitic, 'id' | 'perioadaStart' | 'perioadaEnd'>;

type RaportAnaliticFormGroupContent = {
  id: FormControl<RaportAnaliticFormRawValue['id'] | NewRaportAnalitic['id']>;
  perioadaStart: FormControl<RaportAnaliticFormRawValue['perioadaStart']>;
  perioadaEnd: FormControl<RaportAnaliticFormRawValue['perioadaEnd']>;
  eficientaMedie: FormControl<RaportAnaliticFormRawValue['eficientaMedie']>;
  rataReactiiAdverse: FormControl<RaportAnaliticFormRawValue['rataReactiiAdverse']>;
  observatii: FormControl<RaportAnaliticFormRawValue['observatii']>;
  concluzii: FormControl<RaportAnaliticFormRawValue['concluzii']>;
  medicament: FormControl<RaportAnaliticFormRawValue['medicament']>;
  medic: FormControl<RaportAnaliticFormRawValue['medic']>;
};

export type RaportAnaliticFormGroup = FormGroup<RaportAnaliticFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RaportAnaliticFormService {
  createRaportAnaliticFormGroup(raportAnalitic: RaportAnaliticFormGroupInput = { id: null }): RaportAnaliticFormGroup {
    const raportAnaliticRawValue = this.convertRaportAnaliticToRaportAnaliticRawValue({
      ...this.getFormDefaults(),
      ...raportAnalitic,
    });
    return new FormGroup<RaportAnaliticFormGroupContent>({
      id: new FormControl(
        { value: raportAnaliticRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      perioadaStart: new FormControl(raportAnaliticRawValue.perioadaStart),
      perioadaEnd: new FormControl(raportAnaliticRawValue.perioadaEnd),
      eficientaMedie: new FormControl(raportAnaliticRawValue.eficientaMedie),
      rataReactiiAdverse: new FormControl(raportAnaliticRawValue.rataReactiiAdverse),
      observatii: new FormControl(raportAnaliticRawValue.observatii),
      concluzii: new FormControl(raportAnaliticRawValue.concluzii),
      medicament: new FormControl(raportAnaliticRawValue.medicament),
      medic: new FormControl(raportAnaliticRawValue.medic),
    });
  }

  getRaportAnalitic(form: RaportAnaliticFormGroup): IRaportAnalitic | NewRaportAnalitic {
    return this.convertRaportAnaliticRawValueToRaportAnalitic(
      form.getRawValue() as RaportAnaliticFormRawValue | NewRaportAnaliticFormRawValue,
    );
  }

  resetForm(form: RaportAnaliticFormGroup, raportAnalitic: RaportAnaliticFormGroupInput): void {
    const raportAnaliticRawValue = this.convertRaportAnaliticToRaportAnaliticRawValue({ ...this.getFormDefaults(), ...raportAnalitic });
    form.reset(
      {
        ...raportAnaliticRawValue,
        id: { value: raportAnaliticRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RaportAnaliticFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      perioadaStart: currentTime,
      perioadaEnd: currentTime,
    };
  }

  private convertRaportAnaliticRawValueToRaportAnalitic(
    rawRaportAnalitic: RaportAnaliticFormRawValue | NewRaportAnaliticFormRawValue,
  ): IRaportAnalitic | NewRaportAnalitic {
    return {
      ...rawRaportAnalitic,
      perioadaStart: dayjs(rawRaportAnalitic.perioadaStart, DATE_TIME_FORMAT),
      perioadaEnd: dayjs(rawRaportAnalitic.perioadaEnd, DATE_TIME_FORMAT),
    };
  }

  private convertRaportAnaliticToRaportAnaliticRawValue(
    raportAnalitic: IRaportAnalitic | (Partial<NewRaportAnalitic> & RaportAnaliticFormDefaults),
  ): RaportAnaliticFormRawValue | PartialWithRequiredKeyOf<NewRaportAnaliticFormRawValue> {
    return {
      ...raportAnalitic,
      perioadaStart: raportAnalitic.perioadaStart ? raportAnalitic.perioadaStart.format(DATE_TIME_FORMAT) : undefined,
      perioadaEnd: raportAnalitic.perioadaEnd ? raportAnalitic.perioadaEnd.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
