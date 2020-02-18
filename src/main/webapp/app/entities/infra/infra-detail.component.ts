import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfra } from 'app/shared/model/infra.model';

@Component({
  selector: 'jhi-infra-detail',
  templateUrl: './infra-detail.component.html'
})
export class InfraDetailComponent implements OnInit {
  infra: IInfra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infra }) => (this.infra = infra));
  }

  previousState(): void {
    window.history.back();
  }
}
