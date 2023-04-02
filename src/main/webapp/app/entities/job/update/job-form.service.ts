import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IJob, NewJob } from '../job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJob for edit and NewJobFormGroupInput for create.
 */
type JobFormGroupInput = IJob | PartialWithRequiredKeyOf<NewJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IJob | NewJob> = Omit<T, 'dateFrom' | 'dateTo'> & {
  dateFrom?: string | null;
  dateTo?: string | null;
};

type JobFormRawValue = FormValueOf<IJob>;

type NewJobFormRawValue = FormValueOf<NewJob>;

type JobFormDefaults = Pick<NewJob, 'id' | 'dateFrom' | 'dateTo' | 'isActive' | 'comments' | 'ratings' | 'appUsers'>;

type JobFormGroupContent = {
  id: FormControl<JobFormRawValue['id'] | NewJob['id']>;
  title: FormControl<JobFormRawValue['title']>;
  description: FormControl<JobFormRawValue['description']>;
  dificulty: FormControl<JobFormRawValue['dificulty']>;
  dateFrom: FormControl<JobFormRawValue['dateFrom']>;
  dateTo: FormControl<JobFormRawValue['dateTo']>;
  isActive: FormControl<JobFormRawValue['isActive']>;
  jobType: FormControl<JobFormRawValue['jobType']>;
  typeOfJob: FormControl<JobFormRawValue['typeOfJob']>;
  location: FormControl<JobFormRawValue['location']>;
  comments: FormControl<JobFormRawValue['comments']>;
  ratings: FormControl<JobFormRawValue['ratings']>;
  appUsers: FormControl<JobFormRawValue['appUsers']>;
};

export type JobFormGroup = FormGroup<JobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JobFormService {
  createJobFormGroup(job: JobFormGroupInput = { id: null }): JobFormGroup {
    const jobRawValue = this.convertJobToJobRawValue({
      ...this.getFormDefaults(),
      ...job,
    });
    return new FormGroup<JobFormGroupContent>({
      id: new FormControl(
        { value: jobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(jobRawValue.title),
      description: new FormControl(jobRawValue.description),
      dificulty: new FormControl(jobRawValue.dificulty),
      dateFrom: new FormControl(jobRawValue.dateFrom),
      dateTo: new FormControl(jobRawValue.dateTo),
      isActive: new FormControl(jobRawValue.isActive),
      jobType: new FormControl(jobRawValue.jobType),
      typeOfJob: new FormControl(jobRawValue.typeOfJob),
      location: new FormControl(jobRawValue.location),
      comments: new FormControl(jobRawValue.comments ?? []),
      ratings: new FormControl(jobRawValue.ratings ?? []),
      appUsers: new FormControl(jobRawValue.appUsers ?? []),
    });
  }

  getJob(form: JobFormGroup): IJob | NewJob {
    return this.convertJobRawValueToJob(form.getRawValue() as JobFormRawValue | NewJobFormRawValue);
  }

  resetForm(form: JobFormGroup, job: JobFormGroupInput): void {
    const jobRawValue = this.convertJobToJobRawValue({ ...this.getFormDefaults(), ...job });
    form.reset(
      {
        ...jobRawValue,
        id: { value: jobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): JobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateFrom: currentTime,
      dateTo: currentTime,
      isActive: false,
      comments: [],
      ratings: [],
      appUsers: [],
    };
  }

  private convertJobRawValueToJob(rawJob: JobFormRawValue | NewJobFormRawValue): IJob | NewJob {
    return {
      ...rawJob,
      dateFrom: dayjs(rawJob.dateFrom, DATE_TIME_FORMAT),
      dateTo: dayjs(rawJob.dateTo, DATE_TIME_FORMAT),
    };
  }

  private convertJobToJobRawValue(
    job: IJob | (Partial<NewJob> & JobFormDefaults)
  ): JobFormRawValue | PartialWithRequiredKeyOf<NewJobFormRawValue> {
    return {
      ...job,
      dateFrom: job.dateFrom ? job.dateFrom.format(DATE_TIME_FORMAT) : undefined,
      dateTo: job.dateTo ? job.dateTo.format(DATE_TIME_FORMAT) : undefined,
      comments: job.comments ?? [],
      ratings: job.ratings ?? [],
      appUsers: job.appUsers ?? [],
    };
  }
}
