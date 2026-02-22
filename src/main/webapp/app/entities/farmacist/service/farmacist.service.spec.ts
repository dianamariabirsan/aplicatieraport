import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFarmacist } from '../farmacist.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../farmacist.test-samples';

import { FarmacistService } from './farmacist.service';

const requireRestSample: IFarmacist = {
  ...sampleWithRequiredData,
};

describe('Farmacist Service', () => {
  let service: FarmacistService;
  let httpMock: HttpTestingController;
  let expectedResult: IFarmacist | IFarmacist[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FarmacistService);
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

    it('should create a Farmacist', () => {
      const farmacist = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(farmacist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Farmacist', () => {
      const farmacist = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(farmacist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Farmacist', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Farmacist', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Farmacist', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFarmacistToCollectionIfMissing', () => {
      it('should add a Farmacist to an empty array', () => {
        const farmacist: IFarmacist = sampleWithRequiredData;
        expectedResult = service.addFarmacistToCollectionIfMissing([], farmacist);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(farmacist);
      });

      it('should not add a Farmacist to an array that contains it', () => {
        const farmacist: IFarmacist = sampleWithRequiredData;
        const farmacistCollection: IFarmacist[] = [
          {
            ...farmacist,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFarmacistToCollectionIfMissing(farmacistCollection, farmacist);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Farmacist to an array that doesn't contain it", () => {
        const farmacist: IFarmacist = sampleWithRequiredData;
        const farmacistCollection: IFarmacist[] = [sampleWithPartialData];
        expectedResult = service.addFarmacistToCollectionIfMissing(farmacistCollection, farmacist);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(farmacist);
      });

      it('should add only unique Farmacist to an array', () => {
        const farmacistArray: IFarmacist[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const farmacistCollection: IFarmacist[] = [sampleWithRequiredData];
        expectedResult = service.addFarmacistToCollectionIfMissing(farmacistCollection, ...farmacistArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const farmacist: IFarmacist = sampleWithRequiredData;
        const farmacist2: IFarmacist = sampleWithPartialData;
        expectedResult = service.addFarmacistToCollectionIfMissing([], farmacist, farmacist2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(farmacist);
        expect(expectedResult).toContain(farmacist2);
      });

      it('should accept null and undefined values', () => {
        const farmacist: IFarmacist = sampleWithRequiredData;
        expectedResult = service.addFarmacistToCollectionIfMissing([], null, farmacist, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(farmacist);
      });

      it('should return initial array if no Farmacist is added', () => {
        const farmacistCollection: IFarmacist[] = [sampleWithRequiredData];
        expectedResult = service.addFarmacistToCollectionIfMissing(farmacistCollection, undefined, null);
        expect(expectedResult).toEqual(farmacistCollection);
      });
    });

    describe('compareFarmacist', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFarmacist(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13357 };
        const entity2 = null;

        const compareResult1 = service.compareFarmacist(entity1, entity2);
        const compareResult2 = service.compareFarmacist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13357 };
        const entity2 = { id: 23159 };

        const compareResult1 = service.compareFarmacist(entity1, entity2);
        const compareResult2 = service.compareFarmacist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13357 };
        const entity2 = { id: 13357 };

        const compareResult1 = service.compareFarmacist(entity1, entity2);
        const compareResult2 = service.compareFarmacist(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
