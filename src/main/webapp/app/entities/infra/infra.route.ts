import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInfra, Infra } from 'app/shared/model/infra.model';
import { InfraService } from './infra.service';
import { InfraComponent } from './infra.component';
import { InfraDetailComponent } from './infra-detail.component';
import { InfraUpdateComponent } from './infra-update.component';

@Injectable({ providedIn: 'root' })
export class InfraResolve implements Resolve<IInfra> {
  constructor(private service: InfraService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((infra: HttpResponse<Infra>) => {
          if (infra.body) {
            return of(infra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Infra());
  }
}

export const infraRoute: Routes = [
  {
    path: '',
    component: InfraComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.infra.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InfraDetailComponent,
    resolve: {
      infra: InfraResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.infra.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InfraUpdateComponent,
    resolve: {
      infra: InfraResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.infra.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InfraUpdateComponent,
    resolve: {
      infra: InfraResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.infra.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
