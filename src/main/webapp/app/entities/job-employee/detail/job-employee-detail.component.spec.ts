import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobEmployeeDetailComponent } from './job-employee-detail.component';

describe('JobEmployee Management Detail Component', () => {
  let comp: JobEmployeeDetailComponent;
  let fixture: ComponentFixture<JobEmployeeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JobEmployeeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ jobEmployee: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(JobEmployeeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(JobEmployeeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load jobEmployee on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.jobEmployee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
