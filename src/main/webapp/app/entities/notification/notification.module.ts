import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WhistleBlowerSharedModule } from 'app/shared/shared.module';
import { NotificationComponent } from './notification.component';
import { NotificationDetailComponent } from './notification-detail.component';
import { notificationRoute } from './notification.route';

@NgModule({
  imports: [WhistleBlowerSharedModule, RouterModule.forChild(notificationRoute)],
  declarations: [NotificationComponent, NotificationDetailComponent],
})
export class WhistleBlowerNotificationModule {}
