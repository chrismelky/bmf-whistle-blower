import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WhistleBlowerTestModule } from '../../../test.module';
import { SuspectComponent } from 'app/entities/suspect/suspect.component';
import { SuspectService } from 'app/entities/suspect/suspect.service';
import { Suspect } from 'app/shared/model/suspect.model';

describe('Component Tests', () => {
  describe('Suspect Management Component', () => {
    let comp: SuspectComponent;
    let fixture: ComponentFixture<SuspectComponent>;
    let service: SuspectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhistleBlowerTestModule],
        declarations: [SuspectComponent],
      })
        .overrideTemplate(SuspectComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SuspectComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SuspectService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Suspect(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.suspects && comp.suspects[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
