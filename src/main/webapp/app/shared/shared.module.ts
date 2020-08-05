import { NgModule } from '@angular/core';
import { WhistleBlowerSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { FileUploadComponent } from './upload/file-upload.component';
import { NgxErrorComponent } from './ngx-error/ngx-error.component';

import { JhMaterialModule } from 'app/shared/jh-material.module';

@NgModule({
  imports: [JhMaterialModule, WhistleBlowerSharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    FileUploadComponent,
    NgxErrorComponent,
  ],
  exports: [
    JhMaterialModule,
    WhistleBlowerSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    FileUploadComponent,
    NgxErrorComponent,
  ],
})
export class WhistleBlowerSharedModule {}
