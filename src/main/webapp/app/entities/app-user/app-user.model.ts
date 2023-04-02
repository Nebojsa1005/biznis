import { IJob } from 'app/entities/job/job.model';
import { IComment } from 'app/entities/comment/comment.model';
import { IRating } from 'app/entities/rating/rating.model';
import { UserType } from 'app/entities/enumerations/user-type.model';

export interface IAppUser {
  id: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phone?: number | null;
  image?: string | null;
  imageContentType?: string | null;
  avgSalary?: number | null;
  avgRating?: number | null;
  isActive?: boolean | null;
  userType?: UserType | null;
  jobs?: Pick<IJob, 'id'>[] | null;
  comments?: Pick<IComment, 'id'>[] | null;
  ratings?: Pick<IRating, 'id'>[] | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
