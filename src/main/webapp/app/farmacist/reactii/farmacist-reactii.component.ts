import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'jhi-farmacist-reactii',
  imports: [CommonModule, FormsModule],
  templateUrl: './farmacist-reactii.component.html',
})
export class FarmacistReactiiComponent {
  pacientId: number | null = null;
  medicamentId: number | null = null;
  descriere = '';
  severitate = 'MEDIE';
  evolutie = '';

  status: 'idle' | 'sending' | 'ok' | 'error' = 'idle';
  mesaj = '';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
  ) {
    const q = this.route.snapshot.queryParamMap.get('pacientId');
    this.pacientId = q ? Number(q) : null;
  }

  trimite(): void {
    if (!this.pacientId || !this.descriere.trim()) {
      this.status = 'error';
      this.mesaj = 'Completează pacientId și descriere.';
      return;
    }

    this.status = 'sending';
    this.mesaj = '';

    const payload: Record<string, unknown> = {
      dataRaportare: new Date().toISOString(),
      pacient: { id: this.pacientId },
      descriere: this.descriere,
      severitate: this.severitate,
      evolutie: this.evolutie || null,
      raportatDe: 'FARMACIST',
    };

    if (this.medicamentId) {
      payload['medicament'] = { id: this.medicamentId };
    }

    this.http.post('/api/reactie-adversas', payload).subscribe({
      next: () => {
        this.status = 'ok';
        this.mesaj = 'Raportarea a fost trimisă.';
        this.descriere = '';
        this.evolutie = '';
        this.medicamentId = null;
      },
      error: () => {
        this.status = 'error';
        this.mesaj = 'Eroare la trimitere.';
      },
    });
  }
}
