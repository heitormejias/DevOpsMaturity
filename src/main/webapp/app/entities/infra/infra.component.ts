import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfra } from 'app/shared/model/infra.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InfraService } from './infra.service';
import { InfraDeleteDialogComponent } from './infra-delete-dialog.component';

@Component({
  selector: 'jhi-infra',
  templateUrl: './infra.component.html'
})
export class InfraComponent implements OnInit, OnDestroy {
  infras: IInfra[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected infraService: InfraService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.infras = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.infraService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IInfra[]>) => this.paginateInfras(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.infras = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInfras();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInfra): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInfras(): void {
    this.eventSubscriber = this.eventManager.subscribe('infraListModification', () => this.reset());
  }

  delete(infra: IInfra): void {
    const modalRef = this.modalService.open(InfraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.infra = infra;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInfras(data: IInfra[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.infras.push(data[i]);
      }
    }
  }
}
