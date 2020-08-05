import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { WhistleBlowerSharedModule } from 'app/shared/shared.module';
import { WhistleBlowerCoreModule } from 'app/core/core.module';
import { WhistleBlowerAppRoutingModule } from './app-routing.module';
import { WhistleBlowerHomeModule } from './home/home.module';
import { WhistleBlowerEntityModule } from './entities/entity.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';
import 'hammerjs';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { ToastrModule } from 'ngx-toastr';
import { AvatarModule } from 'ngx-avatar';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    WhistleBlowerSharedModule,
    WhistleBlowerCoreModule,
    WhistleBlowerHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    WhistleBlowerEntityModule,
    WhistleBlowerAppRoutingModule,
    AvatarModule,
    ToastrModule.forRoot(),
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class WhistleBlowerAppModule {}
