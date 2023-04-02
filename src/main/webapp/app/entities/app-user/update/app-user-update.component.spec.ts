import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AppUserFormService } from './app-user-form.service';
import { AppUserService } from '../service/app-user.service';
import { IAppUser } from '../app-user.model';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';
import { IRating } from 'app/entities/rating/rating.model';
import { RatingService } from 'app/entities/rating/service/rating.service';

import { AppUserUpdateComponent } from './app-user-update.component';

describe('AppUser Management Update Component', () => {
  let comp: AppUserUpdateComponent;
  let fixture: ComponentFixture<AppUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appUserFormService: AppUserFormService;
  let appUserService: AppUserService;
  let jobService: JobService;
  let commentService: CommentService;
  let ratingService: RatingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AppUserUpdateComponent],
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
      .overrideTemplate(AppUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appUserFormService = TestBed.inject(AppUserFormService);
    appUserService = TestBed.inject(AppUserService);
    jobService = TestBed.inject(JobService);
    commentService = TestBed.inject(CommentService);
    ratingService = TestBed.inject(RatingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Job query and add missing value', () => {
      const appUser: IAppUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const jobs: IJob[] = [{ id: '632ce3fa-424c-4080-8e9b-f753c1024b08' }];
      appUser.jobs = jobs;

      const jobCollection: IJob[] = [{ id: '57aab1f9-5727-4cc5-b620-7e0946fb8095' }];
      jest.spyOn(jobService, 'query').mockReturnValue(of(new HttpResponse({ body: jobCollection })));
      const additionalJobs = [...jobs];
      const expectedCollection: IJob[] = [...additionalJobs, ...jobCollection];
      jest.spyOn(jobService, 'addJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(jobService.query).toHaveBeenCalled();
      expect(jobService.addJobToCollectionIfMissing).toHaveBeenCalledWith(jobCollection, ...additionalJobs.map(expect.objectContaining));
      expect(comp.jobsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Comment query and add missing value', () => {
      const appUser: IAppUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const comments: IComment[] = [{ id: '432432f7-7f6a-4150-bc7c-ddc50e95ec04' }];
      appUser.comments = comments;

      const commentCollection: IComment[] = [{ id: '51c88caf-31cf-4ad5-8ebd-a004c14bbbd0' }];
      jest.spyOn(commentService, 'query').mockReturnValue(of(new HttpResponse({ body: commentCollection })));
      const additionalComments = [...comments];
      const expectedCollection: IComment[] = [...additionalComments, ...commentCollection];
      jest.spyOn(commentService, 'addCommentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(commentService.query).toHaveBeenCalled();
      expect(commentService.addCommentToCollectionIfMissing).toHaveBeenCalledWith(
        commentCollection,
        ...additionalComments.map(expect.objectContaining)
      );
      expect(comp.commentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rating query and add missing value', () => {
      const appUser: IAppUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const ratings: IRating[] = [{ id: '9f76dc76-543a-4476-91e1-ada173ee8008' }];
      appUser.ratings = ratings;

      const ratingCollection: IRating[] = [{ id: 'c7a4b845-059e-452f-8deb-1dc4afffff08' }];
      jest.spyOn(ratingService, 'query').mockReturnValue(of(new HttpResponse({ body: ratingCollection })));
      const additionalRatings = [...ratings];
      const expectedCollection: IRating[] = [...additionalRatings, ...ratingCollection];
      jest.spyOn(ratingService, 'addRatingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(ratingService.query).toHaveBeenCalled();
      expect(ratingService.addRatingToCollectionIfMissing).toHaveBeenCalledWith(
        ratingCollection,
        ...additionalRatings.map(expect.objectContaining)
      );
      expect(comp.ratingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appUser: IAppUser = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const job: IJob = { id: '0402c26d-366e-440e-b196-ffb51da068ce' };
      appUser.jobs = [job];
      const comment: IComment = { id: '858ab89a-5ec7-4f46-ac6d-d6c1d8730b6c' };
      appUser.comments = [comment];
      const rating: IRating = { id: '6420de53-eaa1-4074-80c5-3bb9584337ee' };
      appUser.ratings = [rating];

      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      expect(comp.jobsSharedCollection).toContain(job);
      expect(comp.commentsSharedCollection).toContain(comment);
      expect(comp.ratingsSharedCollection).toContain(rating);
      expect(comp.appUser).toEqual(appUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppUser>>();
      const appUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(appUserFormService, 'getAppUser').mockReturnValue(appUser);
      jest.spyOn(appUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appUser }));
      saveSubject.complete();

      // THEN
      expect(appUserFormService.getAppUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appUserService.update).toHaveBeenCalledWith(expect.objectContaining(appUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppUser>>();
      const appUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(appUserFormService, 'getAppUser').mockReturnValue({ id: null });
      jest.spyOn(appUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appUser }));
      saveSubject.complete();

      // THEN
      expect(appUserFormService.getAppUser).toHaveBeenCalled();
      expect(appUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppUser>>();
      const appUser = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(appUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareJob', () => {
      it('Should forward to jobService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(jobService, 'compareJob');
        comp.compareJob(entity, entity2);
        expect(jobService.compareJob).toHaveBeenCalledWith(entity, entity2);
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
