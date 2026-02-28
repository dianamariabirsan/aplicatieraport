import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';

interface EvaluareMedicamentDTO {
  denumire: string;
  scor: number | null;
  rezumatSiguranta: string | null;
  sursaUrl: string | null;
  contraindicatii: string | null;
  interactiuni: string | null;
  indicatii: string | null;
  reguliTriggered: string[];
}

interface EvidentaStudiuClinicDTO {
  nctId: string | null;
  titlu: string | null;
  status: string | null;
  faza: string | null;
  inrolare: number | null;
  dataInceput: string | null;
  dataFinal: string | null;
  sponsor: string | null;
  linkUrl: string | null;
}

interface EvaluareDecizieRezultatDTO {
  recomandare: string;
  motivare: string;
  scorA: number | null;
  scorB: number | null;
  evaluareA: EvaluareMedicamentDTO | null;
  evaluareB: EvaluareMedicamentDTO | null;
  evidentaStudiuA: EvidentaStudiuClinicDTO | null;
  evidentaStudiuB: EvidentaStudiuClinicDTO | null;
  alocareTratamentId: number | null;
  decisionLogId: number | null;
  linkuri: string[];
}

@Component({
  standalone: true,
  selector: 'jhi-medic-decizie',
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './medic-decizie.component.html',
})
export class MedicDecizieComponent implements OnInit {
  private static readonly TRATAMENT_IMPLICIT_A = 'Mounjaro';
  private static readonly TRATAMENT_IMPLICIT_B = 'Wegovy';

  pacientId: number | null = null;
  medicId: number | null = null;
  tratamentA = MedicDecizieComponent.TRATAMENT_IMPLICIT_A;
  tratamentB = MedicDecizieComponent.TRATAMENT_IMPLICIT_B;

  rezultat: EvaluareDecizieRezultatDTO | null = null;
  incarcare = false;
  eroare = '';
  mesajSucces = '';

  pacienti: any[] = [];
  medici: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.incarcaListaPacienti();
    this.incarcaListaMedici();
  }

  incarcaListaPacienti(): void {
    this.http.get<any>('/api/pacients', { params: new HttpParams().set('size', '100') }).subscribe({
      next: res => {
        this.pacienti = Array.isArray(res) ? res : (res.content ?? []);
      },
      error: () => {},
    });
  }

  incarcaListaMedici(): void {
    this.http.get<any>('/api/medics', { params: new HttpParams().set('size', '100') }).subscribe({
      next: res => {
        this.medici = Array.isArray(res) ? res : (res.content ?? []);
      },
      error: () => {},
    });
  }

  evalueaza(): void {
    if (!this.pacientId) {
      this.eroare = 'Selectează un pacient.';
      return;
    }
    if (!this.tratamentA.trim() || !this.tratamentB.trim()) {
      this.eroare = 'Completează ambele tratamente A și B.';
      return;
    }

    this.incarcare = true;
    this.eroare = '';
    this.mesajSucces = '';
    this.rezultat = null;

    const cerere = {
      pacientId: this.pacientId,
      medicId: this.medicId ?? null,
      tratamentA: this.tratamentA,
      tratamentB: this.tratamentB,
    };

    this.http.post<EvaluareDecizieRezultatDTO>('/api/decizie/evalueaza', cerere).subscribe({
      next: res => {
        this.rezultat = res;
        this.incarcare = false;
        if (res.alocareTratamentId) {
          this.mesajSucces = `Decizia a fost salvată (AlocareTratament #${res.alocareTratamentId}, Log #${res.decisionLogId}).`;
        }
      },
      error: err => {
        this.eroare = err?.error?.title ?? err?.error?.detail ?? 'Eroare la evaluare.';
        this.incarcare = false;
      },
    });
  }

  aprobaPropunere(): void {
    if (!this.rezultat?.alocareTratamentId) return;
    this.http
      .patch(`/api/alocare-trataments/${this.rezultat.alocareTratamentId}`, {
        id: this.rezultat.alocareTratamentId,
        decizieValidata: true,
      })
      .subscribe({
        next: () => {
          this.mesajSucces = 'Decizia a fost validată de medic!';
        },
        error: () => {
          this.eroare = 'Eroare la validarea deciziei.';
        },
      });
  }

  getScorBara(scor: number | null): number {
    return scor != null ? Math.max(0, Math.min(100, scor)) : 0;
  }

  getScorClasa(scor: number | null): string {
    if (scor == null) return 'bg-secondary';
    if (scor >= 70) return 'bg-success';
    if (scor >= 40) return 'bg-warning';
    return 'bg-danger';
  }

  esteRecomandat(denumire: string): boolean {
    return !!this.rezultat?.recomandare?.includes(denumire);
  }
}
