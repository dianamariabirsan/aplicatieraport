package com.example.healthapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Healthapp.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();

    private final ProspecteMedicament prospekteMedicament = new ProspecteMedicament();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public ProspecteMedicament getProspekteMedicament() {
        return prospekteMedicament;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    /**
     * URL-urile oficiale ale prospectelor/SMPC pentru medicamentele suportate.
     * Sursă: Registrul comunitar EMA (ec.europa.eu).
     */
    public static class ProspecteMedicament {

        /** Prospect oficial Mounjaro (tirzepatidă) — EMA, RO */
        private String mounjaro =
            "https://ec.europa.eu/health/documents/community-register/2023/20231211161235/anx_161235_ro.pdf";

        /** Prospect oficial Wegovy (semaglutidă) — EMA, RO */
        private String wegovy =
            "https://ec.europa.eu/health/documents/community-register/2022/20220106154093/anx_154093_ro.pdf";

        public String getMounjaro() {
            return mounjaro;
        }

        public void setMounjaro(String mounjaro) {
            this.mounjaro = mounjaro;
        }

        public String getWegovy() {
            return wegovy;
        }

        public void setWegovy(String wegovy) {
            this.wegovy = wegovy;
        }
    }

    // jhipster-needle-application-properties-property-class
}
