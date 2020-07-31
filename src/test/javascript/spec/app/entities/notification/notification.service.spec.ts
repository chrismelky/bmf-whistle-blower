import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { NotificationService } from 'app/entities/notification/notification.service';
import { INotification, Notification } from 'app/shared/model/notification.model';

describe('Service Tests', () => {
  describe('Notification Service', () => {
    let injector: TestBed;
    let service: NotificationService;
    let httpMock: HttpTestingController;
    let elemDefault: INotification;
    let expectedResult: INotification | INotification[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(NotificationService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Notification(0, 'AAAAAAA', 'AAAAAAA', false, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should return a list of Notification', () => {
        const returnedFromService = Object.assign(
          {
            subject: 'BBBBBB',
            email: 'BBBBBB',
            isSent: true,
            ignore: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
