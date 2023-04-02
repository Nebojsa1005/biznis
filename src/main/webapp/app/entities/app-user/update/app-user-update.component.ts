import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AppUserFormService, AppUserFormGroup } from './app-user-form.service';
import { IAppUser } from '../app-user.model';
import { AppUserService } from '../service/app-user.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';
import { IRating } from 'app/entities/rating/rating.model';
import { RatingService } from 'app/entities/rating/service/rating.service';
import { UserType } from 'app/entities/enumerations/user-type.model';

@Component({
  selector: 'jhi-app-user-update',
  templateUrl: './app-user-update.component.html',
})
export class AppUserUpdateComponent implements OnInit {
  isSaving = false;
  appUser: IAppUser | null = null;
  userTypeValues = Object.keys(UserType);

  jobsSharedCollection: IJob[] = [];
  commentsSharedCollection: IComment[] = [];
  ratingsSharedCollection: IRating[] = [];

  editForm: AppUserFormGroup = this.appUserFormService.createAppUserFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected appUserService: AppUserService,
    protected appUserFormService: AppUserFormService,
    protected jobService: JobService,
    protected commentService: CommentService,
    protected ratingService: RatingService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareJob = (o1: IJob | null, o2: IJob | null): boolean => this.jobService.compareJob(o1, o2);

  compareComment = (o1: IComment | null, o2: IComment | null): boolean => this.commentService.compareComment(o1, o2);

  compareRating = (o1: IRating | null, o2: IRating | null): boolean => this.ratingService.compareRating(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appUser }) => {
      this.appUser = appUser;
      if (appUser) {
        this.updateForm(appUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('serveUpApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appUser = this.appUserFormService.getAppUser(this.editForm);
    if (appUser.id !== null) {
      this.subscribeToSaveResponse(this.appUserService.update(appUser));
    } else {
      this.subscribeToSaveResponse(this.appUserService.create(appUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(appUser: IAppUser): void {
    this.appUser = appUser;
    this.appUserFormService.resetForm(this.editForm, appUser);

    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing<IJob>(this.jobsSharedCollection, ...(appUser.jobs ?? []));
    this.commentsSharedCollection = this.commentService.addCommentToCollectionIfMissing<IComment>(
      this.commentsSharedCollection,
      ...(appUser.comments ?? [])
    );
    this.ratingsSharedCollection = this.ratingService.addRatingToCollectionIfMissing<IRating>(
      this.ratingsSharedCollection,
      ...(appUser.ratings ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.jobService
      .query()
      .pipe(map((res: HttpResponse<IJob[]>) => res.body ?? []))
      .pipe(map((jobs: IJob[]) => this.jobService.addJobToCollectionIfMissing<IJob>(jobs, ...(this.appUser?.jobs ?? []))))
      .subscribe((jobs: IJob[]) => (this.jobsSharedCollection = jobs));

    this.commentService
      .query()
      .pipe(map((res: HttpResponse<IComment[]>) => res.body ?? []))
      .pipe(
        map((comments: IComment[]) =>
          this.commentService.addCommentToCollectionIfMissing<IComment>(comments, ...(this.appUser?.comments ?? []))
        )
      )
      .subscribe((comments: IComment[]) => (this.commentsSharedCollection = comments));

    this.ratingService
      .query()
      .pipe(map((res: HttpResponse<IRating[]>) => res.body ?? []))
      .pipe(
        map((ratings: IRating[]) => this.ratingService.addRatingToCollectionIfMissing<IRating>(ratings, ...(this.appUser?.ratings ?? [])))
      )
      .subscribe((ratings: IRating[]) => (this.ratingsSharedCollection = ratings));
  }
}
