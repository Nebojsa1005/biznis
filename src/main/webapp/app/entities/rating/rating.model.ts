import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IJob } from 'app/entities/job/job.model';

export interface IRating {
  id: string;
  rating?: number | null;
  appUsers?: Pick<IAppUser, 'id'>[] | null;
  jobs?: Pick<IJob, 'id'>[] | null;
}

export type NewRating = Omit<IRating, 'id'> & { id: null };
