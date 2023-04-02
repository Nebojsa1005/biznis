import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IJob } from 'app/entities/job/job.model';

export interface IComment {
  id: string;
  comment?: string | null;
  appUsers?: Pick<IAppUser, 'id'>[] | null;
  jobs?: Pick<IJob, 'id'>[] | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
