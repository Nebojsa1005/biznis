import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IJobEmployee } from '../job-employee.model';
import { JobEmployeeService } from '../service/job-employee.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './job-employee-delete-dialog.component.html',
})
export class JobEmployeeDeleteDialogComponent {
  jobEmployee?: IJobEmployee;

  constructor(protected jobEmployeeService: JobEmployeeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobEmployeeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
