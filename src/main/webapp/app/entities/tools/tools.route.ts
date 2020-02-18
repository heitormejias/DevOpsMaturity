import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITools, Tools } from 'app/shared/model/tools.model';
import { ToolsService } from './tools.service';
import { ToolsComponent } from './tools.component';
import { ToolsDetailComponent } from './tools-detail.component';
import { ToolsUpdateComponent } from './tools-update.component';

@Injectable({ providedIn: 'root' })
export class ToolsResolve implements Resolve<ITools> {
  constructor(private service: ToolsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITools> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((tools: HttpResponse<Tools>) => {
          if (tools.body) {
            return of(tools.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tools());
  }
}

export const toolsRoute: Routes = [
  {
    path: '',
    component: ToolsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.tools.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ToolsDetailComponent,
    resolve: {
      tools: ToolsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.tools.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ToolsUpdateComponent,
    resolve: {
      tools: ToolsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.tools.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ToolsUpdateComponent,
    resolve: {
      tools: ToolsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'devOpsMaturityApp.tools.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
