import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInfra } from 'app/shared/model/infra.model';
import { InfraService } from './infra.service';

@Component({
  templateUrl: './infra-delete-dialog.component.html'
})
export class InfraDeleteDialogComponent {
  infra?: IInfra;

  constructor(protected infraService: InfraService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infraService.delete(id).subscribe(() => {
      this.eventManager.broadcast('infraListModification');
      this.activeModal.close();
    });
  }
}
