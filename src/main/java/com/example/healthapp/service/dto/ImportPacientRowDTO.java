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

    public String medicament;
    public Instant dataDecizie;
    public Double scorDecizie;
    public String tratamentPropus;
    public Boolean decizieValidata;

    public String tratamentConcomitent;
    public Instant dataAdministrare;
}
