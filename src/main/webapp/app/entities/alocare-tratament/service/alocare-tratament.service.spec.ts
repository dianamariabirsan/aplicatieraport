import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAlocareTratament } from '../alocare-tratament.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../alocare-tratament.test-samples';

import { AlocareTratamentService, RestAlocareTratament } from './alocare-tratament.service';

const requireRestSample: RestAlocareTratament = {
  ...sampleWithRequiredData,
  dataDecizie: sampleWithRequiredData.dataDecizie?.toJSON(),
};

describe('AlocareTratament Service', () => {
  let service: AlocareTratamentService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlocareTratament | IAlocareTratament[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AlocareTratamentService);
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

    it('should create a AlocareTratament', () => {
      const alocareTratament = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alocareTratament).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AlocareTratament', () => {
      const alocareTratament = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alocareTratament).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AlocareTratament', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AlocareTratament', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AlocareTratament', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlocareTratamentToCollectionIfMissing', () => {
      it('should add a AlocareTratament to an empty array', () => {
        const alocareTratament: IAlocareTratament = sampleWithRequiredData;
        expectedResult = service.addAlocareTratamentToCollectionIfMissing([], alocareTratament);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alocareTratament);
      });

      it('should not add a AlocareTratament to an array that contains it', () => {
        const alocareTratament: IAlocareTratament = sampleWithRequiredData;
        const alocareTratamentCollection: IAlocareTratament[] = [
          {
            ...alocareTratament,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlocareTratamentToCollectionIfMissing(alocareTratamentCollection, alocareTratament);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AlocareTratament to an array that doesn't contain it", () => {
        const alocareTratament: IAlocareTratament = sampleWithRequiredData;
        const alocareTratamentCollection: IAlocareTratament[] = [sampleWithPartialData];
        expectedResult = service.addAlocareTratamentToCollectionIfMissing(alocareTratamentCollection, alocareTratament);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alocareTratament);
      });

      it('should add only unique AlocareTratament to an array', () => {
        const alocareTratamentArray: IAlocareTratament[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alocareTratamentCollection: IAlocareTratament[] = [sampleWithRequiredData];
        expectedResult = service.addAlocareTratamentToCollectionIfMissing(alocareTratamentCollection, ...alocareTratamentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alocareTratament: IAlocareTratament = sampleWithRequiredData;
        const alocareTratament2: IAlocareTratament = sampleWithPartialData;
        expectedResult = service.addAlocareTratamentToCollectionIfMissing([], alocareTratament, alocareTratament2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alocareTratament);
        expect(expectedResult).toContain(alocareTratament2);
      });

      it('should accept null and undefined values', () => {
        const alocareTratament: IAlocareTratament = sampleWithRequiredData;
        expectedResult = service.addAlocareTratamentToCollectionIfMissing([], null, alocareTratament, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alocareTratament);
      });

      it('should return initial array if no AlocareTratament is added', () => {
        const alocareTratamentCollection: IAlocareTratament[] = [sampleWithRequiredData];
        expectedResult = service.addAlocareTratamentToCollectionIfMissing(alocareTratamentCollection, undefined, null);
        expect(expectedResult).toEqual(alocareTratamentCollection);
      });
    });

    describe('compareAlocareTratament', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlocareTratament(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13651 };
        const entity2 = null;

        const compareResult1 = service.compareAlocareTratament(entity1, entity2);
        const compareResult2 = service.compareAlocareTratament(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13651 };
        const entity2 = { id: 1228 };

        const compareResult1 = service.compareAlocareTratament(entity1, entity2);
        const compareResult2 = service.compareAlocareTratament(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13651 };
        const entity2 = { id: 13651 };

        const compareResult1 = service.compareAlocareTratament(entity1, entity2);
        const compareResult2 = service.compareAlocareTratament(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
