import { Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AnalyticsService } from './analytics.service';
import { ImportService } from './import.service';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

export interface ChartOption {
  id: string;
  label: string;
  selected: boolean;
}

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

  chartOptions: ChartOption[] = [
    { id: 'bar', label: 'Alocări pe medicament', selected: true },
    { id: 'hist', label: 'Histogramă scor decizie', selected: true },
    { id: 'line', label: 'Alocări pe lună', selected: true },
    { id: 'age', label: 'Pacienți pe grupe de vârstă', selected: true },
    { id: 'sex', label: 'Pacienți pe sex', selected: false },
    { id: 'validated', label: 'Decizii validate vs nevalidate', selected: false },
    { id: 'avgScore', label: 'Scor mediu per medicament', selected: false },
  ];

  isSelected(id: string): boolean {
    return this.chartOptions.find(o => o.id === id)?.selected ?? false;
  }

  @ViewChild('barCanvas', { static: true }) barCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('histCanvas', { static: true }) histCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('lineCanvas', { static: true }) lineCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('ageCanvas', { static: true }) ageCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('sexCanvas', { static: true }) sexCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('validatedCanvas', { static: true }) validatedCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('avgScoreCanvas', { static: true }) avgScoreCanvas!: ElementRef<HTMLCanvasElement>;

  private barChart?: Chart;
  private histChart?: Chart;
  private lineChart?: Chart;
  private ageChart?: Chart;
  private sexChart?: Chart;
  private validatedChart?: Chart;
  private avgScoreChart?: Chart;

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
    this.sexChart?.destroy();
    this.validatedChart?.destroy();
    this.avgScoreChart?.destroy();
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
    if (this.isSelected('bar')) {
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
    } else {
      this.barChart?.destroy();
      this.barChart = undefined;
    }

    // Bar: score histogram
    if (this.isSelected('hist')) {
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
    } else {
      this.histChart?.destroy();
      this.histChart = undefined;
    }

    // Line: allocations by month
    if (this.isSelected('line')) {
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
    } else {
      this.lineChart?.destroy();
      this.lineChart = undefined;
    }

    // Bar: patients by age group
    if (this.isSelected('age')) {
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
    } else {
      this.ageChart?.destroy();
      this.ageChart = undefined;
    }

    // Doughnut: patients by sex
    if (this.isSelected('sex')) {
      this.analytics.pacientBySex(this.dataSource).subscribe({
        next: points => {
          this.sexChart?.destroy();
          this.sexChart = new Chart(this.sexCanvas.nativeElement, {
            type: 'doughnut',
            data: {
              labels: points.map(p => p.label),
              datasets: [{ label: 'Pacienți pe sex', data: points.map(p => p.value), backgroundColor: ['#e83e8c', '#0d6efd', '#adb5bd'] }],
            },
            options: { responsive: true, maintainAspectRatio: false },
          });
        },
      });
    } else {
      this.sexChart?.destroy();
      this.sexChart = undefined;
    }

    // Bar: allocations by validated status
    if (this.isSelected('validated')) {
      this.analytics.alocariByValidated(this.dataSource).subscribe({
        next: points => {
          this.validatedChart?.destroy();
          this.validatedChart = new Chart(this.validatedCanvas.nativeElement, {
            type: 'bar',
            data: {
              labels: points.map(p => p.label),
              datasets: [
                {
                  label: 'Decizii validate',
                  data: points.map(p => p.value),
                  backgroundColor: ['#198754', '#dc3545'],
                },
              ],
            },
            options: { responsive: true, maintainAspectRatio: false },
          });
        },
      });
    } else {
      this.validatedChart?.destroy();
      this.validatedChart = undefined;
    }

    // Bar: average score by medication
    if (this.isSelected('avgScore')) {
      this.analytics.avgScoreByMedicament(this.dataSource).subscribe({
        next: points => {
          this.avgScoreChart?.destroy();
          this.avgScoreChart = new Chart(this.avgScoreCanvas.nativeElement, {
            type: 'bar',
            data: {
              labels: points.map(p => p.label),
              datasets: [{ label: 'Scor mediu decizie', data: points.map(p => p.value), backgroundColor: '#fd7e14' }],
            },
            options: {
              responsive: true,
              maintainAspectRatio: false,
              scales: { y: { min: 0, max: 1 } },
            },
          });
        },
      });
    } else {
      this.avgScoreChart?.destroy();
      this.avgScoreChart = undefined;
    }
  }
}
