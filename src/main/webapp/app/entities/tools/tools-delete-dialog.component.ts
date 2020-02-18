import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITools } from 'app/shared/model/tools.model';
import { ToolsService } from './tools.service';

@Component({
  templateUrl: './tools-delete-dialog.component.html'
})
export class ToolsDeleteDialogComponent {
  tools?: ITools;

  constructor(protected toolsService: ToolsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.toolsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('toolsListModification');
      this.activeModal.close();
    });
  }
}
