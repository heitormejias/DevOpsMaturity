import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DevOpsMaturityTestModule } from '../../../test.module';
import { InfraUpdateComponent } from 'app/entities/infra/infra-update.component';
import { InfraService } from 'app/entities/infra/infra.service';
import { Infra } from 'app/shared/model/infra.model';

describe('Component Tests', () => {
  describe('Infra Management Update Component', () => {
    let comp: InfraUpdateComponent;
    let fixture: ComponentFixture<InfraUpdateComponent>;
    let service: InfraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DevOpsMaturityTestModule],
        declarations: [InfraUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InfraUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InfraUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InfraService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Infra(123);
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
        const entity = new Infra();
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
