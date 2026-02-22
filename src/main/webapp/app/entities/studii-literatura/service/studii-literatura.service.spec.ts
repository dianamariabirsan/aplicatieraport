import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStudiiLiteratura } from '../studii-literatura.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../studii-literatura.test-samples';

import { StudiiLiteraturaService } from './studii-literatura.service';

const requireRestSample: IStudiiLiteratura = {
  ...sampleWithRequiredData,
};

describe('StudiiLiteratura Service', () => {
  let service: StudiiLiteraturaService;
  let httpMock: HttpTestingController;
  let expectedResult: IStudiiLiteratura | IStudiiLiteratura[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StudiiLiteraturaService);
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

    it('should create a StudiiLiteratura', () => {
      const studiiLiteratura = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(studiiLiteratura).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StudiiLiteratura', () => {
      const studiiLiteratura = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(studiiLiteratura).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StudiiLiteratura', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StudiiLiteratura', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StudiiLiteratura', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStudiiLiteraturaToCollectionIfMissing', () => {
      it('should add a StudiiLiteratura to an empty array', () => {
        const studiiLiteratura: IStudiiLiteratura = sampleWithRequiredData;
        expectedResult = service.addStudiiLiteraturaToCollectionIfMissing([], studiiLiteratura);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studiiLiteratura);
      });

      it('should not add a StudiiLiteratura to an array that contains it', () => {
        const studiiLiteratura: IStudiiLiteratura = sampleWithRequiredData;
        const studiiLiteraturaCollection: IStudiiLiteratura[] = [
          {
            ...studiiLiteratura,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStudiiLiteraturaToCollectionIfMissing(studiiLiteraturaCollection, studiiLiteratura);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StudiiLiteratura to an array that doesn't contain it", () => {
        const studiiLiteratura: IStudiiLiteratura = sampleWithRequiredData;
        const studiiLiteraturaCollection: IStudiiLiteratura[] = [sampleWithPartialData];
        expectedResult = service.addStudiiLiteraturaToCollectionIfMissing(studiiLiteraturaCollection, studiiLiteratura);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studiiLiteratura);
      });

      it('should add only unique StudiiLiteratura to an array', () => {
        const studiiLiteraturaArray: IStudiiLiteratura[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const studiiLiteraturaCollection: IStudiiLiteratura[] = [sampleWithRequiredData];
        expectedResult = service.addStudiiLiteraturaToCollectionIfMissing(studiiLiteraturaCollection, ...studiiLiteraturaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const studiiLiteratura: IStudiiLiteratura = sampleWithRequiredData;
        const studiiLiteratura2: IStudiiLiteratura = sampleWithPartialData;
        expectedResult = service.addStudiiLiteraturaToCollectionIfMissing([], studiiLiteratura, studiiLiteratura2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studiiLiteratura);
        expect(expectedResult).toContain(studiiLiteratura2);
      });

      it('should accept null and undefined values', () => {
        const studiiLiteratura: IStudiiLiteratura = sampleWithRequiredData;
        expectedResult = service.addStudiiLiteraturaToCollectionIfMissing([], null, studiiLiteratura, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studiiLiteratura);
      });

      it('should return initial array if no StudiiLiteratura is added', () => {
        const studiiLiteraturaCollection: IStudiiLiteratura[] = [sampleWithRequiredData];
        expectedResult = service.addStudiiLiteraturaToCollectionIfMissing(studiiLiteraturaCollection, undefined, null);
        expect(expectedResult).toEqual(studiiLiteraturaCollection);
      });
    });

    describe('compareStudiiLiteratura', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStudiiLiteratura(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13423 };
        const entity2 = null;

        const compareResult1 = service.compareStudiiLiteratura(entity1, entity2);
        const compareResult2 = service.compareStudiiLiteratura(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13423 };
        const entity2 = { id: 20506 };

        const compareResult1 = service.compareStudiiLiteratura(entity1, entity2);
        const compareResult2 = service.compareStudiiLiteratura(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13423 };
        const entity2 = { id: 13423 };

        const compareResult1 = service.compareStudiiLiteratura(entity1, entity2);
        const compareResult2 = service.compareStudiiLiteratura(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
