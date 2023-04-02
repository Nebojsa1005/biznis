import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 'c4f66c59-0ae8-4960-96be-a072eb974882',
};

export const sampleWithPartialData: IComment = {
  id: 'd9d7b2c9-4e4a-4588-8d4a-1ed15d945f63',
  comment: 'Unbranded',
};

export const sampleWithFullData: IComment = {
  id: 'b51b3dd2-f229-4569-b1ba-07cc8b44122d',
  comment: 'United compelling connect',
};

export const sampleWithNewData: NewComment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
