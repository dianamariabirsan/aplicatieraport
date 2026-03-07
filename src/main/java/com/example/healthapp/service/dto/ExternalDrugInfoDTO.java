package com.example.healthapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.ExternalDrugInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalDrugInfoDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Source identifier", requiredMode = Schema.RequiredMode.REQUIRED)
    private String source;

    private String productSummary;

    private Instant lastUpdated;

    private String sourceUrl;

    private MedicamentDTO medicament;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProductSummary() {
        return productSummary;
    }

    public void setProductSummary(String productSummary) {
        this.productSummary = productSummary;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalDrugInfoDTO)) {
            return false;
        }

        ExternalDrugInfoDTO that = (ExternalDrugInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "ExternalDrugInfoDTO{" +
            "id=" +
            getId() +
            ", source='" +
            getSource() +
            "'" +
            ", productSummary='" +
            getProductSummary() +
            "'" +
            ", lastUpdated='" +
            getLastUpdated() +
            "'" +
            ", sourceUrl='" +
            getSourceUrl() +
            "'" +
            ", medicament=" +
            getMedicament() +
            "}"
        );
    }
}
