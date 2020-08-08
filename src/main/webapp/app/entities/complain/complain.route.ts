import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IComplain, Complain } from 'app/shared/model/complain.model';
import { ComplainService } from './complain.service';
import { ComplainComponent } from './complain.component';
import { ComplainDetailComponent } from './complain-detail.component';
import { ComplainUpdateComponent } from './complain-update.component';

@Injectable({ providedIn: 'root' })
export class ComplainResolve implements Resolve<IComplain> {
  constructor(private service: ComplainService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComplain> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((complain: HttpResponse<Complain>) => {
          if (complain.body) {
            return of(complain.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Complain());
  }
}

@Injectable({ providedIn: 'root' })
export class ComplainStatusResolve implements Resolve<IComplain> {
  constructor(private service: ComplainService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComplain> | Observable<never> {
    const token = route.params['controlNumber'];
    if (token) {
      return this.service.findByControlNumber(token).pipe(
        flatMap((complain: HttpResponse<Complain>) => {
          if (complain.body) {
            return of(complain.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Complain());
  }
}

export const complainRoute: Routes = [
  {
    path: '',
    component: ComplainComponent,
    data: {
      authorities: [Authority.CEO, Authority.BOARD_CHAIR],
      defaultSort: 'id,asc',
      pageTitle: 'whistleBlowerApp.complain.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComplainDetailComponent,
    resolve: {
      complain: ComplainResolve,
    },
    data: {
      authorities: [Authority.CEO, Authority.BOARD_CHAIR],
      pageTitle: 'whistleBlowerApp.complain.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':controlNumber/status',
    component: ComplainDetailComponent,
    resolve: {
      complain: ComplainStatusResolve,
    },
    data: {
      pageTitle: 'whistleBlowerApp.complain.home.title',
    },
  },
  {
    path: 'new',
    component: ComplainUpdateComponent,
    resolve: {
      complain: ComplainResolve,
    },
    data: {
      pageTitle: 'whistleBlowerApp.complain.home.title',
    },
  },
  {
    path: ':id/edit',
    component: ComplainUpdateComponent,
    resolve: {
      complain: ComplainResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'whistleBlowerApp.complain.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
