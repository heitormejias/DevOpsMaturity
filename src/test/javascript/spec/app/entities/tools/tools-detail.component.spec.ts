import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DevOpsMaturityTestModule } from '../../../test.module';
import { ToolsDetailComponent } from 'app/entities/tools/tools-detail.component';
import { Tools } from 'app/shared/model/tools.model';

describe('Component Tests', () => {
  describe('Tools Management Detail Component', () => {
    let comp: ToolsDetailComponent;
    let fixture: ComponentFixture<ToolsDetailComponent>;
    const route = ({ data: of({ tools: new Tools(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DevOpsMaturityTestModule],
        declarations: [ToolsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ToolsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ToolsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tools on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tools).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
