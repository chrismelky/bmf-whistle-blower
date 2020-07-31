import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars

import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'bmf-attachment-update',
  templateUrl: './attachment-dialog.component.html',
})
export class AttachmentUpdateComponent implements OnInit {
  constructor(private dialogRef: MatDialogRef<AttachmentUpdateComponent>) {}

  ngOnInit(): void {}

  onUploaded(attachment: any): void {
    this.dialogRef.close(attachment);
  }
}
