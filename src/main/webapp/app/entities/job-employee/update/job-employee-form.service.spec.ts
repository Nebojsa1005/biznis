import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../job-employee.test-samples';

import { JobEmployeeFormService } from './job-employee-form.service';

describe('JobEmployee Form Service', () => {
  let service: JobEmployeeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobEmployeeFormService);
  });

  describe('Service methods', () => {
    describe('createJobEmployeeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createJobEmployeeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            number: expect.any(Object),
            found: expect.any(Object),
            job: expect.any(Object),
          })
        );
      });

      it('passing IJobEmployee should create a new form with FormGroup', () => {
        const formGroup = service.createJobEmployeeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            number: expect.any(Object),
            found: expect.any(Object),
            job: expect.any(Object),
          })
        );
      });
    });

    describe('getJobEmployee', () => {
      it('should return NewJobEmployee for default JobEmployee initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createJobEmployeeFormGroup(sampleWithNewData);

        const jobEmployee = service.getJobEmployee(formGroup) as any;

        expect(jobEmployee).toMatchObject(sampleWithNewData);
      });

      it('should return NewJobEmployee for empty JobEmployee initial value', () => {
        const formGroup = service.createJobEmployeeFormGroup();

        const jobEmployee = service.getJobEmployee(formGroup) as any;

        expect(jobEmployee).toMatchObject({});
      });

      it('should return IJobEmployee', () => {
        const formGroup = service.createJobEmployeeFormGroup(sampleWithRequiredData);

        const jobEmployee = service.getJobEmployee(formGroup) as any;

        expect(jobEmployee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IJobEmployee should not enable id FormControl', () => {
        const formGroup = service.createJobEmployeeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewJobEmployee should disable id FormControl', () => {
        const formGroup = service.createJobEmployeeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
