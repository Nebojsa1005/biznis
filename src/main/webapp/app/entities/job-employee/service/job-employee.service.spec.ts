import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IJobEmployee } from '../job-employee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../job-employee.test-samples';

import { JobEmployeeService } from './job-employee.service';

const requireRestSample: IJobEmployee = {
  ...sampleWithRequiredData,
};

describe('JobEmployee Service', () => {
  let service: JobEmployeeService;
  let httpMock: HttpTestingController;
  let expectedResult: IJobEmployee | IJobEmployee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JobEmployeeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a JobEmployee', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const jobEmployee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(jobEmployee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a JobEmployee', () => {
      const jobEmployee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(jobEmployee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a JobEmployee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of JobEmployee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a JobEmployee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addJobEmployeeToCollectionIfMissing', () => {
      it('should add a JobEmployee to an empty array', () => {
        const jobEmployee: IJobEmployee = sampleWithRequiredData;
        expectedResult = service.addJobEmployeeToCollectionIfMissing([], jobEmployee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobEmployee);
      });

      it('should not add a JobEmployee to an array that contains it', () => {
        const jobEmployee: IJobEmployee = sampleWithRequiredData;
        const jobEmployeeCollection: IJobEmployee[] = [
          {
            ...jobEmployee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addJobEmployeeToCollectionIfMissing(jobEmployeeCollection, jobEmployee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a JobEmployee to an array that doesn't contain it", () => {
        const jobEmployee: IJobEmployee = sampleWithRequiredData;
        const jobEmployeeCollection: IJobEmployee[] = [sampleWithPartialData];
        expectedResult = service.addJobEmployeeToCollectionIfMissing(jobEmployeeCollection, jobEmployee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobEmployee);
      });

      it('should add only unique JobEmployee to an array', () => {
        const jobEmployeeArray: IJobEmployee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const jobEmployeeCollection: IJobEmployee[] = [sampleWithRequiredData];
        expectedResult = service.addJobEmployeeToCollectionIfMissing(jobEmployeeCollection, ...jobEmployeeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const jobEmployee: IJobEmployee = sampleWithRequiredData;
        const jobEmployee2: IJobEmployee = sampleWithPartialData;
        expectedResult = service.addJobEmployeeToCollectionIfMissing([], jobEmployee, jobEmployee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobEmployee);
        expect(expectedResult).toContain(jobEmployee2);
      });

      it('should accept null and undefined values', () => {
        const jobEmployee: IJobEmployee = sampleWithRequiredData;
        expectedResult = service.addJobEmployeeToCollectionIfMissing([], null, jobEmployee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobEmployee);
      });

      it('should return initial array if no JobEmployee is added', () => {
        const jobEmployeeCollection: IJobEmployee[] = [sampleWithRequiredData];
        expectedResult = service.addJobEmployeeToCollectionIfMissing(jobEmployeeCollection, undefined, null);
        expect(expectedResult).toEqual(jobEmployeeCollection);
      });
    });

    describe('compareJobEmployee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareJobEmployee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareJobEmployee(entity1, entity2);
        const compareResult2 = service.compareJobEmployee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareJobEmployee(entity1, entity2);
        const compareResult2 = service.compareJobEmployee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareJobEmployee(entity1, entity2);
        const compareResult2 = service.compareJobEmployee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
