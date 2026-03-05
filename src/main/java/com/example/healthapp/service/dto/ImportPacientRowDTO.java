package com.example.healthapp.service.dto;

import java.time.Instant;

public class ImportPacientRowDTO {

    public String dataSource;
    public String cnp;
    public String nume;
    public String prenume;
    public String sex;
    public Integer varsta;

    public Double greutate;
    public Double inaltime;

    /** Contact fields — used when a new patient record must be created. */
    public String email;
    public String telefon;

    public String medicament;
    public Instant dataDecizie;
    public Double scorDecizie;
    public String tratamentPropus;
    public Boolean decizieValidata;

    public String tratamentConcomitent;
    public Instant dataAdministrare;

    // -----------------------------------------------------------------------
    // DecisionLog fields
    // -----------------------------------------------------------------------
    /** Actor type for the decision log (MEDIC | SISTEM_AI | VALIDATOR_EXTERN). Defaults to SISTEM_AI. */
    public String actorType;
    public String recomandare;
    public String reguliTriggered;
    public String externalChecks;
    public String finalDecision;

    // -----------------------------------------------------------------------
    // ReactieAdversa fields — a row with descriere populated will create an adverse reaction entry.
    // -----------------------------------------------------------------------
    public String reactieDescriere;
    public String reactieSeveritate;
    public String reactieEvolutie;
    public Instant reactieDataRaportare;
    public String reactieRaportatDe;
}
