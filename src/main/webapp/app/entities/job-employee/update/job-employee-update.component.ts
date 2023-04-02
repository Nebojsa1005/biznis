import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { JobEmployeeFormService, JobEmployeeFormGroup } from './job-employee-form.service';
import { IJobEmployee } from '../job-employee.model';
import { JobEmployeeService } from '../service/job-employee.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { EmployeeType } from 'app/entities/enumerations/employee-type.model';

@Component({
  selector: 'jhi-job-employee-update',
  templateUrl: './job-employee-update.component.html',
})
export class JobEmployeeUpdateComponent implements OnInit {
  isSaving = false;
  jobEmployee: IJobEmployee | null = null;
  employeeTypeValues = Object.keys(EmployeeType);

  jobsSharedCollection: IJob[] = [];

  editForm: JobEmployeeFormGroup = this.jobEmployeeFormService.createJobEmployeeFormGroup();

  constructor(
    protected jobEmployeeService: JobEmployeeService,
    protected jobEmployeeFormService: JobEmployeeFormService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareJob = (o1: IJob | null, o2: IJob | null): boolean => this.jobService.compareJob(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobEmployee }) => {
      this.jobEmployee = jobEmployee;
      if (jobEmployee) {
        this.updateForm(jobEmployee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobEmployee = this.jobEmployeeFormService.getJobEmployee(this.editForm);
    if (jobEmployee.id !== null) {
      this.subscribeToSaveResponse(this.jobEmployeeService.update(jobEmployee));
    } else {
      this.subscribeToSaveResponse(this.jobEmployeeService.create(jobEmployee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobEmployee>>): void {
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

  protected updateForm(jobEmployee: IJobEmployee): void {
    this.jobEmployee = jobEmployee;
    this.jobEmployeeFormService.resetForm(this.editForm, jobEmployee);

    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing<IJob>(this.jobsSharedCollection, jobEmployee.job);
  }

  protected loadRelationshipsOptions(): void {
    this.jobService
      .query()
      .pipe(map((res: HttpResponse<IJob[]>) => res.body ?? []))
      .pipe(map((jobs: IJob[]) => this.jobService.addJobToCollectionIfMissing<IJob>(jobs, this.jobEmployee?.job)))
      .subscribe((jobs: IJob[]) => (this.jobsSharedCollection = jobs));
  }
}
