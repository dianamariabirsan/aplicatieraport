import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReactieAdversa } from '../reactie-adversa.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reactie-adversa.test-samples';

import { ReactieAdversaService, RestReactieAdversa } from './reactie-adversa.service';

const requireRestSample: RestReactieAdversa = {
  ...sampleWithRequiredData,
  dataRaportare: sampleWithRequiredData.dataRaportare?.toJSON(),
};

describe('ReactieAdversa Service', () => {
  let service: ReactieAdversaService;
  let httpMock: HttpTestingController;
  let expectedResult: IReactieAdversa | IReactieAdversa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReactieAdversaService);
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

    it('should create a ReactieAdversa', () => {
      const reactieAdversa = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reactieAdversa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReactieAdversa', () => {
      const reactieAdversa = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reactieAdversa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReactieAdversa', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReactieAdversa', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReactieAdversa', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReactieAdversaToCollectionIfMissing', () => {
      it('should add a ReactieAdversa to an empty array', () => {
        const reactieAdversa: IReactieAdversa = sampleWithRequiredData;
        expectedResult = service.addReactieAdversaToCollectionIfMissing([], reactieAdversa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reactieAdversa);
      });

      it('should not add a ReactieAdversa to an array that contains it', () => {
        const reactieAdversa: IReactieAdversa = sampleWithRequiredData;
        const reactieAdversaCollection: IReactieAdversa[] = [
          {
            ...reactieAdversa,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReactieAdversaToCollectionIfMissing(reactieAdversaCollection, reactieAdversa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReactieAdversa to an array that doesn't contain it", () => {
        const reactieAdversa: IReactieAdversa = sampleWithRequiredData;
        const reactieAdversaCollection: IReactieAdversa[] = [sampleWithPartialData];
        expectedResult = service.addReactieAdversaToCollectionIfMissing(reactieAdversaCollection, reactieAdversa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reactieAdversa);
      });

      it('should add only unique ReactieAdversa to an array', () => {
        const reactieAdversaArray: IReactieAdversa[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reactieAdversaCollection: IReactieAdversa[] = [sampleWithRequiredData];
        expectedResult = service.addReactieAdversaToCollectionIfMissing(reactieAdversaCollection, ...reactieAdversaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reactieAdversa: IReactieAdversa = sampleWithRequiredData;
        const reactieAdversa2: IReactieAdversa = sampleWithPartialData;
        expectedResult = service.addReactieAdversaToCollectionIfMissing([], reactieAdversa, reactieAdversa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reactieAdversa);
        expect(expectedResult).toContain(reactieAdversa2);
      });

      it('should accept null and undefined values', () => {
        const reactieAdversa: IReactieAdversa = sampleWithRequiredData;
        expectedResult = service.addReactieAdversaToCollectionIfMissing([], null, reactieAdversa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reactieAdversa);
      });

      it('should return initial array if no ReactieAdversa is added', () => {
        const reactieAdversaCollection: IReactieAdversa[] = [sampleWithRequiredData];
        expectedResult = service.addReactieAdversaToCollectionIfMissing(reactieAdversaCollection, undefined, null);
        expect(expectedResult).toEqual(reactieAdversaCollection);
      });
    });

    describe('compareReactieAdversa', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReactieAdversa(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1613 };
        const entity2 = null;

        const compareResult1 = service.compareReactieAdversa(entity1, entity2);
        const compareResult2 = service.compareReactieAdversa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1613 };
        const entity2 = { id: 23843 };

        const compareResult1 = service.compareReactieAdversa(entity1, entity2);
        const compareResult2 = service.compareReactieAdversa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1613 };
        const entity2 = { id: 1613 };

        const compareResult1 = service.compareReactieAdversa(entity1, entity2);
        const compareResult2 = service.compareReactieAdversa(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
