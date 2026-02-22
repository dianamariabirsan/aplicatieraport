package com.example.healthapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.ExternalDrugInfo} entity.
 */
@Schema(description = "=====================\nENTITĂȚI FUNCȚIONALE\n=====================")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalDrugInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private String source;

    private String productSummary;

    private Instant lastUpdated;

    private String sourceUrl;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalDrugInfoDTO)) {
            return false;
        }

        ExternalDrugInfoDTO externalDrugInfoDTO = (ExternalDrugInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, externalDrugInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalDrugInfoDTO{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", productSummary='" + getProductSummary() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            "}";
    }
}
