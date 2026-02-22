import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAdministrare } from '../administrare.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../administrare.test-samples';

import { AdministrareService, RestAdministrare } from './administrare.service';

const requireRestSample: RestAdministrare = {
  ...sampleWithRequiredData,
  dataAdministrare: sampleWithRequiredData.dataAdministrare?.toJSON(),
};

describe('Administrare Service', () => {
  let service: AdministrareService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdministrare | IAdministrare[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdministrareService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Administrare', () => {
      const administrare = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(administrare).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Administrare', () => {
      const administrare = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(administrare).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Administrare', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Administrare', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Administrare', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdministrareToCollectionIfMissing', () => {
      it('should add a Administrare to an empty array', () => {
        const administrare: IAdministrare = sampleWithRequiredData;
        expectedResult = service.addAdministrareToCollectionIfMissing([], administrare);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(administrare);
      });

      it('should not add a Administrare to an array that contains it', () => {
        const administrare: IAdministrare = sampleWithRequiredData;
        const administrareCollection: IAdministrare[] = [
          {
            ...administrare,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdministrareToCollectionIfMissing(administrareCollection, administrare);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Administrare to an array that doesn't contain it", () => {
        const administrare: IAdministrare = sampleWithRequiredData;
        const administrareCollection: IAdministrare[] = [sampleWithPartialData];
        expectedResult = service.addAdministrareToCollectionIfMissing(administrareCollection, administrare);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(administrare);
      });

      it('should add only unique Administrare to an array', () => {
        const administrareArray: IAdministrare[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const administrareCollection: IAdministrare[] = [sampleWithRequiredData];
        expectedResult = service.addAdministrareToCollectionIfMissing(administrareCollection, ...administrareArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const administrare: IAdministrare = sampleWithRequiredData;
        const administrare2: IAdministrare = sampleWithPartialData;
        expectedResult = service.addAdministrareToCollectionIfMissing([], administrare, administrare2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(administrare);
        expect(expectedResult).toContain(administrare2);
      });

      it('should accept null and undefined values', () => {
        const administrare: IAdministrare = sampleWithRequiredData;
        expectedResult = service.addAdministrareToCollectionIfMissing([], null, administrare, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(administrare);
      });

      it('should return initial array if no Administrare is added', () => {
        const administrareCollection: IAdministrare[] = [sampleWithRequiredData];
        expectedResult = service.addAdministrareToCollectionIfMissing(administrareCollection, undefined, null);
        expect(expectedResult).toEqual(administrareCollection);
      });
    });

    describe('compareAdministrare', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdministrare(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25740 };
        const entity2 = null;

        const compareResult1 = service.compareAdministrare(entity1, entity2);
        const compareResult2 = service.compareAdministrare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25740 };
        const entity2 = { id: 22046 };

        const compareResult1 = service.compareAdministrare(entity1, entity2);
        const compareResult2 = service.compareAdministrare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25740 };
        const entity2 = { id: 25740 };

        const compareResult1 = service.compareAdministrare(entity1, entity2);
        const compareResult2 = service.compareAdministrare(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
