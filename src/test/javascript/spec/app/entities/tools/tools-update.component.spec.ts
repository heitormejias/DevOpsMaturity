import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DevOpsMaturityTestModule } from '../../../test.module';
import { ToolsUpdateComponent } from 'app/entities/tools/tools-update.component';
import { ToolsService } from 'app/entities/tools/tools.service';
import { Tools } from 'app/shared/model/tools.model';

describe('Component Tests', () => {
  describe('Tools Management Update Component', () => {
    let comp: ToolsUpdateComponent;
    let fixture: ComponentFixture<ToolsUpdateComponent>;
    let service: ToolsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DevOpsMaturityTestModule],
        declarations: [ToolsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ToolsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ToolsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToolsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Tools(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Tools();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
