package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * =====================
 * ENTITĂȚI FUNCȚIONALE
 * =====================
 */
@Entity
@Table(name = "external_drug_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalDrugInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "product_summary")
    private String productSummary;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "source_url")
    private String sourceUrl;

    @JsonIgnoreProperties(value = { "infoExtern", "studiis" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "infoExtern")
    private Medicament medicament;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExternalDrugInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return this.source;
    }

    public ExternalDrugInfo source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProductSummary() {
        return this.productSummary;
    }

    public ExternalDrugInfo productSummary(String productSummary) {
        this.setProductSummary(productSummary);
        return this;
    }

    public void setProductSummary(String productSummary) {
        this.productSummary = productSummary;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public ExternalDrugInfo lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public ExternalDrugInfo sourceUrl(String sourceUrl) {
        this.setSourceUrl(sourceUrl);
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        if (this.medicament != null) {
            this.medicament.setInfoExtern(null);
        }
        if (medicament != null) {
            medicament.setInfoExtern(this);
        }
        this.medicament = medicament;
    }

    public ExternalDrugInfo medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalDrugInfo)) {
            return false;
        }
        return getId() != null && getId().equals(((ExternalDrugInfo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalDrugInfo{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", productSummary='" + getProductSummary() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            "}";
    }
}
