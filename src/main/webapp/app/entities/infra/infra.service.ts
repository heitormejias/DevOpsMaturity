import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInfra } from 'app/shared/model/infra.model';

type EntityResponseType = HttpResponse<IInfra>;
type EntityArrayResponseType = HttpResponse<IInfra[]>;

@Injectable({ providedIn: 'root' })
export class InfraService {
  public resourceUrl = SERVER_API_URL + 'api/infras';

  constructor(protected http: HttpClient) {}

  create(infra: IInfra): Observable<EntityResponseType> {
    return this.http.post<IInfra>(this.resourceUrl, infra, { observe: 'response' });
  }

  update(infra: IInfra): Observable<EntityResponseType> {
    return this.http.put<IInfra>(this.resourceUrl, infra, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInfra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInfra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
