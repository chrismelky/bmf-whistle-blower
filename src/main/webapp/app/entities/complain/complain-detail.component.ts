import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IComplain } from 'app/shared/model/complain.model';
import { CommentComponent } from './comment.component';
import { MatBottomSheet } from '@angular/material/bottom-sheet';

@Component({
  selector: 'bmf-complain-detail',
  templateUrl: './complain-detail.component.html',
})
export class ComplainDetailComponent implements OnInit {
  complain: IComplain | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute, private commentDialog: MatBottomSheet) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ complain }) => (this.complain = complain));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }

  addComment(): void {
    const commentRef = this.commentDialog.open(CommentComponent);

    commentRef.afterDismissed().subscribe(() => {});
  }
}
