import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'tools',
        loadChildren: () => import('./tools/tools.module').then(m => m.DevOpsMaturityToolsModule)
      },
      {
        path: 'infra',
        loadChildren: () => import('./infra/infra.module').then(m => m.DevOpsMaturityInfraModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class DevOpsMaturityEntityModule {}
