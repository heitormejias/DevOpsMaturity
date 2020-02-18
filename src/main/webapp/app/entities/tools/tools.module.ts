import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DevOpsMaturitySharedModule } from 'app/shared/shared.module';
import { ToolsComponent } from './tools.component';
import { ToolsDetailComponent } from './tools-detail.component';
import { ToolsUpdateComponent } from './tools-update.component';
import { ToolsDeleteDialogComponent } from './tools-delete-dialog.component';
import { toolsRoute } from './tools.route';

@NgModule({
  imports: [DevOpsMaturitySharedModule, RouterModule.forChild(toolsRoute)],
  declarations: [ToolsComponent, ToolsDetailComponent, ToolsUpdateComponent, ToolsDeleteDialogComponent],
  entryComponents: [ToolsDeleteDialogComponent]
})
export class DevOpsMaturityToolsModule {}
