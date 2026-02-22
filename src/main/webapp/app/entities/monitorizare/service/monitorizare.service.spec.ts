import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMonitorizare } from '../monitorizare.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../monitorizare.test-samples';

import { MonitorizareService, RestMonitorizare } from './monitorizare.service';

const requireRestSample: RestMonitorizare = {
  ...sampleWithRequiredData,
  dataInstant: sampleWithRequiredData.dataInstant?.toJSON(),
};

describe('Monitorizare Service', () => {
  let service: MonitorizareService;
  let httpMock: HttpTestingController;
  let expectedResult: IMonitorizare | IMonitorizare[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MonitorizareService);
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

    it('should create a Monitorizare', () => {
      const monitorizare = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(monitorizare).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Monitorizare', () => {
      const monitorizare = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(monitorizare).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Monitorizare', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Monitorizare', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Monitorizare', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMonitorizareToCollectionIfMissing', () => {
      it('should add a Monitorizare to an empty array', () => {
        const monitorizare: IMonitorizare = sampleWithRequiredData;
        expectedResult = service.addMonitorizareToCollectionIfMissing([], monitorizare);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitorizare);
      });

      it('should not add a Monitorizare to an array that contains it', () => {
        const monitorizare: IMonitorizare = sampleWithRequiredData;
        const monitorizareCollection: IMonitorizare[] = [
          {
            ...monitorizare,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMonitorizareToCollectionIfMissing(monitorizareCollection, monitorizare);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Monitorizare to an array that doesn't contain it", () => {
        const monitorizare: IMonitorizare = sampleWithRequiredData;
        const monitorizareCollection: IMonitorizare[] = [sampleWithPartialData];
        expectedResult = service.addMonitorizareToCollectionIfMissing(monitorizareCollection, monitorizare);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitorizare);
      });

      it('should add only unique Monitorizare to an array', () => {
        const monitorizareArray: IMonitorizare[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const monitorizareCollection: IMonitorizare[] = [sampleWithRequiredData];
        expectedResult = service.addMonitorizareToCollectionIfMissing(monitorizareCollection, ...monitorizareArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const monitorizare: IMonitorizare = sampleWithRequiredData;
        const monitorizare2: IMonitorizare = sampleWithPartialData;
        expectedResult = service.addMonitorizareToCollectionIfMissing([], monitorizare, monitorizare2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(monitorizare);
        expect(expectedResult).toContain(monitorizare2);
      });

      it('should accept null and undefined values', () => {
        const monitorizare: IMonitorizare = sampleWithRequiredData;
        expectedResult = service.addMonitorizareToCollectionIfMissing([], null, monitorizare, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(monitorizare);
      });

      it('should return initial array if no Monitorizare is added', () => {
        const monitorizareCollection: IMonitorizare[] = [sampleWithRequiredData];
        expectedResult = service.addMonitorizareToCollectionIfMissing(monitorizareCollection, undefined, null);
        expect(expectedResult).toEqual(monitorizareCollection);
      });
    });

    describe('compareMonitorizare', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMonitorizare(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28896 };
        const entity2 = null;

        const compareResult1 = service.compareMonitorizare(entity1, entity2);
        const compareResult2 = service.compareMonitorizare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28896 };
        const entity2 = { id: 2402 };

        const compareResult1 = service.compareMonitorizare(entity1, entity2);
        const compareResult2 = service.compareMonitorizare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28896 };
        const entity2 = { id: 28896 };

        const compareResult1 = service.compareMonitorizare(entity1, entity2);
        const compareResult2 = service.compareMonitorizare(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
