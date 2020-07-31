import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { WhistleBlowerTestModule } from '../../../test.module';
import { ComplainDetailComponent } from 'app/entities/complain/complain-detail.component';
import { Complain } from 'app/shared/model/complain.model';

describe('Component Tests', () => {
  describe('Complain Management Detail Component', () => {
    let comp: ComplainDetailComponent;
    let fixture: ComponentFixture<ComplainDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ complain: new Complain(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WhistleBlowerTestModule],
        declarations: [ComplainDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ComplainDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ComplainDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load complain on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.complain).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
