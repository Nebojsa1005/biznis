import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JobEmployeeComponent } from '../list/job-employee.component';
import { JobEmployeeDetailComponent } from '../detail/job-employee-detail.component';
import { JobEmployeeUpdateComponent } from '../update/job-employee-update.component';
import { JobEmployeeRoutingResolveService } from './job-employee-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const jobEmployeeRoute: Routes = [
  {
    path: '',
    component: JobEmployeeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobEmployeeDetailComponent,
    resolve: {
      jobEmployee: JobEmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobEmployeeUpdateComponent,
    resolve: {
      jobEmployee: JobEmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobEmployeeUpdateComponent,
    resolve: {
      jobEmployee: JobEmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jobEmployeeRoute)],
  exports: [RouterModule],
})
export class JobEmployeeRoutingModule {}
