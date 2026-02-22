import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'jhi-medic-inbox',
  imports: [CommonModule, HttpClientModule],
  templateUrl: './medic-inbox.component.html',
})
export class MedicInboxComponent implements OnInit {
  reactii: any[] = [];
  loading = false;
  eroare = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.incarca();
  }

  incarca(): void {
    this.loading = true;
    this.eroare = '';

    const url = '/api/reactie-adversas'; // verifică în swagger dacă e alt plural
    const params = new HttpParams().set('sort', 'id,desc');

    this.http.get<any[]>(url, { params }).subscribe({
      next: res => {
        this.reactii = res ?? [];
        this.loading = false;
      },
      error: () => {
        this.eroare = 'Nu s-au putut încărca reacțiile.';
        this.reactii = [];
        this.loading = false;
      },
    });
  }
}
