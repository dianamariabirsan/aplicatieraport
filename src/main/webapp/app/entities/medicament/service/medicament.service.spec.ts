import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMedicament } from '../medicament.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../medicament.test-samples';

import { MedicamentService } from './medicament.service';

const requireRestSample: IMedicament = {
  ...sampleWithRequiredData,
};

describe('Medicament Service', () => {
  let service: MedicamentService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicament | IMedicament[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MedicamentService);
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

    it('should create a Medicament', () => {
      const medicament = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicament).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Medicament', () => {
      const medicament = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicament).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Medicament', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Medicament', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Medicament', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicamentToCollectionIfMissing', () => {
      it('should add a Medicament to an empty array', () => {
        const medicament: IMedicament = sampleWithRequiredData;
        expectedResult = service.addMedicamentToCollectionIfMissing([], medicament);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicament);
      });

      it('should not add a Medicament to an array that contains it', () => {
        const medicament: IMedicament = sampleWithRequiredData;
        const medicamentCollection: IMedicament[] = [
          {
            ...medicament,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, medicament);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Medicament to an array that doesn't contain it", () => {
        const medicament: IMedicament = sampleWithRequiredData;
        const medicamentCollection: IMedicament[] = [sampleWithPartialData];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, medicament);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicament);
      });

      it('should add only unique Medicament to an array', () => {
        const medicamentArray: IMedicament[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicamentCollection: IMedicament[] = [sampleWithRequiredData];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, ...medicamentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicament: IMedicament = sampleWithRequiredData;
        const medicament2: IMedicament = sampleWithPartialData;
        expectedResult = service.addMedicamentToCollectionIfMissing([], medicament, medicament2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicament);
        expect(expectedResult).toContain(medicament2);
      });

      it('should accept null and undefined values', () => {
        const medicament: IMedicament = sampleWithRequiredData;
        expectedResult = service.addMedicamentToCollectionIfMissing([], null, medicament, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicament);
      });

      it('should return initial array if no Medicament is added', () => {
        const medicamentCollection: IMedicament[] = [sampleWithRequiredData];
        expectedResult = service.addMedicamentToCollectionIfMissing(medicamentCollection, undefined, null);
        expect(expectedResult).toEqual(medicamentCollection);
      });
    });

    describe('compareMedicament', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicament(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23749 };
        const entity2 = null;

        const compareResult1 = service.compareMedicament(entity1, entity2);
        const compareResult2 = service.compareMedicament(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23749 };
        const entity2 = { id: 11631 };

        const compareResult1 = service.compareMedicament(entity1, entity2);
        const compareResult2 = service.compareMedicament(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23749 };
        const entity2 = { id: 23749 };

        const compareResult1 = service.compareMedicament(entity1, entity2);
        const compareResult2 = service.compareMedicament(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
