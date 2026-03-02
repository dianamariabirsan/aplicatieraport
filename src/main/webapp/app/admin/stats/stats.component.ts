import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StatsService, StatsSummary } from './stats.service';

type FlatRow = Record<string, string>;

@Component({
  standalone: true,
  selector: 'jhi-stats',
  templateUrl: './stats.component.html',
  imports: [CommonModule, FormsModule],
})
export class StatsComponent implements OnInit {
  dbSummary?: StatsSummary;
  csvSummary?: StatsSummary;
  csvRows: FlatRow[] = [];
  loadingDb = false;
  errorDb?: string;

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.loadDb();
  }

  loadDb(): void {
    this.loadingDb = true;
    this.errorDb = undefined;
    this.statsService.summary().subscribe({
      next: res => {
        this.dbSummary = res;
        this.loadingDb = false;
      },
      error: err => {
        this.errorDb = (err?.message as string | undefined) ?? 'Eroare la încărcarea statisticilor din DB';
        this.loadingDb = false;
      },
    });
  }

  async onCsvSelected(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    const text = await input.files[0].text();
    this.csvRows = this.parseCsv(text);
    this.csvSummary = this.computeFromCsv(this.csvRows);
  }

  maxValue(map?: Record<string, number>): number {
    if (!map) return 1;
    return Math.max(1, ...Object.values(map));
  }

  sortedEntries(map?: Record<string, number>): [string, number][] {
    if (!map) return [];
    return Object.entries(map)
      .map(([k, v]) => [k, Number(v)] as [string, number])
      .sort((a, b) => Number(a[0]) - Number(b[0]));
  }

  private parseCsv(csv: string): FlatRow[] {
    const lines = csv.split(/\r?\n/).filter(l => l.trim().length > 0);
    if (lines.length < 2) return [];
    const sep = lines[0].includes('\t') ? '\t' : ',';
    const headers = lines[0].split(sep).map(h => h.trim());
    return lines.slice(1).map(line => {
      const values = line.split(sep);
      const row: FlatRow = {};
      headers.forEach((h, i) => (row[h] = (values[i] ?? '').trim()));
      return row;
    });
  }

  private computeFromCsv(rows: FlatRow[]): StatsSummary {
    const dist: Record<string, number> = {};
    const histVarsta: Record<string, number> = {};
    const histIMC: Record<string, number> = {};

    for (const r of rows) {
      const trat = (
        (r['alocare_tratament_propus'] as string | undefined) ??
        (r['medicament_denumire'] as string | undefined) ??
        ''
      ).toUpperCase();
      const tKey = trat.includes('MOUNJARO') ? 'MOUNJARO' : trat.includes('WEGOVY') ? 'WEGOVY' : trat.length > 0 ? trat : 'NESETAT';
      dist[tKey] = ((dist[tKey] as number | undefined) ?? 0) + 1;

      const v = Number(r['varsta']);
      if (!Number.isNaN(v) && v > 0) {
        const vKey = String(v);
        histVarsta[vKey] = ((histVarsta[vKey] as number | undefined) ?? 0) + 1;
      }

      const kg = Number(r['pacient_greutate']);
      let h = Number(r['pacient_inaltime']);
      if (!Number.isNaN(h) && h > 3) h = h / 100.0;
      if (!Number.isNaN(kg) && !Number.isNaN(h) && h > 0) {
        const imc = kg / (h * h);
        const bucket = String(Math.round(imc));
        histIMC[bucket] = ((histIMC[bucket] as number | undefined) ?? 0) + 1;
      }
    }
    return { totalPacienti: rows.length, distributieTratament: dist, histVarsta, histIMC };
  }
}
