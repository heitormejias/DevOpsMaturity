import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DevOpsMaturityTestModule } from '../../../test.module';
import { InfraDetailComponent } from 'app/entities/infra/infra-detail.component';
import { Infra } from 'app/shared/model/infra.model';

describe('Component Tests', () => {
  describe('Infra Management Detail Component', () => {
    let comp: InfraDetailComponent;
    let fixture: ComponentFixture<InfraDetailComponent>;
    const route = ({ data: of({ infra: new Infra(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DevOpsMaturityTestModule],
        declarations: [InfraDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InfraDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InfraDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load infra on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.infra).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
