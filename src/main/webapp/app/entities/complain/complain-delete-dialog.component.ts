import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IComplain } from 'app/shared/model/complain.model';
import { ComplainService } from './complain.service';

@Component({
  templateUrl: './complain-delete-dialog.component.html',
})
export class ComplainDeleteDialogComponent {
  complain?: IComplain;

  constructor(protected complainService: ComplainService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.complainService.delete(id).subscribe(() => {
      this.eventManager.broadcast('complainListModification');
      this.activeModal.close();
    });
  }
}
