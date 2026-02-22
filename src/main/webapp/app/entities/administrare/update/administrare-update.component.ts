import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPacient } from 'app/entities/pacient/pacient.model';
import { PacientService } from 'app/entities/pacient/service/pacient.service';
import { IFarmacist } from 'app/entities/farmacist/farmacist.model';
import { FarmacistService } from 'app/entities/farmacist/service/farmacist.service';
import { AdministrareService } from '../service/administrare.service';
import { IAdministrare } from '../administrare.model';
import { AdministrareFormGroup, AdministrareFormService } from './administrare-form.service';

@Component({
  selector: 'jhi-administrare-update',
  templateUrl: './administrare-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AdministrareUpdateComponent implements OnInit {
  isSaving = false;
  administrare: IAdministrare | null = null;

  pacientsSharedCollection: IPacient[] = [];
  farmacistsSharedCollection: IFarmacist[] = [];

  protected administrareService = inject(AdministrareService);
  protected administrareFormService = inject(AdministrareFormService);
  protected pacientService = inject(PacientService);
  protected farmacistService = inject(FarmacistService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AdministrareFormGroup = this.administrareFormService.createAdministrareFormGroup();

  comparePacient = (o1: IPacient | null, o2: IPacient | null): boolean => this.pacientService.comparePacient(o1, o2);

  compareFarmacist = (o1: IFarmacist | null, o2: IFarmacist | null): boolean => this.farmacistService.compareFarmacist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ administrare }) => {
      this.administrare = administrare;
      if (administrare) {
        this.updateForm(administrare);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const administrare = this.administrareFormService.getAdministrare(this.editForm);
    if (administrare.id !== null) {
      this.subscribeToSaveResponse(this.administrareService.update(administrare));
    } else {
      this.subscribeToSaveResponse(this.administrareService.create(administrare));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdministrare>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(administrare: IAdministrare): void {
    this.administrare = administrare;
    this.administrareFormService.resetForm(this.editForm, administrare);

    this.pacientsSharedCollection = this.pacientService.addPacientToCollectionIfMissing<IPacient>(
      this.pacientsSharedCollection,
      administrare.pacient,
    );
    this.farmacistsSharedCollection = this.farmacistService.addFarmacistToCollectionIfMissing<IFarmacist>(
      this.farmacistsSharedCollection,
      administrare.farmacist,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pacientService
      .query()
      .pipe(map((res: HttpResponse<IPacient[]>) => res.body ?? []))
      .pipe(
        map((pacients: IPacient[]) => this.pacientService.addPacientToCollectionIfMissing<IPacient>(pacients, this.administrare?.pacient)),
      )
      .subscribe((pacients: IPacient[]) => (this.pacientsSharedCollection = pacients));

    this.farmacistService
      .query()
      .pipe(map((res: HttpResponse<IFarmacist[]>) => res.body ?? []))
      .pipe(
        map((farmacists: IFarmacist[]) =>
          this.farmacistService.addFarmacistToCollectionIfMissing<IFarmacist>(farmacists, this.administrare?.farmacist),
        ),
      )
      .subscribe((farmacists: IFarmacist[]) => (this.farmacistsSharedCollection = farmacists));
  }
}
