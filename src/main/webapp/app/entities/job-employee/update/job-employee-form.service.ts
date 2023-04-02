import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IJobEmployee, NewJobEmployee } from '../job-employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJobEmployee for edit and NewJobEmployeeFormGroupInput for create.
 */
type JobEmployeeFormGroupInput = IJobEmployee | PartialWithRequiredKeyOf<NewJobEmployee>;

type JobEmployeeFormDefaults = Pick<NewJobEmployee, 'id'>;

type JobEmployeeFormGroupContent = {
  id: FormControl<IJobEmployee['id'] | NewJobEmployee['id']>;
  type: FormControl<IJobEmployee['type']>;
  number: FormControl<IJobEmployee['number']>;
  found: FormControl<IJobEmployee['found']>;
  job: FormControl<IJobEmployee['job']>;
};

export type JobEmployeeFormGroup = FormGroup<JobEmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JobEmployeeFormService {
  createJobEmployeeFormGroup(jobEmployee: JobEmployeeFormGroupInput = { id: null }): JobEmployeeFormGroup {
    const jobEmployeeRawValue = {
      ...this.getFormDefaults(),
      ...jobEmployee,
    };
    return new FormGroup<JobEmployeeFormGroupContent>({
      id: new FormControl(
        { value: jobEmployeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      type: new FormControl(jobEmployeeRawValue.type),
      number: new FormControl(jobEmployeeRawValue.number),
      found: new FormControl(jobEmployeeRawValue.found),
      job: new FormControl(jobEmployeeRawValue.job),
    });
  }

  getJobEmployee(form: JobEmployeeFormGroup): IJobEmployee | NewJobEmployee {
    return form.getRawValue() as IJobEmployee | NewJobEmployee;
  }

  resetForm(form: JobEmployeeFormGroup, jobEmployee: JobEmployeeFormGroupInput): void {
    const jobEmployeeRawValue = { ...this.getFormDefaults(), ...jobEmployee };
    form.reset(
      {
        ...jobEmployeeRawValue,
        id: { value: jobEmployeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): JobEmployeeFormDefaults {
    return {
      id: null,
    };
  }
}
