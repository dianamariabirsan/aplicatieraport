package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pacient.
 */
@Entity
@Table(name = "pacient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pacient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nume", nullable = false)
    private String nume;

    @NotNull
    @Column(name = "prenume", nullable = false)
    private String prenume;

    @NotNull
    @Column(name = "sex", nullable = false)
    private String sex;

    @NotNull
    @Column(name = "varsta", nullable = false)
    private Integer varsta;

    @Column(name = "greutate")
    private Double greutate;

    @Column(name = "inaltime")
    private Double inaltime;

    @Column(name = "circumferinta_abdominala")
    private Double circumferintaAbdominala;

    @Size(min = 13, max = 13)
    @Column(name = "cnp", length = 13)
    private String cnp;

    @Column(name = "comorbiditati")
    private String comorbiditati;

    @Column(name = "grad_sedentarism")
    private String gradSedentarism;

    @Column(name = "istoric_tratament")
    private String istoricTratament;

    @Column(name = "toleranta")
    private String toleranta;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon")
    private String telefon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pacient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deciziis", "feedbackuris", "medic", "medicament", "pacient" }, allowSetters = true)
    private Set<AlocareTratament> alocaris = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pacient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medicament", "pacient" }, allowSetters = true)
    private Set<ReactieAdversa> reactiiAdverses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pacient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pacient" }, allowSetters = true)
    private Set<Monitorizare> monitorizaris = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Medic medic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "administraris" }, allowSetters = true)
    private Farmacist farmacist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pacient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return this.nume;
    }

    public Pacient nume(String nume) {
        this.setNume(nume);
        return this;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return this.prenume;
    }

    public Pacient prenume(String prenume) {
        this.setPrenume(prenume);
        return this;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getSex() {
        return this.sex;
    }

    public Pacient sex(String sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getVarsta() {
        return this.varsta;
    }

    public Pacient varsta(Integer varsta) {
        this.setVarsta(varsta);
        return this;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }

    public Double getGreutate() {
        return this.greutate;
    }

    public Pacient greutate(Double greutate) {
        this.setGreutate(greutate);
        return this;
    }

    public void setGreutate(Double greutate) {
        this.greutate = greutate;
    }

    public Double getInaltime() {
        return this.inaltime;
    }

    public Pacient inaltime(Double inaltime) {
        this.setInaltime(inaltime);
        return this;
    }

    public void setInaltime(Double inaltime) {
        this.inaltime = inaltime;
    }

    public Double getCircumferintaAbdominala() {
        return this.circumferintaAbdominala;
    }

    public Pacient circumferintaAbdominala(Double circumferintaAbdominala) {
        this.setCircumferintaAbdominala(circumferintaAbdominala);
        return this;
    }

    public void setCircumferintaAbdominala(Double circumferintaAbdominala) {
        this.circumferintaAbdominala = circumferintaAbdominala;
    }

    public String getCnp() {
        return this.cnp;
    }

    public Pacient cnp(String cnp) {
        this.setCnp(cnp);
        return this;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getComorbiditati() {
        return this.comorbiditati;
    }

    public Pacient comorbiditati(String comorbiditati) {
        this.setComorbiditati(comorbiditati);
        return this;
    }

    public void setComorbiditati(String comorbiditati) {
        this.comorbiditati = comorbiditati;
    }

    public String getGradSedentarism() {
        return this.gradSedentarism;
    }

    public Pacient gradSedentarism(String gradSedentarism) {
        this.setGradSedentarism(gradSedentarism);
        return this;
    }

    public void setGradSedentarism(String gradSedentarism) {
        this.gradSedentarism = gradSedentarism;
    }

    public String getIstoricTratament() {
        return this.istoricTratament;
    }

    public Pacient istoricTratament(String istoricTratament) {
        this.setIstoricTratament(istoricTratament);
        return this;
    }

    public void setIstoricTratament(String istoricTratament) {
        this.istoricTratament = istoricTratament;
    }

    public String getToleranta() {
        return this.toleranta;
    }

    public Pacient toleranta(String toleranta) {
        this.setToleranta(toleranta);
        return this;
    }

    public void setToleranta(String toleranta) {
        this.toleranta = toleranta;
    }

    public String getEmail() {
        return this.email;
    }

    public Pacient email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public Pacient telefon(String telefon) {
        this.setTelefon(telefon);
        return this;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Set<AlocareTratament> getAlocaris() {
        return this.alocaris;
    }

    public void setAlocaris(Set<AlocareTratament> alocareTrataments) {
        if (this.alocaris != null) {
            this.alocaris.forEach(i -> i.setPacient(null));
        }
        if (alocareTrataments != null) {
            alocareTrataments.forEach(i -> i.setPacient(this));
        }
        this.alocaris = alocareTrataments;
    }

    public Pacient alocaris(Set<AlocareTratament> alocareTrataments) {
        this.setAlocaris(alocareTrataments);
        return this;
    }

    public Pacient addAlocari(AlocareTratament alocareTratament) {
        this.alocaris.add(alocareTratament);
        alocareTratament.setPacient(this);
        return this;
    }

    public Pacient removeAlocari(AlocareTratament alocareTratament) {
        this.alocaris.remove(alocareTratament);
        alocareTratament.setPacient(null);
        return this;
    }

    public Set<ReactieAdversa> getReactiiAdverses() {
        return this.reactiiAdverses;
    }

    public void setReactiiAdverses(Set<ReactieAdversa> reactieAdversas) {
        if (this.reactiiAdverses != null) {
            this.reactiiAdverses.forEach(i -> i.setPacient(null));
        }
        if (reactieAdversas != null) {
            reactieAdversas.forEach(i -> i.setPacient(this));
        }
        this.reactiiAdverses = reactieAdversas;
    }

    public Pacient reactiiAdverses(Set<ReactieAdversa> reactieAdversas) {
        this.setReactiiAdverses(reactieAdversas);
        return this;
    }

    public Pacient addReactiiAdverse(ReactieAdversa reactieAdversa) {
        this.reactiiAdverses.add(reactieAdversa);
        reactieAdversa.setPacient(this);
        return this;
    }

    public Pacient removeReactiiAdverse(ReactieAdversa reactieAdversa) {
        this.reactiiAdverses.remove(reactieAdversa);
        reactieAdversa.setPacient(null);
        return this;
    }

    public Set<Monitorizare> getMonitorizaris() {
        return this.monitorizaris;
    }

    public void setMonitorizaris(Set<Monitorizare> monitorizares) {
        if (this.monitorizaris != null) {
            this.monitorizaris.forEach(i -> i.setPacient(null));
        }
        if (monitorizares != null) {
            monitorizares.forEach(i -> i.setPacient(this));
        }
        this.monitorizaris = monitorizares;
    }

    public Pacient monitorizaris(Set<Monitorizare> monitorizares) {
        this.setMonitorizaris(monitorizares);
        return this;
    }

    public Pacient addMonitorizari(Monitorizare monitorizare) {
        this.monitorizaris.add(monitorizare);
        monitorizare.setPacient(this);
        return this;
    }

    public Pacient removeMonitorizari(Monitorizare monitorizare) {
        this.monitorizaris.remove(monitorizare);
        monitorizare.setPacient(null);
        return this;
    }

    public Medic getMedic() {
        return this.medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public Pacient medic(Medic medic) {
        this.setMedic(medic);
        return this;
    }

    public Farmacist getFarmacist() {
        return this.farmacist;
    }

    public void setFarmacist(Farmacist farmacist) {
        this.farmacist = farmacist;
    }

    public Pacient farmacist(Farmacist farmacist) {
        this.setFarmacist(farmacist);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pacient)) {
            return false;
        }
        return getId() != null && getId().equals(((Pacient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pacient{" +
            "id=" + getId() +
            ", nume='" + getNume() + "'" +
            ", prenume='" + getPrenume() + "'" +
            ", sex='" + getSex() + "'" +
            ", varsta=" + getVarsta() +
            ", greutate=" + getGreutate() +
            ", inaltime=" + getInaltime() +
            ", circumferintaAbdominala=" + getCircumferintaAbdominala() +
            ", cnp='" + getCnp() + "'" +
            ", comorbiditati='" + getComorbiditati() + "'" +
            ", gradSedentarism='" + getGradSedentarism() + "'" +
            ", istoricTratament='" + getIstoricTratament() + "'" +
            ", toleranta='" + getToleranta() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefon='" + getTelefon() + "'" +
            "}";
    }
}
