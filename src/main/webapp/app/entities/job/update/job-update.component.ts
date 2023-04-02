import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { JobFormService, JobFormGroup } from './job-form.service';
import { IJob } from '../job.model';
import { JobService } from '../service/job.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';
import { IRating } from 'app/entities/rating/rating.model';
import { RatingService } from 'app/entities/rating/service/rating.service';
import { JobType } from 'app/entities/enumerations/job-type.model';

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html',
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;
  job: IJob | null = null;
  jobTypeValues = Object.keys(JobType);

  locationsCollection: ILocation[] = [];
  commentsSharedCollection: IComment[] = [];
  ratingsSharedCollection: IRating[] = [];

  editForm: JobFormGroup = this.jobFormService.createJobFormGroup();

  constructor(
    protected jobService: JobService,
    protected jobFormService: JobFormService,
    protected locationService: LocationService,
    protected commentService: CommentService,
    protected ratingService: RatingService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  compareComment = (o1: IComment | null, o2: IComment | null): boolean => this.commentService.compareComment(o1, o2);

  compareRating = (o1: IRating | null, o2: IRating | null): boolean => this.ratingService.compareRating(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      this.job = job;
      if (job) {
        this.updateForm(job);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.jobFormService.getJob(this.editForm);
    if (job.id !== null) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
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

  protected updateForm(job: IJob): void {
    this.job = job;
    this.jobFormService.resetForm(this.editForm, job);

    this.locationsCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(this.locationsCollection, job.location);
    this.commentsSharedCollection = this.commentService.addCommentToCollectionIfMissing<IComment>(
      this.commentsSharedCollection,
      ...(job.comments ?? [])
    );
    this.ratingsSharedCollection = this.ratingService.addRatingToCollectionIfMissing<IRating>(
      this.ratingsSharedCollection,
      ...(job.ratings ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query({ filter: 'job-is-null' })
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) => this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.job?.location))
      )
      .subscribe((locations: ILocation[]) => (this.locationsCollection = locations));

    this.commentService
      .query()
      .pipe(map((res: HttpResponse<IComment[]>) => res.body ?? []))
      .pipe(
        map((comments: IComment[]) =>
          this.commentService.addCommentToCollectionIfMissing<IComment>(comments, ...(this.job?.comments ?? []))
        )
      )
      .subscribe((comments: IComment[]) => (this.commentsSharedCollection = comments));

    this.ratingService
      .query()
      .pipe(map((res: HttpResponse<IRating[]>) => res.body ?? []))
      .pipe(map((ratings: IRating[]) => this.ratingService.addRatingToCollectionIfMissing<IRating>(ratings, ...(this.job?.ratings ?? []))))
      .subscribe((ratings: IRating[]) => (this.ratingsSharedCollection = ratings));
  }
}
