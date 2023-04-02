import { EmployeeType } from 'app/entities/enumerations/employee-type.model';

import { IJobEmployee, NewJobEmployee } from './job-employee.model';

export const sampleWithRequiredData: IJobEmployee = {
  id: 79844,
};

export const sampleWithPartialData: IJobEmployee = {
  id: 33623,
  number: 35305,
};

export const sampleWithFullData: IJobEmployee = {
  id: 69867,
  type: EmployeeType['COOK'],
  number: 82238,
  found: 47174,
};

export const sampleWithNewData: NewJobEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
