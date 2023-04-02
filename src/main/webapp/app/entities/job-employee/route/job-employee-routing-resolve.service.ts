import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJobEmployee } from '../job-employee.model';
import { JobEmployeeService } from '../service/job-employee.service';

@Injectable({ providedIn: 'root' })
export class JobEmployeeRoutingResolveService implements Resolve<IJobEmployee | null> {
  constructor(protected service: JobEmployeeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobEmployee | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jobEmployee: HttpResponse<IJobEmployee>) => {
          if (jobEmployee.body) {
            return of(jobEmployee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
