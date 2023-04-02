import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobEmployee } from '../job-employee.model';

@Component({
  selector: 'jhi-job-employee-detail',
  templateUrl: './job-employee-detail.component.html',
})
export class JobEmployeeDetailComponent implements OnInit {
  jobEmployee: IJobEmployee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobEmployee }) => {
      this.jobEmployee = jobEmployee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
