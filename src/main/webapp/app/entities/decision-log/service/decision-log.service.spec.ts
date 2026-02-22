import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDecisionLog } from '../decision-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../decision-log.test-samples';

import { DecisionLogService, RestDecisionLog } from './decision-log.service';

const requireRestSample: RestDecisionLog = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('DecisionLog Service', () => {
  let service: DecisionLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IDecisionLog | IDecisionLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DecisionLogService);
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

    it('should create a DecisionLog', () => {
      const decisionLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(decisionLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DecisionLog', () => {
      const decisionLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(decisionLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DecisionLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DecisionLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DecisionLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDecisionLogToCollectionIfMissing', () => {
      it('should add a DecisionLog to an empty array', () => {
        const decisionLog: IDecisionLog = sampleWithRequiredData;
        expectedResult = service.addDecisionLogToCollectionIfMissing([], decisionLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decisionLog);
      });

      it('should not add a DecisionLog to an array that contains it', () => {
        const decisionLog: IDecisionLog = sampleWithRequiredData;
        const decisionLogCollection: IDecisionLog[] = [
          {
            ...decisionLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDecisionLogToCollectionIfMissing(decisionLogCollection, decisionLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DecisionLog to an array that doesn't contain it", () => {
        const decisionLog: IDecisionLog = sampleWithRequiredData;
        const decisionLogCollection: IDecisionLog[] = [sampleWithPartialData];
        expectedResult = service.addDecisionLogToCollectionIfMissing(decisionLogCollection, decisionLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decisionLog);
      });

      it('should add only unique DecisionLog to an array', () => {
        const decisionLogArray: IDecisionLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const decisionLogCollection: IDecisionLog[] = [sampleWithRequiredData];
        expectedResult = service.addDecisionLogToCollectionIfMissing(decisionLogCollection, ...decisionLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const decisionLog: IDecisionLog = sampleWithRequiredData;
        const decisionLog2: IDecisionLog = sampleWithPartialData;
        expectedResult = service.addDecisionLogToCollectionIfMissing([], decisionLog, decisionLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decisionLog);
        expect(expectedResult).toContain(decisionLog2);
      });

      it('should accept null and undefined values', () => {
        const decisionLog: IDecisionLog = sampleWithRequiredData;
        expectedResult = service.addDecisionLogToCollectionIfMissing([], null, decisionLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decisionLog);
      });

      it('should return initial array if no DecisionLog is added', () => {
        const decisionLogCollection: IDecisionLog[] = [sampleWithRequiredData];
        expectedResult = service.addDecisionLogToCollectionIfMissing(decisionLogCollection, undefined, null);
        expect(expectedResult).toEqual(decisionLogCollection);
      });
    });

    describe('compareDecisionLog', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDecisionLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11733 };
        const entity2 = null;

        const compareResult1 = service.compareDecisionLog(entity1, entity2);
        const compareResult2 = service.compareDecisionLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11733 };
        const entity2 = { id: 1703 };

        const compareResult1 = service.compareDecisionLog(entity1, entity2);
        const compareResult2 = service.compareDecisionLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11733 };
        const entity2 = { id: 11733 };

        const compareResult1 = service.compareDecisionLog(entity1, entity2);
        const compareResult2 = service.compareDecisionLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
