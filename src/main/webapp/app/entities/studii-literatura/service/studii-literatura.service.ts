import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudiiLiteratura, NewStudiiLiteratura } from '../studii-literatura.model';

export type PartialUpdateStudiiLiteratura = Partial<IStudiiLiteratura> & Pick<IStudiiLiteratura, 'id'>;

export type EntityResponseType = HttpResponse<IStudiiLiteratura>;
export type EntityArrayResponseType = HttpResponse<IStudiiLiteratura[]>;

@Injectable({ providedIn: 'root' })
export class StudiiLiteraturaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/studii-literaturas');

  create(studiiLiteratura: NewStudiiLiteratura): Observable<EntityResponseType> {
    return this.http.post<IStudiiLiteratura>(this.resourceUrl, studiiLiteratura, { observe: 'response' });
  }

  update(studiiLiteratura: IStudiiLiteratura): Observable<EntityResponseType> {
    return this.http.put<IStudiiLiteratura>(
      `${this.resourceUrl}/${this.getStudiiLiteraturaIdentifier(studiiLiteratura)}`,
      studiiLiteratura,
      { observe: 'response' },
    );
  }

  partialUpdate(studiiLiteratura: PartialUpdateStudiiLiteratura): Observable<EntityResponseType> {
    return this.http.patch<IStudiiLiteratura>(
      `${this.resourceUrl}/${this.getStudiiLiteraturaIdentifier(studiiLiteratura)}`,
      studiiLiteratura,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudiiLiteratura>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudiiLiteratura[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStudiiLiteraturaIdentifier(studiiLiteratura: Pick<IStudiiLiteratura, 'id'>): number {
    return studiiLiteratura.id;
  }

  compareStudiiLiteratura(o1: Pick<IStudiiLiteratura, 'id'> | null, o2: Pick<IStudiiLiteratura, 'id'> | null): boolean {
    return o1 && o2 ? this.getStudiiLiteraturaIdentifier(o1) === this.getStudiiLiteraturaIdentifier(o2) : o1 === o2;
  }

  addStudiiLiteraturaToCollectionIfMissing<Type extends Pick<IStudiiLiteratura, 'id'>>(
    studiiLiteraturaCollection: Type[],
    ...studiiLiteraturasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const studiiLiteraturas: Type[] = studiiLiteraturasToCheck.filter(isPresent);
    if (studiiLiteraturas.length > 0) {
      const studiiLiteraturaCollectionIdentifiers = studiiLiteraturaCollection.map(studiiLiteraturaItem =>
        this.getStudiiLiteraturaIdentifier(studiiLiteraturaItem),
      );
      const studiiLiteraturasToAdd = studiiLiteraturas.filter(studiiLiteraturaItem => {
        const studiiLiteraturaIdentifier = this.getStudiiLiteraturaIdentifier(studiiLiteraturaItem);
        if (studiiLiteraturaCollectionIdentifiers.includes(studiiLiteraturaIdentifier)) {
          return false;
        }
        studiiLiteraturaCollectionIdentifiers.push(studiiLiteraturaIdentifier);
        return true;
      });
      return [...studiiLiteraturasToAdd, ...studiiLiteraturaCollection];
    }
    return studiiLiteraturaCollection;
  }
}
