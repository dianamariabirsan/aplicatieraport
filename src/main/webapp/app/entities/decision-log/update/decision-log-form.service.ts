import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDecisionLog, NewDecisionLog } from '../decision-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDecisionLog for edit and NewDecisionLogFormGroupInput for create.
 */
type DecisionLogFormGroupInput = IDecisionLog | PartialWithRequiredKeyOf<NewDecisionLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDecisionLog | NewDecisionLog> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type DecisionLogFormRawValue = FormValueOf<IDecisionLog>;

type NewDecisionLogFormRawValue = FormValueOf<NewDecisionLog>;

type DecisionLogFormDefaults = Pick<NewDecisionLog, 'id' | 'timestamp'>;

type DecisionLogFormGroupContent = {
  id: FormControl<DecisionLogFormRawValue['id'] | NewDecisionLog['id']>;
  timestamp: FormControl<DecisionLogFormRawValue['timestamp']>;
  actorType: FormControl<DecisionLogFormRawValue['actorType']>;
  recomandare: FormControl<DecisionLogFormRawValue['recomandare']>;
  modelScore: FormControl<DecisionLogFormRawValue['modelScore']>;
  reguliTriggered: FormControl<DecisionLogFormRawValue['reguliTriggered']>;
  externalChecks: FormControl<DecisionLogFormRawValue['externalChecks']>;
  alocare: FormControl<DecisionLogFormRawValue['alocare']>;
};

export type DecisionLogFormGroup = FormGroup<DecisionLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DecisionLogFormService {
  createDecisionLogFormGroup(decisionLog: DecisionLogFormGroupInput = { id: null }): DecisionLogFormGroup {
    const decisionLogRawValue = this.convertDecisionLogToDecisionLogRawValue({
      ...this.getFormDefaults(),
      ...decisionLog,
    });
    return new FormGroup<DecisionLogFormGroupContent>({
      id: new FormControl(
        { value: decisionLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      timestamp: new FormControl(decisionLogRawValue.timestamp, {
        validators: [Validators.required],
      }),
      actorType: new FormControl(decisionLogRawValue.actorType, {
        validators: [Validators.required],
      }),
      recomandare: new FormControl(decisionLogRawValue.recomandare),
      modelScore: new FormControl(decisionLogRawValue.modelScore),
      reguliTriggered: new FormControl(decisionLogRawValue.reguliTriggered),
      externalChecks: new FormControl(decisionLogRawValue.externalChecks),
      alocare: new FormControl(decisionLogRawValue.alocare),
    });
  }

  getDecisionLog(form: DecisionLogFormGroup): IDecisionLog | NewDecisionLog {
    return this.convertDecisionLogRawValueToDecisionLog(form.getRawValue() as DecisionLogFormRawValue | NewDecisionLogFormRawValue);
  }

  resetForm(form: DecisionLogFormGroup, decisionLog: DecisionLogFormGroupInput): void {
    const decisionLogRawValue = this.convertDecisionLogToDecisionLogRawValue({ ...this.getFormDefaults(), ...decisionLog });
    form.reset(
      {
        ...decisionLogRawValue,
        id: { value: decisionLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DecisionLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertDecisionLogRawValueToDecisionLog(
    rawDecisionLog: DecisionLogFormRawValue | NewDecisionLogFormRawValue,
  ): IDecisionLog | NewDecisionLog {
    return {
      ...rawDecisionLog,
      timestamp: dayjs(rawDecisionLog.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertDecisionLogToDecisionLogRawValue(
    decisionLog: IDecisionLog | (Partial<NewDecisionLog> & DecisionLogFormDefaults),
  ): DecisionLogFormRawValue | PartialWithRequiredKeyOf<NewDecisionLogFormRawValue> {
    return {
      ...decisionLog,
      timestamp: decisionLog.timestamp ? decisionLog.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
