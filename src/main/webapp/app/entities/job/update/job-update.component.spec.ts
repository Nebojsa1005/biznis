import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JobFormService } from './job-form.service';
import { JobService } from '../service/job.service';
import { IJob } from '../job.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';
import { IRating } from 'app/entities/rating/rating.model';
import { RatingService } from 'app/entities/rating/service/rating.service';

import { JobUpdateComponent } from './job-update.component';

describe('Job Management Update Component', () => {
  let comp: JobUpdateComponent;
  let fixture: ComponentFixture<JobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let jobFormService: JobFormService;
  let jobService: JobService;
  let locationService: LocationService;
  let commentService: CommentService;
  let ratingService: RatingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [JobUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(JobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    jobFormService = TestBed.inject(JobFormService);
    jobService = TestBed.inject(JobService);
    locationService = TestBed.inject(LocationService);
    commentService = TestBed.inject(CommentService);
    ratingService = TestBed.inject(RatingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call location query and add missing value', () => {
      const job: IJob = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const location: ILocation = { id: '6019884f-e206-4723-98ed-2aa893bbbb18' };
      job.location = location;

      const locationCollection: ILocation[] = [{ id: '4746bdaf-5629-4785-b25a-07b1be2d01cc' }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const expectedCollection: ILocation[] = [location, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ job });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, location);
      expect(comp.locationsCollection).toEqual(expectedCollection);
    });

    it('Should call Comment query and add missing value', () => {
      const job: IJob = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const comments: IComment[] = [{ id: '2d2a88b0-be75-45f1-af5f-c3426cc30835' }];
      job.comments = comments;

      const commentCollection: IComment[] = [{ id: '2b5eb13d-0086-4072-9dc4-a9fe605b193d' }];
      jest.spyOn(commentService, 'query').mockReturnValue(of(new HttpResponse({ body: commentCollection })));
      const additionalComments = [...comments];
      const expectedCollection: IComment[] = [...additionalComments, ...commentCollection];
      jest.spyOn(commentService, 'addCommentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ job });
      comp.ngOnInit();

      expect(commentService.query).toHaveBeenCalled();
      expect(commentService.addCommentToCollectionIfMissing).toHaveBeenCalledWith(
        commentCollection,
        ...additionalComments.map(expect.objectContaining)
      );
      expect(comp.commentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rating query and add missing value', () => {
      const job: IJob = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const ratings: IRating[] = [{ id: '16b86c5d-d498-4de7-a49c-0412b8c78422' }];
      job.ratings = ratings;

      const ratingCollection: IRating[] = [{ id: '05b3f784-f151-49d2-ae44-300880e2aab8' }];
      jest.spyOn(ratingService, 'query').mockReturnValue(of(new HttpResponse({ body: ratingCollection })));
      const additionalRatings = [...ratings];
      const expectedCollection: IRating[] = [...additionalRatings, ...ratingCollection];
      jest.spyOn(ratingService, 'addRatingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ job });
      comp.ngOnInit();

      expect(ratingService.query).toHaveBeenCalled();
      expect(ratingService.addRatingToCollectionIfMissing).toHaveBeenCalledWith(
        ratingCollection,
        ...additionalRatings.map(expect.objectContaining)
      );
      expect(comp.ratingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const job: IJob = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const location: ILocation = { id: 'c4611a4a-f37c-4457-8efa-cc4984b19d6b' };
      job.location = location;
      const comment: IComment = { id: '52ab5a21-881a-4ad7-8743-7ce8d1d93006' };
      job.comments = [comment];
      const rating: IRating = { id: '45c0d98b-8783-4cfd-932a-8d41b832047e' };
      job.ratings = [rating];

      activatedRoute.data = of({ job });
      comp.ngOnInit();

      expect(comp.locationsCollection).toContain(location);
      expect(comp.commentsSharedCollection).toContain(comment);
      expect(comp.ratingsSharedCollection).toContain(rating);
      expect(comp.job).toEqual(job);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJob>>();
      const job = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(jobFormService, 'getJob').mockReturnValue(job);
      jest.spyOn(jobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ job });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: job }));
      saveSubject.complete();

      // THEN
      expect(jobFormService.getJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(jobService.update).toHaveBeenCalledWith(expect.objectContaining(job));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJob>>();
      const job = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(jobFormService, 'getJob').mockReturnValue({ id: null });
      jest.spyOn(jobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ job: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: job }));
      saveSubject.complete();

      // THEN
      expect(jobFormService.getJob).toHaveBeenCalled();
      expect(jobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJob>>();
      const job = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(jobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ job });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(jobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareComment', () => {
      it('Should forward to commentService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(commentService, 'compareComment');
        comp.compareComment(entity, entity2);
        expect(commentService.compareComment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRating', () => {
      it('Should forward to ratingService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(ratingService, 'compareRating');
        comp.compareRating(entity, entity2);
        expect(ratingService.compareRating).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
