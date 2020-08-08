import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Complain } from 'app/shared/model/complain.model';

@Component({
  selector: 'bmf-complain-status',
  templateUrl: './complain-status.component.html',
})
export class ComplainStatusComponent implements OnInit {
  constructor(private dialogRef: MatDialogRef<ComplainStatusComponent>, @Inject(MAT_DIALOG_DATA) public data: Complain) {}

  ngOnInit(): void {}

  close(): void {
    this.dialogRef.close();
  }
}
