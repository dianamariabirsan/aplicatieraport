import { Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnalyticsService } from './analytics.service';
import { ImportService } from './import.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  standalone: true,
  selector: 'jhi-analytics-dashboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './analytics-dashboard.component.html',
})
export class AnalyticsDashboardComponent implements OnInit, OnDestroy {
  private analytics = inject(AnalyticsService);
  private importer = inject(ImportService);

  dataSource: 'ALL' | 'REAL' | 'SIMULAT' = 'ALL';
  medicament: string | null = null;
  bins = 10;

  @ViewChild('barCanvas', { static: true }) barCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('histCanvas', { static: true }) histCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('lineCanvas', { static: true }) lineCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('ageCanvas', { static: true }) ageCanvas!: ElementRef<HTMLCanvasElement>;

  private barChart?: Chart;
  private histChart?: Chart;
  private lineChart?: Chart;
  private ageChart?: Chart;

  importResult: { imported: number; dataSource: string } | null = null;
  importError: string | null = null;
  loading = false;

  ngOnInit(): void {
    this.reloadCharts();
  }

  ngOnDestroy(): void {
    this.barChart?.destroy();
    this.histChart?.destroy();
    this.lineChart?.destroy();
    this.ageChart?.destroy();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.loading = true;
    this.importResult = null;
    this.importError = null;

    this.importer.importPacientiCsv(file, 'SIMULAT').subscribe({
      next: res => {
        this.importResult = res;
        this.loading = false;
        this.reloadCharts();
      },
      error: err => {
        this.importError = (err?.message as string | undefined) ?? 'Eroare la import CSV';
        this.loading = false;
      },
    });
  }

  reloadCharts(): void {
    // Bar: allocations by medication
    this.analytics.alocariByMedicament(this.dataSource).subscribe({
      next: points => {
        this.barChart?.destroy();
        this.barChart = new Chart(this.barCanvas.nativeElement, {
          type: 'bar',
          data: {
            labels: points.map(p => p.label),
            datasets: [{ label: 'Alocări pe medicament', data: points.map(p => p.value), backgroundColor: '#0d6efd' }],
          },
          options: { responsive: true, maintainAspectRatio: false },
        });
      },
    });

    // Bar: score histogram
    this.analytics.scorHistogram(this.medicament, this.bins, this.dataSource).subscribe({
      next: binsData => {
        this.histChart?.destroy();
        this.histChart = new Chart(this.histCanvas.nativeElement, {
          type: 'bar',
          data: {
            labels: binsData.map(b => `${b.from.toFixed(1)}–${b.to.toFixed(1)}`),
            datasets: [{ label: 'Histogramă scor', data: binsData.map(b => b.count), backgroundColor: '#198754' }],
          },
          options: { responsive: true, maintainAspectRatio: false },
        });
      },
    });

    // Line: allocations by month
    this.analytics.alocariByMonth(this.dataSource).subscribe({
      next: points => {
        this.lineChart?.destroy();
        this.lineChart = new Chart(this.lineCanvas.nativeElement, {
          type: 'line',
          data: {
            labels: points.map(p => p.label),
            datasets: [
              {
                label: 'Alocări / lună',
                data: points.map(p => p.value),
                borderColor: '#fd7e14',
                backgroundColor: 'rgba(253,126,20,0.15)',
                fill: true,
                tension: 0.3,
              },
            ],
          },
          options: { responsive: true, maintainAspectRatio: false },
        });
      },
    });

    // Bar: patients by age group
    this.analytics.pacientByAgeGroup(this.dataSource).subscribe({
      next: points => {
        this.ageChart?.destroy();
        this.ageChart = new Chart(this.ageCanvas.nativeElement, {
          type: 'bar',
          data: {
            labels: points.map(p => p.label),
            datasets: [{ label: 'Pacienți pe grupe de vârstă', data: points.map(p => p.value), backgroundColor: '#6f42c1' }],
          },
          options: { responsive: true, maintainAspectRatio: false },
        });
      },
    });
  }
}
