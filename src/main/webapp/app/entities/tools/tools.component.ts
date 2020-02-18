import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITools } from 'app/shared/model/tools.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ToolsService } from './tools.service';
import { ToolsDeleteDialogComponent } from './tools-delete-dialog.component';

@Component({
  selector: 'jhi-tools',
  templateUrl: './tools.component.html'
})
export class ToolsComponent implements OnInit, OnDestroy {
  tools: ITools[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected toolsService: ToolsService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.tools = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.toolsService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITools[]>) => this.paginateTools(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.tools = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTools();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITools): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTools(): void {
    this.eventSubscriber = this.eventManager.subscribe('toolsListModification', () => this.reset());
  }

  delete(tools: ITools): void {
    const modalRef = this.modalService.open(ToolsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tools = tools;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTools(data: ITools[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.tools.push(data[i]);
      }
    }
  }
}
