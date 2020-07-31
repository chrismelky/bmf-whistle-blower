import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.WhistleBlowerCategoryModule),
      },
      {
        path: 'complain',
        loadChildren: () => import('./complain/complain.module').then(m => m.WhistleBlowerComplainModule),
      },
      {
        path: 'notification',
        loadChildren: () => import('./notification/notification.module').then(m => m.WhistleBlowerNotificationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class WhistleBlowerEntityModule {}
