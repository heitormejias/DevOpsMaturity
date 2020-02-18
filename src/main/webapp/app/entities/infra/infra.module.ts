import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DevOpsMaturitySharedModule } from 'app/shared/shared.module';
import { InfraComponent } from './infra.component';
import { InfraDetailComponent } from './infra-detail.component';
import { InfraUpdateComponent } from './infra-update.component';
import { InfraDeleteDialogComponent } from './infra-delete-dialog.component';
import { infraRoute } from './infra.route';

@NgModule({
  imports: [DevOpsMaturitySharedModule, RouterModule.forChild(infraRoute)],
  declarations: [InfraComponent, InfraDetailComponent, InfraUpdateComponent, InfraDeleteDialogComponent],
  entryComponents: [InfraDeleteDialogComponent]
})
export class DevOpsMaturityInfraModule {}
