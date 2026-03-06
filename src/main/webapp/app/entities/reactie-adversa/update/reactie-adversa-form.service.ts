import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReactieAdversa, NewReactieAdversa } from '../reactie-adversa.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReactieAdversa for edit and NewReactieAdversaFormGroupInput for create.
 */
type ReactieAdversaFormGroupInput = IReactieAdversa | PartialWithRequiredKeyOf<NewReactieAdversa>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReactieAdversa | NewReactieAdversa> = Omit<T, 'dataRaportare'> & {
  dataRaportare?: string | null;
};

type ReactieAdversaFormRawValue = FormValueOf<IReactieAdversa>;

type NewReactieAdversaFormRawValue = FormValueOf<NewReactieAdversa>;

type ReactieAdversaFormDefaults = Pick<NewReactieAdversa, 'id' | 'dataRaportare'>;

type ReactieAdversaFormGroupContent = {
  id: FormControl<ReactieAdversaFormRawValue['id'] | NewReactieAdversa['id']>;
  dataRaportare: FormControl<ReactieAdversaFormRawValue['dataRaportare']>;
  severitate: FormControl<ReactieAdversaFormRawValue['severitate']>;
  descriere: FormControl<ReactieAdversaFormRawValue['descriere']>;
  evolutie: FormControl<ReactieAdversaFormRawValue['evolutie']>;
  raportatDe: FormControl<ReactieAdversaFormRawValue['raportatDe']>;
  medicament: FormControl<ReactieAdversaFormRawValue['medicament']>;
  pacient: FormControl<ReactieAdversaFormRawValue['pacient']>;
};

export type ReactieAdversaFormGroup = FormGroup<ReactieAdversaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReactieAdversaFormService {
  createReactieAdversaFormGroup(reactieAdversa: ReactieAdversaFormGroupInput = { id: null }): ReactieAdversaFormGroup {
    const reactieAdversaRawValue = this.convertReactieAdversaToReactieAdversaRawValue({
      ...this.getFormDefaults(),
      ...reactieAdversa,
    });
    return new FormGroup<ReactieAdversaFormGroupContent>({
      id: new FormControl(
        { value: reactieAdversaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataRaportare: new FormControl(reactieAdversaRawValue.dataRaportare, {
        validators: [Validators.required],
      }),
      severitate: new FormControl(reactieAdversaRawValue.severitate),
      descriere: new FormControl(reactieAdversaRawValue.descriere, {
        validators: [Validators.required],
      }),
      evolutie: new FormControl(reactieAdversaRawValue.evolutie),
      raportatDe: new FormControl(reactieAdversaRawValue.raportatDe),
      medicament: new FormControl(reactieAdversaRawValue.medicament, {
        validators: [Validators.required],
      }),
      pacient: new FormControl(reactieAdversaRawValue.pacient, {
        validators: [Validators.required],
      }),
    });
  }

  getReactieAdversa(form: ReactieAdversaFormGroup): IReactieAdversa | NewReactieAdversa {
    return this.convertReactieAdversaRawValueToReactieAdversa(
      form.getRawValue() as ReactieAdversaFormRawValue | NewReactieAdversaFormRawValue,
    );
  }

  resetForm(form: ReactieAdversaFormGroup, reactieAdversa: ReactieAdversaFormGroupInput): void {
    const reactieAdversaRawValue = this.convertReactieAdversaToReactieAdversaRawValue({ ...this.getFormDefaults(), ...reactieAdversa });
    form.reset(
      {
        ...reactieAdversaRawValue,
        id: { value: reactieAdversaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReactieAdversaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataRaportare: currentTime,
    };
  }

  private convertReactieAdversaRawValueToReactieAdversa(
    rawReactieAdversa: ReactieAdversaFormRawValue | NewReactieAdversaFormRawValue,
  ): IReactieAdversa | NewReactieAdversa {
    return {
      ...rawReactieAdversa,
      dataRaportare: dayjs(rawReactieAdversa.dataRaportare, DATE_TIME_FORMAT),
    };
  }

  private convertReactieAdversaToReactieAdversaRawValue(
    reactieAdversa: IReactieAdversa | (Partial<NewReactieAdversa> & ReactieAdversaFormDefaults),
  ): ReactieAdversaFormRawValue | PartialWithRequiredKeyOf<NewReactieAdversaFormRawValue> {
    return {
      ...reactieAdversa,
      dataRaportare: reactieAdversa.dataRaportare ? reactieAdversa.dataRaportare.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
