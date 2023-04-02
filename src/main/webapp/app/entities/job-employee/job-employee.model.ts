import { IJob } from 'app/entities/job/job.model';
import { EmployeeType } from 'app/entities/enumerations/employee-type.model';

export interface IJobEmployee {
  id: number;
  type?: EmployeeType | null;
  number?: number | null;
  found?: number | null;
  job?: Pick<IJob, 'id'> | null;
}

export type NewJobEmployee = Omit<IJobEmployee, 'id'> & { id: null };
