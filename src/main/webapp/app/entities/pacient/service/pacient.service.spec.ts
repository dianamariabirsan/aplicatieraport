import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPacient } from '../pacient.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../pacient.test-samples';

import { PacientService } from './pacient.service';

const requireRestSample: IPacient = {
  ...sampleWithRequiredData,
};

describe('Pacient Service', () => {
  let service: PacientService;
  let httpMock: HttpTestingController;
  let expectedResult: IPacient | IPacient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PacientService);
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

    it('should create a Pacient', () => {
      const pacient = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pacient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pacient', () => {
      const pacient = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pacient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pacient', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pacient', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pacient', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPacientToCollectionIfMissing', () => {
      it('should add a Pacient to an empty array', () => {
        const pacient: IPacient = sampleWithRequiredData;
        expectedResult = service.addPacientToCollectionIfMissing([], pacient);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pacient);
      });

      it('should not add a Pacient to an array that contains it', () => {
        const pacient: IPacient = sampleWithRequiredData;
        const pacientCollection: IPacient[] = [
          {
            ...pacient,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPacientToCollectionIfMissing(pacientCollection, pacient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pacient to an array that doesn't contain it", () => {
        const pacient: IPacient = sampleWithRequiredData;
        const pacientCollection: IPacient[] = [sampleWithPartialData];
        expectedResult = service.addPacientToCollectionIfMissing(pacientCollection, pacient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pacient);
      });

      it('should add only unique Pacient to an array', () => {
        const pacientArray: IPacient[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pacientCollection: IPacient[] = [sampleWithRequiredData];
        expectedResult = service.addPacientToCollectionIfMissing(pacientCollection, ...pacientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pacient: IPacient = sampleWithRequiredData;
        const pacient2: IPacient = sampleWithPartialData;
        expectedResult = service.addPacientToCollectionIfMissing([], pacient, pacient2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pacient);
        expect(expectedResult).toContain(pacient2);
      });

      it('should accept null and undefined values', () => {
        const pacient: IPacient = sampleWithRequiredData;
        expectedResult = service.addPacientToCollectionIfMissing([], null, pacient, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pacient);
      });

      it('should return initial array if no Pacient is added', () => {
        const pacientCollection: IPacient[] = [sampleWithRequiredData];
        expectedResult = service.addPacientToCollectionIfMissing(pacientCollection, undefined, null);
        expect(expectedResult).toEqual(pacientCollection);
      });
    });

    describe('comparePacient', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePacient(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14998 };
        const entity2 = null;

        const compareResult1 = service.comparePacient(entity1, entity2);
        const compareResult2 = service.comparePacient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14998 };
        const entity2 = { id: 10827 };

        const compareResult1 = service.comparePacient(entity1, entity2);
        const compareResult2 = service.comparePacient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14998 };
        const entity2 = { id: 14998 };

        const compareResult1 = service.comparePacient(entity1, entity2);
        const compareResult2 = service.comparePacient(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
