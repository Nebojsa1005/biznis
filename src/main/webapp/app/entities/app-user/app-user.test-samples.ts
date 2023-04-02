import { UserType } from 'app/entities/enumerations/user-type.model';

import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: '0bd47b77-a5cf-40eb-87c2-5f289ee70007',
};

export const sampleWithPartialData: IAppUser = {
  id: 'bc7914ce-7837-4d33-ba42-b24d74b9b77d',
  firstName: 'Nathen',
  avgSalary: 12799,
  avgRating: 20474,
  userType: UserType['EMPLOYEE'],
};

export const sampleWithFullData: IAppUser = {
  id: '6e02e3ad-4e9a-4353-b9b3-3eb1195b01b5',
  firstName: 'Sandy',
  lastName: 'Torp',
  email: 'Savanna.Morissette52@gmail.com',
  phone: 97941,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  avgSalary: 44811,
  avgRating: 52361,
  isActive: false,
  userType: UserType['EMPLOYER'],
};

export const sampleWithNewData: NewAppUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
