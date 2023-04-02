import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { JobEmployeeComponent } from './list/job-employee.component';
import { JobEmployeeDetailComponent } from './detail/job-employee-detail.component';
import { JobEmployeeUpdateComponent } from './update/job-employee-update.component';
import { JobEmployeeDeleteDialogComponent } from './delete/job-employee-delete-dialog.component';
import { JobEmployeeRoutingModule } from './route/job-employee-routing.module';

@NgModule({
  imports: [SharedModule, JobEmployeeRoutingModule],
  declarations: [JobEmployeeComponent, JobEmployeeDetailComponent, JobEmployeeUpdateComponent, JobEmployeeDeleteDialogComponent],
})
export class JobEmployeeModule {}
