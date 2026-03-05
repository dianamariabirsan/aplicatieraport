import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'jhi-pacient-reactii',
  imports: [CommonModule, FormsModule],
  templateUrl: './pacient-reactii.component.html',
})
export class PacientReactiiComponent {
  pacientId: number | null = null;
  descriere = '';
  severitate = 'MICA';
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

    const payload = {
      pacientId: this.pacientId,
      descriere: this.descriere,
      severitate: this.severitate,
      evolutie: this.evolutie || null,
      raportatDe: 'PACIENT',
    };

    this.http.post('/api/adverse-reports', payload).subscribe({
      next: () => {
        this.status = 'ok';
        this.mesaj = 'Raportarea a fost trimisă.';
        this.descriere = '';
        this.evolutie = '';
      },
      error: err => {
        this.status = 'error';
        this.mesaj = err?.error?.title ?? 'Eroare la trimitere.';
      },
    });
  }
}
