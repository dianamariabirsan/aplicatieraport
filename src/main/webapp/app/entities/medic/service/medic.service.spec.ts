import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMedic } from '../medic.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../medic.test-samples';

import { MedicService } from './medic.service';

const requireRestSample: IMedic = {
  ...sampleWithRequiredData,
};

describe('Medic Service', () => {
  let service: MedicService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedic | IMedic[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MedicService);
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

    it('should create a Medic', () => {
      const medic = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Medic', () => {
      const medic = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Medic', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Medic', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Medic', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicToCollectionIfMissing', () => {
      it('should add a Medic to an empty array', () => {
        const medic: IMedic = sampleWithRequiredData;
        expectedResult = service.addMedicToCollectionIfMissing([], medic);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medic);
      });

      it('should not add a Medic to an array that contains it', () => {
        const medic: IMedic = sampleWithRequiredData;
        const medicCollection: IMedic[] = [
          {
            ...medic,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicToCollectionIfMissing(medicCollection, medic);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Medic to an array that doesn't contain it", () => {
        const medic: IMedic = sampleWithRequiredData;
        const medicCollection: IMedic[] = [sampleWithPartialData];
        expectedResult = service.addMedicToCollectionIfMissing(medicCollection, medic);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medic);
      });

      it('should add only unique Medic to an array', () => {
        const medicArray: IMedic[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicCollection: IMedic[] = [sampleWithRequiredData];
        expectedResult = service.addMedicToCollectionIfMissing(medicCollection, ...medicArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medic: IMedic = sampleWithRequiredData;
        const medic2: IMedic = sampleWithPartialData;
        expectedResult = service.addMedicToCollectionIfMissing([], medic, medic2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medic);
        expect(expectedResult).toContain(medic2);
      });

      it('should accept null and undefined values', () => {
        const medic: IMedic = sampleWithRequiredData;
        expectedResult = service.addMedicToCollectionIfMissing([], null, medic, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medic);
      });

      it('should return initial array if no Medic is added', () => {
        const medicCollection: IMedic[] = [sampleWithRequiredData];
        expectedResult = service.addMedicToCollectionIfMissing(medicCollection, undefined, null);
        expect(expectedResult).toEqual(medicCollection);
      });
    });

    describe('compareMedic', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedic(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29038 };
        const entity2 = null;

        const compareResult1 = service.compareMedic(entity1, entity2);
        const compareResult2 = service.compareMedic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29038 };
        const entity2 = { id: 10974 };

        const compareResult1 = service.compareMedic(entity1, entity2);
        const compareResult2 = service.compareMedic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29038 };
        const entity2 = { id: 29038 };

        const compareResult1 = service.compareMedic(entity1, entity2);
        const compareResult2 = service.compareMedic(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
