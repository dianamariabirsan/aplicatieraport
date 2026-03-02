package com.example.healthapp.service;

import com.example.healthapp.service.dto.ChartPointDTO;
import com.example.healthapp.service.dto.DoubleChartPointDTO;
import com.example.healthapp.service.dto.HistogramBinDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Analytics service that runs aggregate SQL queries for dashboard charts.
 *
 * <p>Data-source filtering uses the {@code DATA_SOURCE=<VALUE>} token stored in
 * {@code pacient.comorbiditati} (temporary approach until a dedicated column is added).
 * Input values are validated against an allowlist to prevent SQL injection.
 */
@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private static final String DATASOURCE_REAL = "REAL";
    private static final String DATASOURCE_SIMULAT = "SIMULAT";

    private final JdbcTemplate jdbc;

    public AnalyticsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // -----------------------------------------------------------------------
    // Allocations by medication (bar chart)
    // -----------------------------------------------------------------------

    /**
     * Count allocations grouped by medication name, optionally filtered by data-source.
     *
     * @param dataSource "REAL", "SIMULAT" or "ALL"
     */
    public List<ChartPointDTO> alocariByMedicament(String dataSource) {
        String where = buildWhere(dataSource);
        String sql =
            "select m.denumire as label, count(*) as value " +
            "from alocare_tratament a " +
            "join medicament m on m.id = a.medicament_id " +
            "join pacient p on p.id = a.pacient_id " +
            where +
            " group by m.denumire order by value desc";
        return jdbc.query(sql, (rs, i) -> new ChartPointDTO(rs.getString("label"), rs.getLong("value")));
    }

    // -----------------------------------------------------------------------
    // Score histogram (bar chart)
    // -----------------------------------------------------------------------

    /**
     * Build a histogram of {@code scor_decizie} values with {@code bins} equal-width bins.
     *
     * @param medicament filter by medication name (null = all)
     * @param bins       number of bins (clamped to [2, 50])
     * @param dataSource "REAL", "SIMULAT" or "ALL"
     */
    public List<HistogramBinDTO> scorHistogram(String medicament, int bins, String dataSource) {
        bins = Math.max(2, Math.min(50, bins));

        String dsWhere = buildWhere(dataSource);
        boolean hasMed = medicament != null && !medicament.isBlank();

        // Build min/max query
        String mmSql =
            "select min(a.scor_decizie) as mn, max(a.scor_decizie) as mx " +
            "from alocare_tratament a " +
            "join medicament m on m.id = a.medicament_id " +
            "join pacient p on p.id = a.pacient_id " +
            dsWhere +
            " and a.scor_decizie is not null" +
            (hasMed ? " and lower(m.denumire) = lower(?)" : "");

        Map<String, Object> mm = hasMed ? jdbc.queryForMap(mmSql, medicament) : jdbc.queryForMap(mmSql);

        Number mnN = (Number) mm.get("mn");
        Number mxN = (Number) mm.get("mx");
        if (mnN == null || mxN == null) return List.of();

        double mn = mnN.doubleValue();
        double mx = mxN.doubleValue();
        if (Double.compare(mn, mx) == 0) return List.of();

        double width = (mx - mn) / bins;
        List<HistogramBinDTO> out = new ArrayList<>();

        for (int i = 0; i < bins; i++) {
            double from = mn + i * width;
            double to = (i == bins - 1) ? mx + 1e-9 : mn + (i + 1) * width;

            String cntSql =
                "select count(*) " +
                "from alocare_tratament a " +
                "join medicament m on m.id = a.medicament_id " +
                "join pacient p on p.id = a.pacient_id " +
                dsWhere +
                " and a.scor_decizie is not null" +
                " and a.scor_decizie >= ? and a.scor_decizie < ?" +
                (hasMed ? " and lower(m.denumire) = lower(?)" : "");

            Long c = hasMed
                ? jdbc.queryForObject(cntSql, Long.class, from, to, medicament)
                : jdbc.queryForObject(cntSql, Long.class, from, to);

            out.add(new HistogramBinDTO(from, to, c != null ? c : 0L));
        }
        return out;
    }

    // -----------------------------------------------------------------------
    // Allocations by month (line/bar chart)
    // -----------------------------------------------------------------------

    /**
     * Count allocations grouped by year-month (format "YYYY-MM"), optionally filtered.
     *
     * @param dataSource "REAL", "SIMULAT" or "ALL"
     */
    public List<ChartPointDTO> alocariByMonth(String dataSource) {
        String where = buildWhere(dataSource);
        // Portable substring: works on H2 (dev) and PostgreSQL (prod).
        // H2 and PostgreSQL both support SUBSTRING(col FROM 1 FOR 7).
        String sql =
            "select substring(cast(a.data_decizie as varchar) from 1 for 7) as label, count(*) as value " +
            "from alocare_tratament a " +
            "join pacient p on p.id = a.pacient_id " +
            where +
            " and a.data_decizie is not null " +
            " group by label order by label";
        return jdbc.query(sql, (rs, i) -> new ChartPointDTO(rs.getString("label"), rs.getLong("value")));
    }

    // -----------------------------------------------------------------------
    // Patients by age group (bar chart)
    // -----------------------------------------------------------------------

    /**
     * Count patients in 10-year age buckets (e.g. "20-29", "30-39", ...).
     *
     * @param dataSource "REAL", "SIMULAT" or "ALL"
     */
    public List<ChartPointDTO> pacientByAgeGroup(String dataSource) {
        String where = buildWhere(dataSource);
        String sql =
            "select " +
            "  concat(cast((p.varsta / 10) * 10 as varchar), '-', cast((p.varsta / 10) * 10 + 9 as varchar)) as label, " +
            "  count(*) as value " +
            "from pacient p " +
            where +
            " and p.varsta is not null " +
            " group by (p.varsta / 10) order by (p.varsta / 10)";
        return jdbc.query(sql, (rs, i) -> new ChartPointDTO(rs.getString("label"), rs.getLong("value")));
    }

    // -----------------------------------------------------------------------
    // Patients by sex (bar chart)
    // -----------------------------------------------------------------------

    /**
     * Count patients grouped by sex, optionally filtered by data-source.
     *
     * @param dataSource "REAL", "SIMULAT" or "ALL"
     */
    public List<ChartPointDTO> pacientBySex(String dataSource) {
        String where = buildWhere(dataSource);
        String sql =
            "select coalesce(nullif(p.sex,''),'NECUNOSCUT') as label, count(*) as value " +
            "from pacient p " +
            where +
            " group by coalesce(nullif(p.sex,''),'NECUNOSCUT') order by value desc";
        return jdbc.query(sql, (rs, i) -> new ChartPointDTO(rs.getString("label"), rs.getLong("value")));
    }

    // -----------------------------------------------------------------------
    // Allocations by validated status (bar chart)
    // -----------------------------------------------------------------------

    /**
     * Count allocations split by whether the decision was validated or not.
     *
     * @param dataSource "REAL", "SIMULAT" or "ALL"
     */
    public List<ChartPointDTO> alocariByValidated(String dataSource) {
        String where = buildWhere(dataSource);
        String sql =
            "select case when a.decizie_validata = true then 'Validat' else 'Nevalidat' end as label, " +
            "count(*) as value " +
            "from alocare_tratament a " +
            "join pacient p on p.id = a.pacient_id " +
            where +
            " group by a.decizie_validata order by a.decizie_validata desc";
        return jdbc.query(sql, (rs, i) -> new ChartPointDTO(rs.getString("label"), rs.getLong("value")));
    }

    // -----------------------------------------------------------------------
    // Average decision score by medication (bar chart)
    // -----------------------------------------------------------------------

    /**
     * Compute average {@code scor_decizie} grouped by medication name.
     *
     * @param dataSource "REAL", "SIMULAT" or "ALL"
     */
    public List<DoubleChartPointDTO> avgScoreByMedicament(String dataSource) {
        String where = buildWhere(dataSource);
        String sql =
            "select m.denumire as label, avg(a.scor_decizie) as value " +
            "from alocare_tratament a " +
            "join medicament m on m.id = a.medicament_id " +
            "join pacient p on p.id = a.pacient_id " +
            where +
            " and a.scor_decizie is not null " +
            " group by m.denumire order by value desc";
        return jdbc.query(sql, (rs, i) -> new DoubleChartPointDTO(rs.getString("label"), rs.getDouble("value")));
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Builds a safe {@code WHERE} clause fragment for data-source filtering.
     * Only "REAL" and "SIMULAT" are accepted; anything else means "ALL".
     */
    private String buildWhere(String dataSource) {
        if (DATASOURCE_REAL.equalsIgnoreCase(dataSource)) {
            return " where coalesce(p.comorbiditati,'') like '%DATA_SOURCE=REAL%' ";
        }
        if (DATASOURCE_SIMULAT.equalsIgnoreCase(dataSource)) {
            return " where coalesce(p.comorbiditati,'') like '%DATA_SOURCE=SIMULAT%' ";
        }
        return " where 1=1 ";
    }
}
