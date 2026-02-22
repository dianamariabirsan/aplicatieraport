import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPacient, NewPacient } from '../pacient.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPacient for edit and NewPacientFormGroupInput for create.
 */
type PacientFormGroupInput = IPacient | PartialWithRequiredKeyOf<NewPacient>;

type PacientFormDefaults = Pick<NewPacient, 'id'>;

type PacientFormGroupContent = {
  id: FormControl<IPacient['id'] | NewPacient['id']>;
  nume: FormControl<IPacient['nume']>;
  prenume: FormControl<IPacient['prenume']>;
  sex: FormControl<IPacient['sex']>;
  varsta: FormControl<IPacient['varsta']>;
  greutate: FormControl<IPacient['greutate']>;
  inaltime: FormControl<IPacient['inaltime']>;
  circumferintaAbdominala: FormControl<IPacient['circumferintaAbdominala']>;
  cnp: FormControl<IPacient['cnp']>;
  comorbiditati: FormControl<IPacient['comorbiditati']>;
  gradSedentarism: FormControl<IPacient['gradSedentarism']>;
  istoricTratament: FormControl<IPacient['istoricTratament']>;
  toleranta: FormControl<IPacient['toleranta']>;
  email: FormControl<IPacient['email']>;
  telefon: FormControl<IPacient['telefon']>;
  medic: FormControl<IPacient['medic']>;
  farmacist: FormControl<IPacient['farmacist']>;
};

export type PacientFormGroup = FormGroup<PacientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PacientFormService {
  createPacientFormGroup(pacient: PacientFormGroupInput = { id: null }): PacientFormGroup {
    const pacientRawValue = {
      ...this.getFormDefaults(),
      ...pacient,
    };
    return new FormGroup<PacientFormGroupContent>({
      id: new FormControl(
        { value: pacientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nume: new FormControl(pacientRawValue.nume, {
        validators: [Validators.required],
      }),
      prenume: new FormControl(pacientRawValue.prenume, {
        validators: [Validators.required],
      }),
      sex: new FormControl(pacientRawValue.sex, {
        validators: [Validators.required],
      }),
      varsta: new FormControl(pacientRawValue.varsta, {
        validators: [Validators.required],
      }),
      greutate: new FormControl(pacientRawValue.greutate),
      inaltime: new FormControl(pacientRawValue.inaltime),
      circumferintaAbdominala: new FormControl(pacientRawValue.circumferintaAbdominala),
      cnp: new FormControl(pacientRawValue.cnp, {
        validators: [Validators.minLength(13), Validators.maxLength(13)],
      }),
      comorbiditati: new FormControl(pacientRawValue.comorbiditati),
      gradSedentarism: new FormControl(pacientRawValue.gradSedentarism),
      istoricTratament: new FormControl(pacientRawValue.istoricTratament),
      toleranta: new FormControl(pacientRawValue.toleranta),
      email: new FormControl(pacientRawValue.email),
      telefon: new FormControl(pacientRawValue.telefon),
      medic: new FormControl(pacientRawValue.medic),
      farmacist: new FormControl(pacientRawValue.farmacist),
    });
  }

  getPacient(form: PacientFormGroup): IPacient | NewPacient {
    return form.getRawValue() as IPacient | NewPacient;
  }

  resetForm(form: PacientFormGroup, pacient: PacientFormGroupInput): void {
    const pacientRawValue = { ...this.getFormDefaults(), ...pacient };
    form.reset(
      {
        ...pacientRawValue,
        id: { value: pacientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PacientFormDefaults {
    return {
      id: null,
    };
  }
}
