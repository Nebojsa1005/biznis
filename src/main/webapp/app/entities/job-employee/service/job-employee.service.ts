import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJobEmployee, NewJobEmployee } from '../job-employee.model';

export type PartialUpdateJobEmployee = Partial<IJobEmployee> & Pick<IJobEmployee, 'id'>;

export type EntityResponseType = HttpResponse<IJobEmployee>;
export type EntityArrayResponseType = HttpResponse<IJobEmployee[]>;

@Injectable({ providedIn: 'root' })
export class JobEmployeeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/job-employees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(jobEmployee: NewJobEmployee): Observable<EntityResponseType> {
    return this.http.post<IJobEmployee>(this.resourceUrl, jobEmployee, { observe: 'response' });
  }

  update(jobEmployee: IJobEmployee): Observable<EntityResponseType> {
    return this.http.put<IJobEmployee>(`${this.resourceUrl}/${this.getJobEmployeeIdentifier(jobEmployee)}`, jobEmployee, {
      observe: 'response',
    });
  }

  partialUpdate(jobEmployee: PartialUpdateJobEmployee): Observable<EntityResponseType> {
    return this.http.patch<IJobEmployee>(`${this.resourceUrl}/${this.getJobEmployeeIdentifier(jobEmployee)}`, jobEmployee, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJobEmployee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJobEmployee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getJobEmployeeIdentifier(jobEmployee: Pick<IJobEmployee, 'id'>): number {
    return jobEmployee.id;
  }

  compareJobEmployee(o1: Pick<IJobEmployee, 'id'> | null, o2: Pick<IJobEmployee, 'id'> | null): boolean {
    return o1 && o2 ? this.getJobEmployeeIdentifier(o1) === this.getJobEmployeeIdentifier(o2) : o1 === o2;
  }

  addJobEmployeeToCollectionIfMissing<Type extends Pick<IJobEmployee, 'id'>>(
    jobEmployeeCollection: Type[],
    ...jobEmployeesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const jobEmployees: Type[] = jobEmployeesToCheck.filter(isPresent);
    if (jobEmployees.length > 0) {
      const jobEmployeeCollectionIdentifiers = jobEmployeeCollection.map(
        jobEmployeeItem => this.getJobEmployeeIdentifier(jobEmployeeItem)!
      );
      const jobEmployeesToAdd = jobEmployees.filter(jobEmployeeItem => {
        const jobEmployeeIdentifier = this.getJobEmployeeIdentifier(jobEmployeeItem);
        if (jobEmployeeCollectionIdentifiers.includes(jobEmployeeIdentifier)) {
          return false;
        }
        jobEmployeeCollectionIdentifiers.push(jobEmployeeIdentifier);
        return true;
      });
      return [...jobEmployeesToAdd, ...jobEmployeeCollection];
    }
    return jobEmployeeCollection;
  }
}
