import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpEventType, HttpRequest } from '@angular/common/http';
import { of, Subscription } from 'rxjs';
import { catchError, last, map, tap } from 'rxjs/operators';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'bmf-file-upload',
  templateUrl: './file-upload.component.html',
  animations: [
    trigger('fadeInOut', [state('in', style({ opacity: 100 })), transition('* => void', [animate(300, style({ opacity: 0 }))])]),
  ],
})
export class FileUploadComponent implements OnInit {
  /** Name/Title of the file */
  @Input() name: string | undefined;
  /** Default tags */
  @Input() tags: string | undefined;
  /** If to add name while uploading */
  @Input() inputName: boolean | undefined;
  /** If to add tags while uploading */
  @Input() inputTags: boolean | undefined;
  /** if upload multiple files */
  @Input() multiple: boolean | undefined;
  /** Files already uploaded */
  @Input() uploaded: any;
  public files: Array<FileUploadModel> = [];
  /** File type to accept */
  @Input() accept = '.pdf,.doc,.docx,.txt,audio/*,video/*,image/*';
  /** File upload url */
  url = '/api/attachments/upload';
  /** Event fired with file file resource object to parent component after successfully upload */
  @Output() complete = new EventEmitter<any>();

  constructor(private http: HttpClient) {}

  ngOnInit(): void {}

  onClick(): void {
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.onchange = () => {
      for (let index = 0; index < fileUpload.files!.length; index++) {
        const file = fileUpload.files![index];
        this.files.push({
          data: file,
          name: this.name!,
          tags: this.tags!,
          state: 'in',
          inProgress: false,
          progress: 0,
          canRetry: false,
          canCancel: true,
        });
      }
      this.uploadFiles();
    };
    fileUpload.click();
  }

  private uploadFile(file: FileUploadModel): void {
    const fd = new FormData();
    fd.append('file', file.data!);
    fd.append('name', file.name!);

    const req = new HttpRequest('POST', this.url, fd, {
      reportProgress: true,
    });

    file.inProgress = true;
    file.sub = this.http
      .request(req)
      .pipe(
        map(event => {
          switch (event.type) {
            case HttpEventType.UploadProgress:
              file.progress = Math.round((event.loaded * 100) / event.total!);
              return;
            case HttpEventType.Response:
              return event;
            default:
              return;
          }
        }),
        tap(message => message),
        last(),
        catchError((error: HttpErrorResponse) => {
          console.error(error);
          file.inProgress = false;
          file.canRetry = true;
          return of(`${file.data!.name} upload failed.`);
        })
      )
      .subscribe((event: any) => {
        if (typeof event === 'object') {
          this.removeFileFromArray(file);
          this.complete.emit(event.body);
        }
      });
  }

  uploadFiles(): void {
    this.files.forEach(file => {
      this.uploadFile(file);
    });
    /** Clear inputs to allow another upload */
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.value = '';
    this.name = this.inputName ? undefined : this.name!;
    this.tags = this.inputTags ? undefined : this.tags!;
  }

  cancelFile(file: FileUploadModel): void {
    file.sub!.unsubscribe();
    this.removeFileFromArray(file);
  }

  retryFile(file: FileUploadModel): void {
    this.uploadFile(file);
    file.canRetry = false;
  }

  private removeFileFromArray(file: FileUploadModel): void {
    const index = this.files.indexOf(file);
    if (index > -1) {
      this.files.splice(index, 1);
    }
  }
}

export class FileUploadModel {
  data: File | undefined;
  name: string | undefined;
  tags: string | undefined;
  state: string | undefined;
  inProgress: boolean | undefined;
  progress: number | undefined;
  canRetry: boolean | undefined;
  canCancel: boolean | undefined;
  sub?: Subscription;
}
