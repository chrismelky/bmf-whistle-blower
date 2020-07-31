import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators, FormArray, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiEventManager } from 'ng-jhipster';

import { IComplain, Complain } from 'app/shared/model/complain.model';
import { ComplainService } from './complain.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ATTACHMENT_DOWNLOAD_URL } from 'app/app.constants';
import { MatDialog } from '@angular/material/dialog';
import { AttachmentUpdateComponent } from './attachment-dialog.component';

type SelectableEntity = ICategory | IUser;

@Component({
  selector: 'bmf-complain-update',
  templateUrl: './complain-update.component.html',
})
export class ComplainUpdateComponent implements OnInit {
  isSaving = false;
  categories: ICategory[] = [];
  users: IUser[] = [];
  complainForm: FormGroup;

  public downloadUrl = ATTACHMENT_DOWNLOAD_URL;

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected complainService: ComplainService,
    protected categoryService: CategoryService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private dialog: MatDialog
  ) {
    this.complainForm = this.fb.group({
      id: [],
      controlNumber: [],
      description: [null, Validators.required],
      categoryId: [null, Validators.required],
      name: [],
      position: [],
      organisation: [],
      email: [],
      phoneNumber: [],
      suspects: fb.array([]),
      witnesses: fb.array([]),
      attachments: fb.array([]),
    });
  }

  get suspects(): FormArray {
    return this.complainForm.get('suspects') as FormArray;
  }

  get witnesses(): FormArray {
    return this.complainForm.get('witnesses') as FormArray;
  }

  get attachments(): FormArray {
    return this.complainForm.get('attachments') as FormArray;
  }

  newSuspect(data?: any): FormGroup {
    return this.fb.group({
      id: [data?.id],
      name: [data?.name, Validators.required],
      position: [data?.position],
      organisation: [data?.organisation],
      email: [data?.email],
      phoneNumber: [data?.phoneNumber],
    });
  }

  newWitness(data?: any): FormGroup {
    return this.fb.group({
      id: [data?.id],
      name: [data?.name, Validators.required],
      position: [data?.position],
      organisation: [data?.organisation],
      email: [data?.email],
      phoneNumber: [data?.phoneNumber],
    });
  }

  newAttachment(data?: any): FormGroup {
    return this.fb.group({
      id: [data?.id],
      name: [data?.name, Validators.required],
      contentId: [data?.contentId],
      contentLength: [data?.contentLength],
      mimeType: [data?.mimeType],
      complainId: [data?.complain?.id],
    });
  }

  addSuspect(): void {
    this.suspects.push(this.newSuspect());
  }

  addWitness(): void {
    this.witnesses.push(this.newWitness());
  }

  addAttachment(): void {
    this.attachments.push(this.newAttachment());
  }

  removeSuspect(i: number): void {
    this.suspects.removeAt(i);
  }

  removeWitness(i: number): void {
    this.witnesses.removeAt(i);
  }

  removeAttachment(i: number): void {
    this.attachments.removeAt(i);
  }

  openAttachementDialog(): void {
    const dialogRef = this.dialog.open(AttachmentUpdateComponent, {
      width: '300px',
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.attachments.push(this.newAttachment(result));
      }
    });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ complain }) => {
      this.updateForm(complain);

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(complain: IComplain): void {
    this.complainForm.patchValue({
      id: complain.id,
      controlNumber: complain.controlNumber,
      description: complain.description,
      categoryId: complain.categoryId,
      name: complain.name,
      position: complain.position,
      organisation: complain.organisation,
      email: complain.email,
      phoneNumber: complain.phoneNumber,
    });
    complain.suspects?.forEach(s => {
      this.suspects.push(this.newSuspect(s));
    });
    complain.witnesses?.forEach(w => {
      this.witnesses.push(this.newWitness(w));
    });
    complain.attachments?.forEach(a => {
      this.attachments.push(this.newAttachment(a));
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const complain = this.createFromForm();
    console.error(complain);
    if (complain.id !== undefined && complain.id !== null) {
      this.subscribeToSaveResponse(this.complainService.update(complain));
    } else {
      this.subscribeToSaveResponse(this.complainService.create(complain));
    }
  }

  private createFromForm(): IComplain {
    return {
      ...new Complain(),
      id: this.complainForm.get(['id'])!.value,
      controlNumber: this.complainForm.get(['controlNumber'])!.value,
      description: this.complainForm.get(['description'])!.value,
      categoryId: this.complainForm.get(['categoryId'])!.value,
      name: this.complainForm.get(['name'])!.value,
      position: this.complainForm.get(['position'])!.value,
      organisation: this.complainForm.get(['organisation'])!.value,
      email: this.complainForm.get(['email'])!.value,
      phoneNumber: this.complainForm.get(['phoneNumber'])!.value,
      suspects: this.complainForm.get(['suspects'])!.value,
      witnesses: this.complainForm.get(['witnesses'])!.value,
      attachments: this.complainForm.get(['attachments'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComplain>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IUser[], option: IUser): IUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
