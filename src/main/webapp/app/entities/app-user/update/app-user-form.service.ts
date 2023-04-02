import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppUser, NewAppUser } from '../app-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppUser for edit and NewAppUserFormGroupInput for create.
 */
type AppUserFormGroupInput = IAppUser | PartialWithRequiredKeyOf<NewAppUser>;

type AppUserFormDefaults = Pick<NewAppUser, 'id' | 'isActive' | 'jobs' | 'comments' | 'ratings'>;

type AppUserFormGroupContent = {
  id: FormControl<IAppUser['id'] | NewAppUser['id']>;
  firstName: FormControl<IAppUser['firstName']>;
  lastName: FormControl<IAppUser['lastName']>;
  email: FormControl<IAppUser['email']>;
  phone: FormControl<IAppUser['phone']>;
  image: FormControl<IAppUser['image']>;
  imageContentType: FormControl<IAppUser['imageContentType']>;
  avgSalary: FormControl<IAppUser['avgSalary']>;
  avgRating: FormControl<IAppUser['avgRating']>;
  isActive: FormControl<IAppUser['isActive']>;
  userType: FormControl<IAppUser['userType']>;
  jobs: FormControl<IAppUser['jobs']>;
  comments: FormControl<IAppUser['comments']>;
  ratings: FormControl<IAppUser['ratings']>;
};

export type AppUserFormGroup = FormGroup<AppUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppUserFormService {
  createAppUserFormGroup(appUser: AppUserFormGroupInput = { id: null }): AppUserFormGroup {
    const appUserRawValue = {
      ...this.getFormDefaults(),
      ...appUser,
    };
    return new FormGroup<AppUserFormGroupContent>({
      id: new FormControl(
        { value: appUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(appUserRawValue.firstName),
      lastName: new FormControl(appUserRawValue.lastName),
      email: new FormControl(appUserRawValue.email),
      phone: new FormControl(appUserRawValue.phone),
      image: new FormControl(appUserRawValue.image),
      imageContentType: new FormControl(appUserRawValue.imageContentType),
      avgSalary: new FormControl(appUserRawValue.avgSalary),
      avgRating: new FormControl(appUserRawValue.avgRating),
      isActive: new FormControl(appUserRawValue.isActive),
      userType: new FormControl(appUserRawValue.userType),
      jobs: new FormControl(appUserRawValue.jobs ?? []),
      comments: new FormControl(appUserRawValue.comments ?? []),
      ratings: new FormControl(appUserRawValue.ratings ?? []),
    });
  }

  getAppUser(form: AppUserFormGroup): IAppUser | NewAppUser {
    return form.getRawValue() as IAppUser | NewAppUser;
  }

  resetForm(form: AppUserFormGroup, appUser: AppUserFormGroupInput): void {
    const appUserRawValue = { ...this.getFormDefaults(), ...appUser };
    form.reset(
      {
        ...appUserRawValue,
        id: { value: appUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AppUserFormDefaults {
    return {
      id: null,
      isActive: false,
      jobs: [],
      comments: [],
      ratings: [],
    };
  }
}
