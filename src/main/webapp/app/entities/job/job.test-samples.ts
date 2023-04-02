import dayjs from 'dayjs/esm';

import { JobType } from 'app/entities/enumerations/job-type.model';

import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: '6970a9f1-2401-404d-95ed-b9946035a427',
};

export const sampleWithPartialData: IJob = {
  id: 'ad777be9-853a-4384-a3b0-296055291f9a',
  title: 'tangible turquoise',
  description: 'distributed Gorgeous',
  dificulty: 83115,
  isActive: false,
  jobType: JobType['ONE_TIME'],
  typeOfJob: JobType['PERMANENT'],
};

export const sampleWithFullData: IJob = {
  id: '60de2a0e-9109-4188-9e9a-92f9281651e4',
  title: 'Chair withdrawal Fresh',
  description: 'Rubber Unbranded Designer',
  dificulty: 76005,
  dateFrom: dayjs('2023-04-02T03:25'),
  dateTo: dayjs('2023-04-02T10:42'),
  isActive: true,
  jobType: JobType['PERMANENT'],
  typeOfJob: JobType['PERMANENT'],
};

export const sampleWithNewData: NewJob = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
