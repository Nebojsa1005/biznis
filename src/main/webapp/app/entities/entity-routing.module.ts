import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'app-user',
        data: { pageTitle: 'serveUpApp.appUser.home.title' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'serveUpApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'job-employee',
        data: { pageTitle: 'serveUpApp.jobEmployee.home.title' },
        loadChildren: () => import('./job-employee/job-employee.module').then(m => m.JobEmployeeModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'serveUpApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'rating',
        data: { pageTitle: 'serveUpApp.rating.home.title' },
        loadChildren: () => import('./rating/rating.module').then(m => m.RatingModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'serveUpApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
