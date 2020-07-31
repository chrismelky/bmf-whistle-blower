import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'bmf-ngx-error',
  templateUrl: './ngx-error.component.html',
})
export class NgxErrorComponent {
  @Input() label = 'Field';
  @Input() control: AbstractControl | null = null;

  isInvalidControl(): any {
    return this.control && this.control.invalid && this.control.errors && (this.control.dirty || this.control.touched);
  }
}
