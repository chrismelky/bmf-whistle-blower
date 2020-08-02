import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IComplain } from 'app/shared/model/complain.model';
import { CommentComponent } from './comment.component';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { CommentService } from './comment.service';
import { IComment } from 'app/shared/model/comment.model';
import { ATTACHMENT_DOWNLOAD_URL } from 'app/app.constants';

@Component({
  selector: 'bmf-complain-detail',
  templateUrl: './complain-detail.component.html',
})
export class ComplainDetailComponent implements OnInit {
  complain: IComplain | null = null;
  comments: IComment[] = [];
  public downloadUrl = ATTACHMENT_DOWNLOAD_URL;

  constructor(
    protected dataUtils: JhiDataUtils,
    protected activatedRoute: ActivatedRoute,
    private commentService: CommentService,
    private commentDialog: MatBottomSheet
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ complain }) => {
      this.complain = complain;
      if (this.complain) {
        this.loadComments();
      }
    });
  }

  loadComments(): void {
    this.commentService.getByComplian(this.complain?.id!).subscribe(res => (this.comments = res.body || []));
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
    const commentRef = this.commentDialog.open(CommentComponent, {
      data: { complainId: this.complain?.id },
    });

    commentRef.afterDismissed().subscribe(resp => {
      if (resp) {
        this.comments.unshift(resp);
      }
    });
  }
}
