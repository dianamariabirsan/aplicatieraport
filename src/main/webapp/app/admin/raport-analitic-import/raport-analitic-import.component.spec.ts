import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpErrorResponse, provideHttpClient } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { RaportAnaliticImportService } from './service/raport-analitic-import.service';
import RaportAnaliticImportComponent from './raport-analitic-import.component';

describe('RaportAnaliticImportComponent', () => {
  let comp: RaportAnaliticImportComponent;
  let fixture: ComponentFixture<RaportAnaliticImportComponent>;
  let importService: Pick<RaportAnaliticImportService, 'uploadCsv'>;

  beforeEach(waitForAsync(() => {
    importService = {
      uploadCsv: jest.fn(),
    };

    TestBed.configureTestingModule({
      imports: [RaportAnaliticImportComponent],
      providers: [provideHttpClient(), { provide: RaportAnaliticImportService, useValue: importService }],
    })
      .overrideTemplate(RaportAnaliticImportComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaportAnaliticImportComponent);
    comp = fixture.componentInstance;
  });

  it('should reject non-csv files', () => {
    comp.selectedFile = new File(['dummy'], 'raport.txt', { type: 'text/plain' });

    comp.upload();

    expect(importService.uploadCsv).not.toHaveBeenCalled();
    expect(comp.uploadError).toBe('Selectează un fișier CSV valid.');
  });

  it('should upload csv and store result', () => {
    (importService.uploadCsv as jest.Mock).mockReturnValue(
      of({
        createdCount: 2,
        updatedCount: 1,
        failedCount: 0,
        errors: [],
      }),
    );
    comp.selectedFile = new File(['a,b'], 'raport.csv', { type: 'text/csv' });

    comp.upload();

    expect(importService.uploadCsv).toHaveBeenCalledTimes(1);
    expect(comp.result).toEqual(
      expect.objectContaining({
        createdCount: 2,
        updatedCount: 1,
        failedCount: 0,
      }),
    );
    expect(comp.isUploading).toBe(false);
  });

  it('should expose backend error details on upload failure', () => {
    (importService.uploadCsv as jest.Mock).mockReturnValue(
      throwError(() => new HttpErrorResponse({ status: 400, error: { detail: 'CSV invalid la rândul 3' } })),
    );
    comp.selectedFile = new File(['a,b'], 'raport.csv', { type: 'text/csv' });

    comp.upload();

    expect(comp.uploadError).toBe('CSV invalid la rândul 3');
    expect(comp.isUploading).toBe(false);
  });
});
