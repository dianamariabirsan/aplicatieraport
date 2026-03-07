import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExternalDrugInfo, NewExternalDrugInfo } from '../external-drug-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExternalDrugInfo for edit and NewExternalDrugInfoFormGroupInput for create.
 */
type ExternalDrugInfoFormGroupInput = IExternalDrugInfo | PartialWithRequiredKeyOf<NewExternalDrugInfo>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExternalDrugInfo | NewExternalDrugInfo> = Omit<T, 'lastUpdated'> & {
  lastUpdated?: string | null;
};

type ExternalDrugInfoFormRawValue = FormValueOf<IExternalDrugInfo>;
type NewExternalDrugInfoFormRawValue = FormValueOf<NewExternalDrugInfo>;

type ExternalDrugInfoFormDefaults = Pick<NewExternalDrugInfo, 'id' | 'lastUpdated' | 'medicament'>;

type ExternalDrugInfoFormGroupContent = {
  id: FormControl<ExternalDrugInfoFormRawValue['id'] | NewExternalDrugInfo['id']>;
  source: FormControl<ExternalDrugInfoFormRawValue['source']>;
  productSummary: FormControl<ExternalDrugInfoFormRawValue['productSummary']>;
  lastUpdated: FormControl<ExternalDrugInfoFormRawValue['lastUpdated']>;
  sourceUrl: FormControl<ExternalDrugInfoFormRawValue['sourceUrl']>;
  medicament: FormControl<ExternalDrugInfoFormRawValue['medicament']>;
};

export type ExternalDrugInfoFormGroup = FormGroup<ExternalDrugInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExternalDrugInfoFormService {
  createExternalDrugInfoFormGroup(externalDrugInfo: ExternalDrugInfoFormGroupInput = { id: null }): ExternalDrugInfoFormGroup {
    const externalDrugInfoRawValue = this.convertExternalDrugInfoToExternalDrugInfoRawValue({
      ...this.getFormDefaults(),
      ...externalDrugInfo,
    });

    return new FormGroup<ExternalDrugInfoFormGroupContent>({
      id: new FormControl(
        { value: externalDrugInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      source: new FormControl(externalDrugInfoRawValue.source, {
        validators: [Validators.required],
      }),
      productSummary: new FormControl(externalDrugInfoRawValue.productSummary),
      lastUpdated: new FormControl(externalDrugInfoRawValue.lastUpdated),
      sourceUrl: new FormControl(externalDrugInfoRawValue.sourceUrl),
      medicament: new FormControl(externalDrugInfoRawValue.medicament),
    });
  }

  getExternalDrugInfo(form: ExternalDrugInfoFormGroup): IExternalDrugInfo | NewExternalDrugInfo {
    return this.convertExternalDrugInfoRawValueToExternalDrugInfo(
      form.getRawValue() as ExternalDrugInfoFormRawValue | NewExternalDrugInfoFormRawValue,
    );
  }

  resetForm(form: ExternalDrugInfoFormGroup, externalDrugInfo: ExternalDrugInfoFormGroupInput): void {
    const externalDrugInfoRawValue = this.convertExternalDrugInfoToExternalDrugInfoRawValue({
      ...this.getFormDefaults(),
      ...externalDrugInfo,
    });

    form.reset({
      ...externalDrugInfoRawValue,
      id: { value: externalDrugInfoRawValue.id, disabled: true },
    } as any);
  }

  private getFormDefaults(): ExternalDrugInfoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastUpdated: currentTime,
      medicament: null,
    };
  }

  private convertExternalDrugInfoRawValueToExternalDrugInfo(
    rawExternalDrugInfo: ExternalDrugInfoFormRawValue | NewExternalDrugInfoFormRawValue,
  ): IExternalDrugInfo | NewExternalDrugInfo {
    return {
      ...rawExternalDrugInfo,
      lastUpdated: dayjs(rawExternalDrugInfo.lastUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertExternalDrugInfoToExternalDrugInfoRawValue(
    externalDrugInfo: IExternalDrugInfo | (Partial<NewExternalDrugInfo> & ExternalDrugInfoFormDefaults),
  ): ExternalDrugInfoFormRawValue | PartialWithRequiredKeyOf<NewExternalDrugInfoFormRawValue> {
    return {
      ...externalDrugInfo,
      lastUpdated: externalDrugInfo.lastUpdated ? externalDrugInfo.lastUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
