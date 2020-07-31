import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WhistleBlowerTestModule } from '../../../test.module';
import { WitnessUpdateComponent } from 'app/entities/witness/witness-update.component';
import { WitnessService } from 'app/entities/witness/witness.service';
import { Witness } from 'app/shared/model/witness.model';

describe('Component Tests', () => {
  describe('Witness Management Update Component', () => {
    let comp: WitnessUpdateComponent;
    let fixture: ComponentFixture<WitnessUpdateComponent>;
    let service: WitnessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhistleBlowerTestModule],
        declarations: [WitnessUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(WitnessUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WitnessUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WitnessService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Witness(123);
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
        const entity = new Witness();
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
