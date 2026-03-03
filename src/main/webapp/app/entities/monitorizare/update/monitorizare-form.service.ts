import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMonitorizare, NewMonitorizare } from '../monitorizare.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMonitorizare for edit and NewMonitorizareFormGroupInput for create.
 */
type MonitorizareFormGroupInput = IMonitorizare | PartialWithRequiredKeyOf<NewMonitorizare>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMonitorizare | NewMonitorizare> = Omit<T, 'dataInstant'> & {
  dataInstant?: string | null;
};

type MonitorizareFormRawValue = FormValueOf<IMonitorizare>;

type NewMonitorizareFormRawValue = FormValueOf<NewMonitorizare>;

type MonitorizareFormDefaults = Pick<NewMonitorizare, 'id' | 'dataInstant'>;

type MonitorizareFormGroupContent = {
  id: FormControl<MonitorizareFormRawValue['id'] | NewMonitorizare['id']>;
  dataInstant: FormControl<MonitorizareFormRawValue['dataInstant']>;
  tensiuneSist: FormControl<MonitorizareFormRawValue['tensiuneSist']>;
  tensiuneDiast: FormControl<MonitorizareFormRawValue['tensiuneDiast']>;
  puls: FormControl<MonitorizareFormRawValue['puls']>;
  glicemie: FormControl<MonitorizareFormRawValue['glicemie']>;
  scorEficacitate: FormControl<MonitorizareFormRawValue['scorEficacitate']>;
  comentarii: FormControl<MonitorizareFormRawValue['comentarii']>;
  pacient: FormControl<MonitorizareFormRawValue['pacient']>;
};

export type MonitorizareFormGroup = FormGroup<MonitorizareFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MonitorizareFormService {
  createMonitorizareFormGroup(monitorizare: MonitorizareFormGroupInput = { id: null }): MonitorizareFormGroup {
    const monitorizareRawValue = this.convertMonitorizareToMonitorizareRawValue({
      ...this.getFormDefaults(),
      ...monitorizare,
    });
    return new FormGroup<MonitorizareFormGroupContent>({
      id: new FormControl(
        { value: monitorizareRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataInstant: new FormControl(monitorizareRawValue.dataInstant, {
        validators: [Validators.required],
      }),
      tensiuneSist: new FormControl(monitorizareRawValue.tensiuneSist, {
        validators: [Validators.min(60), Validators.max(300)],
      }),
      tensiuneDiast: new FormControl(monitorizareRawValue.tensiuneDiast, {
        validators: [Validators.min(30), Validators.max(200)],
      }),
      puls: new FormControl(monitorizareRawValue.puls, {
        validators: [Validators.min(20), Validators.max(250)],
      }),
      glicemie: new FormControl(monitorizareRawValue.glicemie, {
        validators: [Validators.min(2), Validators.max(1000)],
      }),
      scorEficacitate: new FormControl(monitorizareRawValue.scorEficacitate, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      comentarii: new FormControl(monitorizareRawValue.comentarii),
      pacient: new FormControl(monitorizareRawValue.pacient),
    });
  }

  getMonitorizare(form: MonitorizareFormGroup): IMonitorizare | NewMonitorizare {
    return this.convertMonitorizareRawValueToMonitorizare(form.getRawValue() as MonitorizareFormRawValue | NewMonitorizareFormRawValue);
  }

  resetForm(form: MonitorizareFormGroup, monitorizare: MonitorizareFormGroupInput): void {
    const monitorizareRawValue = this.convertMonitorizareToMonitorizareRawValue({ ...this.getFormDefaults(), ...monitorizare });
    form.reset(
      {
        ...monitorizareRawValue,
        id: { value: monitorizareRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MonitorizareFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataInstant: currentTime,
    };
  }

  private convertMonitorizareRawValueToMonitorizare(
    rawMonitorizare: MonitorizareFormRawValue | NewMonitorizareFormRawValue,
  ): IMonitorizare | NewMonitorizare {
    return {
      ...rawMonitorizare,
      dataInstant: dayjs(rawMonitorizare.dataInstant, DATE_TIME_FORMAT),
    };
  }

  private convertMonitorizareToMonitorizareRawValue(
    monitorizare: IMonitorizare | (Partial<NewMonitorizare> & MonitorizareFormDefaults),
  ): MonitorizareFormRawValue | PartialWithRequiredKeyOf<NewMonitorizareFormRawValue> {
    return {
      ...monitorizare,
      dataInstant: monitorizare.dataInstant ? monitorizare.dataInstant.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
