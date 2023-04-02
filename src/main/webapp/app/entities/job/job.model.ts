import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';
import { IComment } from 'app/entities/comment/comment.model';
import { IRating } from 'app/entities/rating/rating.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { JobType } from 'app/entities/enumerations/job-type.model';

export interface IJob {
  id: string;
  title?: string | null;
  description?: string | null;
  dificulty?: number | null;
  dateFrom?: dayjs.Dayjs | null;
  dateTo?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  jobType?: JobType | null;
  typeOfJob?: JobType | null;
  location?: Pick<ILocation, 'id'> | null;
  comments?: Pick<IComment, 'id'>[] | null;
  ratings?: Pick<IRating, 'id'>[] | null;
  appUsers?: Pick<IAppUser, 'id'>[] | null;
}

export type NewJob = Omit<IJob, 'id'> & { id: null };
