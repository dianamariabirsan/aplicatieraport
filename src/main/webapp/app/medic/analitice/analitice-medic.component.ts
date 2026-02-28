import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface AnaliticeMedicamentDTO {
  medicamentId: number;
  denumire: string;
  totalReactiiAdverse: number;
  reactiiSevere: number;
  rataReactiiAdverse: number;
  eficientaMedie: number | null;
  totalAlocari: number;
}

@Component({
  standalone: true,
  selector: 'jhi-analitice-medic',
  imports: [CommonModule, HttpClientModule],
  templateUrl: './analitice-medic.component.html',
})
export class AnaliticeMedicComponent implements OnInit {
  analitice: AnaliticeMedicamentDTO[] = [];
  incarcare = false;
  eroare = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.incarcaAnalitice();
  }

  incarcaAnalitice(): void {
    this.incarcare = true;
    this.eroare = '';

    this.http.get<AnaliticeMedicamentDTO[]>('/api/analitice/medicament').subscribe({
      next: res => {
        this.analitice = res ?? [];
        this.incarcare = false;
      },
      error: () => {
        this.eroare = 'Nu s-au putut încărca analiticele.';
        this.incarcare = false;
      },
    });
  }

  getRataFormatata(rata: number): string {
    return (rata * 100).toFixed(1) + '%';
  }

  getRataClasa(rata: number): string {
    if (rata < 0.05) return 'bg-success';
    if (rata < 0.15) return 'bg-warning';
    return 'bg-danger';
  }

  getEficientaFormatata(val: number | null): string {
    return val != null ? val.toFixed(1) + '/10' : 'N/A';
  }
}
