import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JobEmployeeFormService } from './job-employee-form.service';
import { JobEmployeeService } from '../service/job-employee.service';
import { IJobEmployee } from '../job-employee.model';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';

import { JobEmployeeUpdateComponent } from './job-employee-update.component';

describe('JobEmployee Management Update Component', () => {
  let comp: JobEmployeeUpdateComponent;
  let fixture: ComponentFixture<JobEmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let jobEmployeeFormService: JobEmployeeFormService;
  let jobEmployeeService: JobEmployeeService;
  let jobService: JobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [JobEmployeeUpdateComponent],
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
      .overrideTemplate(JobEmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JobEmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    jobEmployeeFormService = TestBed.inject(JobEmployeeFormService);
    jobEmployeeService = TestBed.inject(JobEmployeeService);
    jobService = TestBed.inject(JobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Job query and add missing value', () => {
      const jobEmployee: IJobEmployee = { id: 456 };
      const job: IJob = { id: '5be36895-7e27-428f-ab35-ba07e82d3ebf' };
      jobEmployee.job = job;

      const jobCollection: IJob[] = [{ id: '3fa52ea1-ccc5-4dfe-8521-d6834419956e' }];
      jest.spyOn(jobService, 'query').mockReturnValue(of(new HttpResponse({ body: jobCollection })));
      const additionalJobs = [job];
      const expectedCollection: IJob[] = [...additionalJobs, ...jobCollection];
      jest.spyOn(jobService, 'addJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jobEmployee });
      comp.ngOnInit();

      expect(jobService.query).toHaveBeenCalled();
      expect(jobService.addJobToCollectionIfMissing).toHaveBeenCalledWith(jobCollection, ...additionalJobs.map(expect.objectContaining));
      expect(comp.jobsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const jobEmployee: IJobEmployee = { id: 456 };
      const job: IJob = { id: '7888e02d-70c1-4901-9987-6c223eb991da' };
      jobEmployee.job = job;

      activatedRoute.data = of({ jobEmployee });
      comp.ngOnInit();

      expect(comp.jobsSharedCollection).toContain(job);
      expect(comp.jobEmployee).toEqual(jobEmployee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJobEmployee>>();
      const jobEmployee = { id: 123 };
      jest.spyOn(jobEmployeeFormService, 'getJobEmployee').mockReturnValue(jobEmployee);
      jest.spyOn(jobEmployeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobEmployee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobEmployee }));
      saveSubject.complete();

      // THEN
      expect(jobEmployeeFormService.getJobEmployee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(jobEmployeeService.update).toHaveBeenCalledWith(expect.objectContaining(jobEmployee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJobEmployee>>();
      const jobEmployee = { id: 123 };
      jest.spyOn(jobEmployeeFormService, 'getJobEmployee').mockReturnValue({ id: null });
      jest.spyOn(jobEmployeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobEmployee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobEmployee }));
      saveSubject.complete();

      // THEN
      expect(jobEmployeeFormService.getJobEmployee).toHaveBeenCalled();
      expect(jobEmployeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJobEmployee>>();
      const jobEmployee = { id: 123 };
      jest.spyOn(jobEmployeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobEmployee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(jobEmployeeService.update).toHaveBeenCalled();
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
  });
});
