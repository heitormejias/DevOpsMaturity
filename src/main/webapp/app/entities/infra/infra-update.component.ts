import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInfra, Infra } from 'app/shared/model/infra.model';
import { InfraService } from './infra.service';

@Component({
  selector: 'jhi-infra-update',
  templateUrl: './infra-update.component.html'
})
export class InfraUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(protected infraService: InfraService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infra }) => {
      this.updateForm(infra);
    });
  }

  updateForm(infra: IInfra): void {
    this.editForm.patchValue({
      id: infra.id,
      name: infra.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infra = this.createFromForm();
    if (infra.id !== undefined) {
      this.subscribeToSaveResponse(this.infraService.update(infra));
    } else {
      this.subscribeToSaveResponse(this.infraService.create(infra));
    }
  }

  private createFromForm(): IInfra {
    return {
      ...new Infra(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfra>>): void {
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
