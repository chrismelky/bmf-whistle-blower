import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest, Observable } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IComplain } from 'app/shared/model/complain.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ComplainService } from './complain.service';
import { ComplainDeleteDialogComponent } from './complain-delete-dialog.component';
import { LocalStorageService } from 'ngx-webstorage';
import { PageEvent } from '@angular/material/paginator';
import { CategoryService } from '../category/category.service';
import { ICategory } from 'app/shared/model/category.model';
import { BreakpointObserver } from '@angular/cdk/layout';
import { map } from 'rxjs/operators';

@Component({
  selector: 'bmf-complain',
  templateUrl: './complain.component.html',
})
export class ComplainComponent implements OnInit, OnDestroy {
  complains?: IComplain[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  pageSizeOptions: number[] = [5, 10, 20, 50, 100];
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  account: Account | null = null;
  displayedColumns = ['createdDate', 'controlNumber', 'name', 'description', 'status', 'category', 'formActions'];
  filter: any = {};
  categories: ICategory[] = [];
  expanded = false;
  gtMd!: Observable<boolean>;

  constructor(
    protected complainService: ComplainService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected localStorage: LocalStorageService,
    protected modalService: NgbModal,
    protected categoryService: CategoryService,
    private breakPointObsever: BreakpointObserver
  ) {
    this.account = localStorage.retrieve('user');
    this.gtMd = this.breakPointObsever.observe('(max-width: 959px)').pipe(map(result => !result.matches));
    this.gtMd.subscribe(value => {
      if (!value) {
        this.displayedColumns = ['description', 'formActions'];
      } else {
        this.displayedColumns = ['createdDate', 'controlNumber', 'name', 'description', 'status', 'category', 'formActions'];
      }
    });
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    if (this.account == null) {
      return;
    }
    const pageToLoad: number = page || this.page || 1;

    this.complainService
      .query({
        'receiversId.equals': this.account.id,
        ...this.filter,
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IComplain[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInComplains();
    this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
  }

  clearFilter(): void {
    this.filter = {};
    this.loadPage(0);
  }

  search(): void {
    this.loadPage(0);
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IComplain): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInComplains(): void {
    this.eventSubscriber = this.eventManager.subscribe('complainListModification', () => this.loadPage());
  }

  delete(complain: IComplain): void {
    const modalRef = this.modalService.open(ComplainDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.complain = complain;
  }

  pageChange($event: PageEvent): void {
    this.itemsPerPage = $event.pageSize;
    this.page = $event.pageIndex;
    this.loadPage(this.page, false);
  }

  sort(): string[] {
    return ['createdDate,desc'];
    // const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    // if (this.predicate !== 'id') {
    //   result.push('id');
    // }
    // return result;
  }

  protected onSuccess(data: IComplain[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/complain'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.complains = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
