import { IRating, NewRating } from './rating.model';

export const sampleWithRequiredData: IRating = {
  id: 'ee6a755f-88af-4513-b28c-d62e7692563d',
};

export const sampleWithPartialData: IRating = {
  id: 'bf818a24-3990-4d58-ba76-02917e2e6aa9',
};

export const sampleWithFullData: IRating = {
  id: '779996ce-c242-43db-9a3d-da7c2c1eff4c',
  rating: 30050,
};

export const sampleWithNewData: NewRating = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
