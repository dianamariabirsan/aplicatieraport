import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRaportAnalitic } from '../raport-analitic.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../raport-analitic.test-samples';

import { RaportAnaliticService, RestRaportAnalitic } from './raport-analitic.service';

const requireRestSample: RestRaportAnalitic = {
  ...sampleWithRequiredData,
  perioadaStart: sampleWithRequiredData.perioadaStart?.toJSON(),
  perioadaEnd: sampleWithRequiredData.perioadaEnd?.toJSON(),
};

describe('RaportAnalitic Service', () => {
  let service: RaportAnaliticService;
  let httpMock: HttpTestingController;
  let expectedResult: IRaportAnalitic | IRaportAnalitic[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RaportAnaliticService);
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

    it('should create a RaportAnalitic', () => {
      const raportAnalitic = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(raportAnalitic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RaportAnalitic', () => {
      const raportAnalitic = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(raportAnalitic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RaportAnalitic', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RaportAnalitic', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RaportAnalitic', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRaportAnaliticToCollectionIfMissing', () => {
      it('should add a RaportAnalitic to an empty array', () => {
        const raportAnalitic: IRaportAnalitic = sampleWithRequiredData;
        expectedResult = service.addRaportAnaliticToCollectionIfMissing([], raportAnalitic);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raportAnalitic);
      });

      it('should not add a RaportAnalitic to an array that contains it', () => {
        const raportAnalitic: IRaportAnalitic = sampleWithRequiredData;
        const raportAnaliticCollection: IRaportAnalitic[] = [
          {
            ...raportAnalitic,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRaportAnaliticToCollectionIfMissing(raportAnaliticCollection, raportAnalitic);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RaportAnalitic to an array that doesn't contain it", () => {
        const raportAnalitic: IRaportAnalitic = sampleWithRequiredData;
        const raportAnaliticCollection: IRaportAnalitic[] = [sampleWithPartialData];
        expectedResult = service.addRaportAnaliticToCollectionIfMissing(raportAnaliticCollection, raportAnalitic);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raportAnalitic);
      });

      it('should add only unique RaportAnalitic to an array', () => {
        const raportAnaliticArray: IRaportAnalitic[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const raportAnaliticCollection: IRaportAnalitic[] = [sampleWithRequiredData];
        expectedResult = service.addRaportAnaliticToCollectionIfMissing(raportAnaliticCollection, ...raportAnaliticArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const raportAnalitic: IRaportAnalitic = sampleWithRequiredData;
        const raportAnalitic2: IRaportAnalitic = sampleWithPartialData;
        expectedResult = service.addRaportAnaliticToCollectionIfMissing([], raportAnalitic, raportAnalitic2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raportAnalitic);
        expect(expectedResult).toContain(raportAnalitic2);
      });

      it('should accept null and undefined values', () => {
        const raportAnalitic: IRaportAnalitic = sampleWithRequiredData;
        expectedResult = service.addRaportAnaliticToCollectionIfMissing([], null, raportAnalitic, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raportAnalitic);
      });

      it('should return initial array if no RaportAnalitic is added', () => {
        const raportAnaliticCollection: IRaportAnalitic[] = [sampleWithRequiredData];
        expectedResult = service.addRaportAnaliticToCollectionIfMissing(raportAnaliticCollection, undefined, null);
        expect(expectedResult).toEqual(raportAnaliticCollection);
      });
    });

    describe('compareRaportAnalitic', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRaportAnalitic(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6838 };
        const entity2 = null;

        const compareResult1 = service.compareRaportAnalitic(entity1, entity2);
        const compareResult2 = service.compareRaportAnalitic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6838 };
        const entity2 = { id: 16031 };

        const compareResult1 = service.compareRaportAnalitic(entity1, entity2);
        const compareResult2 = service.compareRaportAnalitic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6838 };
        const entity2 = { id: 6838 };

        const compareResult1 = service.compareRaportAnalitic(entity1, entity2);
        const compareResult2 = service.compareRaportAnalitic(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
