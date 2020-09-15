import { Component, OnInit, RendererFactory2, Renderer2, ViewChild, TemplateRef } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { Account } from 'app/core/user/account.model';
import { ComplainService } from 'app/entities/complain/complain.service';
import { ProfileService } from '../profiles/profile.service';
import { MatDialog } from '@angular/material/dialog';
import { ComplainStatusComponent } from 'app/entities/complain/complain-status.component';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { BreakpointObserver } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LANGUAGES } from 'app/core/language/language.constants';
import { JhiLanguageService } from 'ng-jhipster';

@Component({
  selector: 'bmf-main',
  templateUrl: './main.component.html',
})
export class MainComponent implements OnInit {
  private renderer: Renderer2;
  account: Account | null = null;
  totalComplains = '0';
  inProduction: boolean | undefined = false;
  isSearchBar = false;
  @ViewChild('searchBar')
  searchBar!: TemplateRef<any>;
  dialogRef: any;
  gtMd!: Observable<boolean>;
  isGtMd = true;
  languages = LANGUAGES;

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private translateService: TranslateService,
    private localStorage: LocalStorageService,
    rootRenderer: RendererFactory2,
    private loginService: LoginService,
    public languageService: JhiLanguageService,
    private sessionStorage: SessionStorageService,
    private complainService: ComplainService,
    private profileService: ProfileService,
    private dialog: MatDialog,
    private toastr: ToastrService,
    private breakPointObsever: BreakpointObserver
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
    this.gtMd = this.breakPointObsever.observe('(max-width: 959px)').pipe(map(result => !result.matches));
    this.gtMd.subscribe(value => {
      this.isGtMd = value;
    });
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(inf => {
      this.inProduction = inf.inProduction;
    });
    // try to log in automatically
    this.accountService.identity().subscribe(res => {
      this.account = res || null;
      if (this.account !== null) {
        this.getTotalComplains();
      }
    });

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();

      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorage.store('locale', languageKey);
    this.languageService.changeLanguage(languageKey);
  }

  openStatusDialog(search?: string): void {
    if (search?.length! > 0) {
      this.complainService.findByControlNumber(search!).subscribe(
        result => {
          const dialogRef = this.dialog.open(ComplainStatusComponent, {
            width: '400px',
            data: result.body,
          });

          dialogRef.afterClosed().subscribe(() => {});
        },
        (error: HttpErrorResponse) => {
          if (error.status === 404) {
            this.toastr.warning(`No Complain found with token ${search}`, 'Not found', { timeOut: 3000 });
          }
        }
      );
    }
  }

  getTotalComplains(): void {
    this.complainService
      .count({
        'receiversId.equals': this.localStorage.retrieve('user')?.id,
      })
      .subscribe(resp => {
        this.totalComplains = resp.toString();
      });
  }

  openSeachBar(): void {
    this.dialogRef = this.dialog.open(this.searchBar, {
      position: {
        top: '0px',
      },
      width: '100%',
      hasBackdrop: true,
      id: 'searchBar',
      panelClass: 'searchBarPanel',
      backdropClass: 'searchBarBackdrop',
    });
    this.dialogRef.afterClosed().subscribe(() => {});
  }
  closeSearchBar(): void {
    this.dialogRef.close();
  }
  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginService.login();
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
