package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.ExternalDrugInfo} entity. This class is used
 * in {@link com.example.healthapp.web.rest.ExternalDrugInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /external-drug-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalDrugInfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter source;

    private StringFilter productSummary;

    private InstantFilter lastUpdated;

    private StringFilter sourceUrl;

    private LongFilter medicamentId;

    private Boolean distinct;

    public ExternalDrugInfoCriteria() {}

    public ExternalDrugInfoCriteria(ExternalDrugInfoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.source = other.optionalSource().map(StringFilter::copy).orElse(null);
        this.productSummary = other.optionalProductSummary().map(StringFilter::copy).orElse(null);
        this.lastUpdated = other.optionalLastUpdated().map(InstantFilter::copy).orElse(null);
        this.sourceUrl = other.optionalSourceUrl().map(StringFilter::copy).orElse(null);
        this.medicamentId = other.optionalMedicamentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExternalDrugInfoCriteria copy() {
        return new ExternalDrugInfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSource() {
        return source;
    }

    public Optional<StringFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public StringFilter source() {
        if (source == null) {
            setSource(new StringFilter());
        }
        return source;
    }

    public void setSource(StringFilter source) {
        this.source = source;
    }

    public StringFilter getProductSummary() {
        return productSummary;
    }

    public Optional<StringFilter> optionalProductSummary() {
        return Optional.ofNullable(productSummary);
    }

    public StringFilter productSummary() {
        if (productSummary == null) {
            setProductSummary(new StringFilter());
        }
        return productSummary;
    }

    public void setProductSummary(StringFilter productSummary) {
        this.productSummary = productSummary;
    }

    public InstantFilter getLastUpdated() {
        return lastUpdated;
    }

    public Optional<InstantFilter> optionalLastUpdated() {
        return Optional.ofNullable(lastUpdated);
    }

    public InstantFilter lastUpdated() {
        if (lastUpdated == null) {
            setLastUpdated(new InstantFilter());
        }
        return lastUpdated;
    }

    public void setLastUpdated(InstantFilter lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public StringFilter getSourceUrl() {
        return sourceUrl;
    }

    public Optional<StringFilter> optionalSourceUrl() {
        return Optional.ofNullable(sourceUrl);
    }

    public StringFilter sourceUrl() {
        if (sourceUrl == null) {
            setSourceUrl(new StringFilter());
        }
        return sourceUrl;
    }

    public void setSourceUrl(StringFilter sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public LongFilter getMedicamentId() {
        return medicamentId;
    }

    public Optional<LongFilter> optionalMedicamentId() {
        return Optional.ofNullable(medicamentId);
    }

    public LongFilter medicamentId() {
        if (medicamentId == null) {
            setMedicamentId(new LongFilter());
        }
        return medicamentId;
    }

    public void setMedicamentId(LongFilter medicamentId) {
        this.medicamentId = medicamentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExternalDrugInfoCriteria that = (ExternalDrugInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(source, that.source) &&
            Objects.equals(productSummary, that.productSummary) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(sourceUrl, that.sourceUrl) &&
            Objects.equals(medicamentId, that.medicamentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, productSummary, lastUpdated, sourceUrl, medicamentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalDrugInfoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalProductSummary().map(f -> "productSummary=" + f + ", ").orElse("") +
            optionalLastUpdated().map(f -> "lastUpdated=" + f + ", ").orElse("") +
            optionalSourceUrl().map(f -> "sourceUrl=" + f + ", ").orElse("") +
            optionalMedicamentId().map(f -> "medicamentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
