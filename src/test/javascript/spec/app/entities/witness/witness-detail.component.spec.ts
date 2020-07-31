import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WhistleBlowerTestModule } from '../../../test.module';
import { WitnessDetailComponent } from 'app/entities/witness/witness-detail.component';
import { Witness } from 'app/shared/model/witness.model';

describe('Component Tests', () => {
  describe('Witness Management Detail Component', () => {
    let comp: WitnessDetailComponent;
    let fixture: ComponentFixture<WitnessDetailComponent>;
    const route = ({ data: of({ witness: new Witness(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhistleBlowerTestModule],
        declarations: [WitnessDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(WitnessDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WitnessDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load witness on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.witness).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
