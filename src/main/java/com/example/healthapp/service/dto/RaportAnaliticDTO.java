package com.example.healthapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.RaportAnalitic} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RaportAnaliticDTO implements Serializable {

    private Long id;

    private Instant perioadaStart;

    private Instant perioadaEnd;

    private Double eficientaMedie;

    private Double rataReactiiAdverse;

    private String observatii;

    private String concluzii;

    private MedicamentDTO medicament;

    private MedicDTO medic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPerioadaStart() {
        return perioadaStart;
    }

    public void setPerioadaStart(Instant perioadaStart) {
        this.perioadaStart = perioadaStart;
    }

    public Instant getPerioadaEnd() {
        return perioadaEnd;
    }

    public void setPerioadaEnd(Instant perioadaEnd) {
        this.perioadaEnd = perioadaEnd;
    }

    public Double getEficientaMedie() {
        return eficientaMedie;
    }

    public void setEficientaMedie(Double eficientaMedie) {
        this.eficientaMedie = eficientaMedie;
    }

    public Double getRataReactiiAdverse() {
        return rataReactiiAdverse;
    }

    public void setRataReactiiAdverse(Double rataReactiiAdverse) {
        this.rataReactiiAdverse = rataReactiiAdverse;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public String getConcluzii() {
        return concluzii;
    }

    public void setConcluzii(String concluzii) {
        this.concluzii = concluzii;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    public MedicDTO getMedic() {
        return medic;
    }

    public void setMedic(MedicDTO medic) {
        this.medic = medic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RaportAnaliticDTO)) {
            return false;
        }

        RaportAnaliticDTO raportAnaliticDTO = (RaportAnaliticDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, raportAnaliticDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RaportAnaliticDTO{" +
            "id=" + getId() +
            ", perioadaStart='" + getPerioadaStart() + "'" +
            ", perioadaEnd='" + getPerioadaEnd() + "'" +
            ", eficientaMedie=" + getEficientaMedie() +
            ", rataReactiiAdverse=" + getRataReactiiAdverse() +
            ", observatii='" + getObservatii() + "'" +
            ", concluzii='" + getConcluzii() + "'" +
            ", medicament=" + getMedicament() +
            ", medic=" + getMedic() +
            "}";
    }
}
