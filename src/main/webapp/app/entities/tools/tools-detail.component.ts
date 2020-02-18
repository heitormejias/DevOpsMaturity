import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITools } from 'app/shared/model/tools.model';

@Component({
  selector: 'jhi-tools-detail',
  templateUrl: './tools-detail.component.html'
})
export class ToolsDetailComponent implements OnInit {
  tools: ITools | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tools }) => (this.tools = tools));
  }

  previousState(): void {
    window.history.back();
  }
}
