export interface ILocation {
  id: string;
  latitude?: number | null;
  longitude?: number | null;
  streetAddress?: string | null;
  postalCode?: number | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
