import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WhistleBlowerTestModule } from '../../../test.module';
import { ComplainUpdateComponent } from 'app/entities/complain/complain-update.component';
import { ComplainService } from 'app/entities/complain/complain.service';
import { Complain } from 'app/shared/model/complain.model';

describe('Component Tests', () => {
  describe('Complain Management Update Component', () => {
    let comp: ComplainUpdateComponent;
    let fixture: ComponentFixture<ComplainUpdateComponent>;
    let service: ComplainService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhistleBlowerTestModule],
        declarations: [ComplainUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ComplainUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ComplainUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ComplainService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Complain(123);
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
        const entity = new Complain();
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
