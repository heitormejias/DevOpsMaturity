import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITools, Tools } from 'app/shared/model/tools.model';
import { ToolsService } from './tools.service';

@Component({
  selector: 'jhi-tools-update',
  templateUrl: './tools-update.component.html'
})
export class ToolsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(protected toolsService: ToolsService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tools }) => {
      this.updateForm(tools);
    });
  }

  updateForm(tools: ITools): void {
    this.editForm.patchValue({
      id: tools.id,
      name: tools.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tools = this.createFromForm();
    if (tools.id !== undefined) {
      this.subscribeToSaveResponse(this.toolsService.update(tools));
    } else {
      this.subscribeToSaveResponse(this.toolsService.create(tools));
    }
  }

  private createFromForm(): ITools {
    return {
      ...new Tools(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITools>>): void {
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
}
