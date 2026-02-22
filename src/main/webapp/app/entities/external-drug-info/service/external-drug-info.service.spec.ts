import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExternalDrugInfo } from '../external-drug-info.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../external-drug-info.test-samples';

import { ExternalDrugInfoService, RestExternalDrugInfo } from './external-drug-info.service';

const requireRestSample: RestExternalDrugInfo = {
  ...sampleWithRequiredData,
  lastUpdated: sampleWithRequiredData.lastUpdated?.toJSON(),
};

describe('ExternalDrugInfo Service', () => {
  let service: ExternalDrugInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: IExternalDrugInfo | IExternalDrugInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExternalDrugInfoService);
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

    it('should create a ExternalDrugInfo', () => {
      const externalDrugInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(externalDrugInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExternalDrugInfo', () => {
      const externalDrugInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(externalDrugInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExternalDrugInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExternalDrugInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExternalDrugInfo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExternalDrugInfoToCollectionIfMissing', () => {
      it('should add a ExternalDrugInfo to an empty array', () => {
        const externalDrugInfo: IExternalDrugInfo = sampleWithRequiredData;
        expectedResult = service.addExternalDrugInfoToCollectionIfMissing([], externalDrugInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(externalDrugInfo);
      });

      it('should not add a ExternalDrugInfo to an array that contains it', () => {
        const externalDrugInfo: IExternalDrugInfo = sampleWithRequiredData;
        const externalDrugInfoCollection: IExternalDrugInfo[] = [
          {
            ...externalDrugInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExternalDrugInfoToCollectionIfMissing(externalDrugInfoCollection, externalDrugInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExternalDrugInfo to an array that doesn't contain it", () => {
        const externalDrugInfo: IExternalDrugInfo = sampleWithRequiredData;
        const externalDrugInfoCollection: IExternalDrugInfo[] = [sampleWithPartialData];
        expectedResult = service.addExternalDrugInfoToCollectionIfMissing(externalDrugInfoCollection, externalDrugInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(externalDrugInfo);
      });

      it('should add only unique ExternalDrugInfo to an array', () => {
        const externalDrugInfoArray: IExternalDrugInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const externalDrugInfoCollection: IExternalDrugInfo[] = [sampleWithRequiredData];
        expectedResult = service.addExternalDrugInfoToCollectionIfMissing(externalDrugInfoCollection, ...externalDrugInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const externalDrugInfo: IExternalDrugInfo = sampleWithRequiredData;
        const externalDrugInfo2: IExternalDrugInfo = sampleWithPartialData;
        expectedResult = service.addExternalDrugInfoToCollectionIfMissing([], externalDrugInfo, externalDrugInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(externalDrugInfo);
        expect(expectedResult).toContain(externalDrugInfo2);
      });

      it('should accept null and undefined values', () => {
        const externalDrugInfo: IExternalDrugInfo = sampleWithRequiredData;
        expectedResult = service.addExternalDrugInfoToCollectionIfMissing([], null, externalDrugInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(externalDrugInfo);
      });

      it('should return initial array if no ExternalDrugInfo is added', () => {
        const externalDrugInfoCollection: IExternalDrugInfo[] = [sampleWithRequiredData];
        expectedResult = service.addExternalDrugInfoToCollectionIfMissing(externalDrugInfoCollection, undefined, null);
        expect(expectedResult).toEqual(externalDrugInfoCollection);
      });
    });

    describe('compareExternalDrugInfo', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExternalDrugInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7370 };
        const entity2 = null;

        const compareResult1 = service.compareExternalDrugInfo(entity1, entity2);
        const compareResult2 = service.compareExternalDrugInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7370 };
        const entity2 = { id: 14099 };

        const compareResult1 = service.compareExternalDrugInfo(entity1, entity2);
        const compareResult2 = service.compareExternalDrugInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7370 };
        const entity2 = { id: 7370 };

        const compareResult1 = service.compareExternalDrugInfo(entity1, entity2);
        const compareResult2 = service.compareExternalDrugInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
