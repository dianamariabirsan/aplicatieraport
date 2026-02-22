package com.example.healthapp.service.dto;

import java.util.ArrayList;
import java.util.List;

public class EvaluareDecizieRezultatDTO {
    private String recomandare;
    private String motivare;

    private EvaluareMedicamentDTO evaluareA;
    private EvaluareMedicamentDTO evaluareB;

    private StudiiCliniceStatisticaDTO studiiClinice;
    private SigurantaExternaDTO sigurantaExterna;

    private List<String> linkuri = new ArrayList<>();

    public String getRecomandare() { return recomandare; }
    public void setRecomandare(String recomandare) { this.recomandare = recomandare; }

    public String getMotivare() { return motivare; }
    public void setMotivare(String motivare) { this.motivare = motivare; }

    public EvaluareMedicamentDTO getEvaluareA() { return evaluareA; }
    public void setEvaluareA(EvaluareMedicamentDTO evaluareA) { this.evaluareA = evaluareA; }

    public EvaluareMedicamentDTO getEvaluareB() { return evaluareB; }
    public void setEvaluareB(EvaluareMedicamentDTO evaluareB) { this.evaluareB = evaluareB; }

    public StudiiCliniceStatisticaDTO getStudiiClinice() { return studiiClinice; }
    public void setStudiiClinice(StudiiCliniceStatisticaDTO studiiClinice) { this.studiiClinice = studiiClinice; }

    public SigurantaExternaDTO getSigurantaExterna() { return sigurantaExterna; }
    public void setSigurantaExterna(SigurantaExternaDTO sigurantaExterna) { this.sigurantaExterna = sigurantaExterna; }

    public List<String> getLinkuri() { return linkuri; }
    public void setLinkuri(List<String> linkuri) { this.linkuri = linkuri; }
}
