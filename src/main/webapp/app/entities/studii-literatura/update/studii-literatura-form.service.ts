import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStudiiLiteratura, NewStudiiLiteratura } from '../studii-literatura.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStudiiLiteratura for edit and NewStudiiLiteraturaFormGroupInput for create.
 */
type StudiiLiteraturaFormGroupInput = IStudiiLiteratura | PartialWithRequiredKeyOf<NewStudiiLiteratura>;

type StudiiLiteraturaFormDefaults = Pick<NewStudiiLiteratura, 'id'>;

type StudiiLiteraturaFormGroupContent = {
  id: FormControl<IStudiiLiteratura['id'] | NewStudiiLiteratura['id']>;
  titlu: FormControl<IStudiiLiteratura['titlu']>;
  autori: FormControl<IStudiiLiteratura['autori']>;
  anul: FormControl<IStudiiLiteratura['anul']>;
  tipStudiu: FormControl<IStudiiLiteratura['tipStudiu']>;
  substanta: FormControl<IStudiiLiteratura['substanta']>;
  concluzie: FormControl<IStudiiLiteratura['concluzie']>;
  link: FormControl<IStudiiLiteratura['link']>;
  medicament: FormControl<IStudiiLiteratura['medicament']>;
};

export type StudiiLiteraturaFormGroup = FormGroup<StudiiLiteraturaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StudiiLiteraturaFormService {
  createStudiiLiteraturaFormGroup(studiiLiteratura: StudiiLiteraturaFormGroupInput = { id: null }): StudiiLiteraturaFormGroup {
    const studiiLiteraturaRawValue = {
      ...this.getFormDefaults(),
      ...studiiLiteratura,
    };
    return new FormGroup<StudiiLiteraturaFormGroupContent>({
      id: new FormControl(
        { value: studiiLiteraturaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titlu: new FormControl(studiiLiteraturaRawValue.titlu, {
        validators: [Validators.required],
      }),
      autori: new FormControl(studiiLiteraturaRawValue.autori),
      anul: new FormControl(studiiLiteraturaRawValue.anul),
      tipStudiu: new FormControl(studiiLiteraturaRawValue.tipStudiu),
      substanta: new FormControl(studiiLiteraturaRawValue.substanta),
      concluzie: new FormControl(studiiLiteraturaRawValue.concluzie),
      link: new FormControl(studiiLiteraturaRawValue.link),
      medicament: new FormControl(studiiLiteraturaRawValue.medicament),
    });
  }

  getStudiiLiteratura(form: StudiiLiteraturaFormGroup): IStudiiLiteratura | NewStudiiLiteratura {
    return form.getRawValue() as IStudiiLiteratura | NewStudiiLiteratura;
  }

  resetForm(form: StudiiLiteraturaFormGroup, studiiLiteratura: StudiiLiteraturaFormGroupInput): void {
    const studiiLiteraturaRawValue = { ...this.getFormDefaults(), ...studiiLiteratura };
    form.reset(
      {
        ...studiiLiteraturaRawValue,
        id: { value: studiiLiteraturaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StudiiLiteraturaFormDefaults {
    return {
      id: null,
    };
  }
}
