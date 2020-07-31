import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WhistleBlowerSharedModule } from 'app/shared/shared.module';
import { ComplainComponent } from './complain.component';
import { ComplainDetailComponent } from './complain-detail.component';
import { ComplainUpdateComponent } from './complain-update.component';
import { ComplainDeleteDialogComponent } from './complain-delete-dialog.component';
import { complainRoute } from './complain.route';
import { AttachmentUpdateComponent } from './attachment-dialog.component';

@NgModule({
  imports: [WhistleBlowerSharedModule, RouterModule.forChild(complainRoute)],
  declarations: [
    ComplainComponent,
    ComplainDetailComponent,
    ComplainUpdateComponent,
    ComplainDeleteDialogComponent,
    AttachmentUpdateComponent,
  ],
  entryComponents: [ComplainDeleteDialogComponent],
})
export class WhistleBlowerComplainModule {}
