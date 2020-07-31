import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WhistleBlowerTestModule } from '../../../test.module';
import { WitnessComponent } from 'app/entities/witness/witness.component';
import { WitnessService } from 'app/entities/witness/witness.service';
import { Witness } from 'app/shared/model/witness.model';

describe('Component Tests', () => {
  describe('Witness Management Component', () => {
    let comp: WitnessComponent;
    let fixture: ComponentFixture<WitnessComponent>;
    let service: WitnessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhistleBlowerTestModule],
        declarations: [WitnessComponent],
      })
        .overrideTemplate(WitnessComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WitnessComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WitnessService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Witness(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.witnesses && comp.witnesses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
