import { Component, OnInit, Inject } from '@angular/core';
import { CommentService } from './comment.service';
import { MatBottomSheetRef, MAT_BOTTOM_SHEET_DATA } from '@angular/material/bottom-sheet';

@Component({
  selector: 'bmf-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss'],
})
export class CommentComponent implements OnInit {
  description: string | undefined;

  constructor(
    private commentService: CommentService,
    private dialogRef: MatBottomSheetRef<CommentComponent>,
    @Inject(MAT_BOTTOM_SHEET_DATA) public data: any
  ) {}

  ngOnInit(): void {}

  save(): void {
    this.commentService
      .create({
        description: this.description,
        complainId: this.data.complainId,
      })
      .subscribe(resp => {
        this.dialogRef.dismiss(resp.body);
      });
  }
  cancel(): void {
    this.dialogRef.dismiss();
  }
}
