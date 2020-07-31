import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WhistleBlowerTestModule } from '../../../test.module';
import { SuspectDetailComponent } from 'app/entities/suspect/suspect-detail.component';
import { Suspect } from 'app/shared/model/suspect.model';

describe('Component Tests', () => {
  describe('Suspect Management Detail Component', () => {
    let comp: SuspectDetailComponent;
    let fixture: ComponentFixture<SuspectDetailComponent>;
    const route = ({ data: of({ suspect: new Suspect(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhistleBlowerTestModule],
        declarations: [SuspectDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SuspectDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SuspectDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load suspect on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.suspect).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
