import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IComplain } from 'app/shared/model/complain.model';

type EntityResponseType = HttpResponse<IComplain>;
type EntityArrayResponseType = HttpResponse<IComplain[]>;

@Injectable({ providedIn: 'root' })
export class ComplainService {
  public resourceUrl = SERVER_API_URL + 'api/complains';

  constructor(protected http: HttpClient) {}

  create(complain: IComplain): Observable<EntityResponseType> {
    return this.http.post<IComplain>(this.resourceUrl, complain, { observe: 'response' });
  }

  update(complain: IComplain): Observable<EntityResponseType> {
    return this.http.put<IComplain>(this.resourceUrl, complain, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComplain>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComplain[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  count(req?: any): Observable<number> {
    const options = createRequestOption(req);
    return this.http.get<number>(`${this.resourceUrl}/count`, { params: options });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
