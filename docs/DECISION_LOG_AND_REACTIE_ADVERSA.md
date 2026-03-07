# DecisionLog & Reactie Adversa — Logic and Operations

This document explains the business logic and technical operations for the two closely related
features: **DecisionLog** (the audit trail of every treatment-allocation decision) and
**Reactie Adversa** (adverse-reaction reporting).

---

## Table of Contents

1. [Overview](#1-overview)
2. [ReactieAdversa — Adverse Reaction Reporting](#2-reactieadversa--adverse-reaction-reporting)
   - [Database Schema](#21-database-schema)
   - [CRUD Operations](#22-crud-operations)
   - [Shortcut Report Endpoint](#23-shortcut-report-endpoint)
   - [Validation Rules](#24-validation-rules)
   - [Frontend](#25-frontend)
3. [DecisionLog — Decision Audit Trail](#3-decisionlog--decision-audit-trail)
   - [Database Schema](#31-database-schema)
   - [Read-Only Design](#32-read-only-design)
   - [How a DecisionLog Entry Is Created](#33-how-a-decisionlog-entry-is-created)
   - [Frontend](#34-frontend)
4. [The Decision Engine — Core Logic](#4-the-decision-engine--core-logic)
   - [Priority System](#41-priority-system)
   - [Scoring Formula](#42-scoring-formula)
   - [Rule Evaluation (Step by Step)](#43-rule-evaluation-step-by-step)
   - [Machine-Learning Integration](#44-machine-learning-integration)
   - [Recommendation Text](#45-recommendation-text)
5. [How ReactieAdversa Feeds into DecisionLog](#5-how-reactieadversa-feeds-into-decisionlog)
6. [Entity Relationship Summary](#6-entity-relationship-summary)
7. [API Quick Reference](#7-api-quick-reference)

---

## 1. Overview

```
Doctor proposes treatment
        │
        ▼
AlocareTratament saved to DB
        │
        ▼
DecisionEngineService.evaluate()   ◄── reads ReactieAdversa history (P2 rule)
        │
        ▼
DecisionResult  {score, warnings, ruleCodes, recommendation}
        │
        ▼
DecisionEngineService.persistAudit()
        │
        ▼
DecisionLog row written to DB  (immutable audit record)
```

The two features are related as follows:

* **ReactieAdversa** is a mutable record that any authorised user can create, read, update, or
  delete. It represents an adverse reaction that a patient experienced with a specific drug.
* **DecisionLog** is a fully **read-only** audit record that the system generates automatically
  every time a treatment allocation is evaluated. It captures what the engine decided, why, and
  with what score.

---

## 2. ReactieAdversa — Adverse Reaction Reporting

### 2.1 Database Schema

Table: `reactie_adversa`

| Column             | Type           | Nullable | Notes                         |
|--------------------|----------------|----------|-------------------------------|
| `id`               | BIGINT (PK)    | ✗        | Auto-generated sequence       |
| `data_raportare`   | TIMESTAMP      | ✗        | When the reaction was reported |
| `severitate`       | VARCHAR(255)   | ✓        | `MICA`, `MEDIE`, or `SEVERA`  |
| `descriere`        | VARCHAR(255)   | ✗        | Free-text description         |
| `evolutie`         | VARCHAR(255)   | ✓        | How the reaction progressed   |
| `raportat_de`      | VARCHAR(255)   | ✓        | Name / role of the reporter   |
| `medicament_id`    | BIGINT (FK)    | ✓        | Drug that caused the reaction |
| `pacient_id`       | BIGINT (FK)    | ✓        | Affected patient              |

Severity enum (`SeveritateReactie`):

| Value    | Meaning  |
|----------|----------|
| `MICA`   | Mild     |
| `MEDIE`  | Moderate |
| `SEVERA` | Severe   |

### 2.2 CRUD Operations

All operations go through `ReactieAdversaResource` (base path `/api/reactie-adversas`) backed by
`ReactieAdversaService`.

#### Create — `POST /api/reactie-adversas`

```json
{
  "dataRaportare": "2024-06-01T10:00:00Z",
  "severitate": "SEVERA",
  "descriere": "Anafilaxie la penicilina",
  "evolutie": "Ameliorat dupa adrenalina",
  "raportatDe": "Dr. Ionescu",
  "medicament": { "id": 42 },
  "pacient":    { "id": 7  }
}
```

Service validation (`ReactieAdversaService.save`):

1. Both `pacient.id` and `medicament.id` must be present — otherwise an
   `IllegalArgumentException` is thrown.
2. The service calls `resolveRelationships` to load the full `Pacient` and `Medicament` entities
   from the database before persisting.
3. Returns the saved record as a `ReactieAdversaDTO` (HTTP 201).

#### Read — `GET /api/reactie-adversas` / `GET /api/reactie-adversas/{id}`

* List endpoint supports pagination and criteria-based filtering
  (`ReactieAdversaQueryService`). You can filter by `pacientId`, `medicamentId`,
  `severitate`, or `dataRaportare` range.
* `GET /api/reactie-adversas/count` returns the number of records matching a filter.

#### Update — `PUT /api/reactie-adversas/{id}` (full) / `PATCH /api/reactie-adversas/{id}` (partial)

The same validation as Create applies. Partial update only modifies fields explicitly
included in the request body.

#### Delete — `DELETE /api/reactie-adversas/{id}`

Returns HTTP 204. There is no cascade deletion to `DecisionLog` because the engine only
*reads* adverse reactions; the log entries are independent records.

### 2.3 Shortcut Report Endpoint

`RaportReactieAdversaResource` exposes a convenience endpoint at
`POST /api/adverse-reports` that lets a caller report an adverse reaction by supplying
only the **patient ID** plus reaction details, without needing to specify the drug
explicitly.

```java
// RaportReactieAdversaService.raporteaza()
Pacient p = pacientRepository.findById(cerere.getPacientId());

// Prefer the most recent validated treatment allocation; fall back to latest
AlocareTratament alocare = alocareTratamentRepository
    .findTopByPacientIdAndDecizieValidataTrueOrderByDataDecizieDesc(p.getId())
    .orElse(findTopByPacientIdOrderByDataDecizieDesc(p.getId()));

Medicament m = alocare.getMedicament();   // drug extracted from allocation

ReactieAdversa ra = new ReactieAdversa()
    .pacient(p).medicament(m)
    .dataRaportare(Instant.now())
    .descriere(cerere.getDescriere())
    .severitate(cerere.getSeveritate())
    .evolutie(cerere.getEvolutie())
    .raportatDe(cerere.getRaportatDe());

reactieAdversaRepository.save(ra);
```

Response (`RaportReactieAdversaRezultatDTO`):

```json
{
  "reactieAdversaId": 123,
  "pacientId": 7,
  "medicamentId": 42,
  "mesaj": "Raportare inregistrata cu succes."
}
```

### 2.4 Validation Rules

| Field          | Rule                                          |
|----------------|-----------------------------------------------|
| `dataRaportare`| `@NotNull`                                    |
| `descriere`    | `@NotNull`                                    |
| `pacient`      | Must be provided with a valid `id`            |
| `medicament`   | Must be provided with a valid `id`            |
| `severitate`   | Optional; one of `MICA`, `MEDIE`, `SEVERA`    |

### 2.5 Frontend

Angular components located in
`src/main/webapp/app/entities/reactie-adversa/`:

| Component file                              | Role                           |
|---------------------------------------------|--------------------------------|
| `list/reactie-adversa.component.ts`         | Paginated list with filters    |
| `detail/reactie-adversa-detail.component.ts`| Read-only detail view          |
| `update/reactie-adversa-update.component.ts`| Create / edit form             |
| `delete/reactie-adversa-delete-dialog.component.ts` | Confirmation dialog  |
| `service/reactie-adversa.service.ts`        | HTTP client (CRUD + query)     |

TypeScript model (`IReactieAdversa`):

```typescript
interface IReactieAdversa {
  id: number;
  dataRaportare?: dayjs.Dayjs;
  severitate?: 'MICA' | 'MEDIE' | 'SEVERA';
  descriere?: string;
  evolutie?: string;
  raportatDe?: string;
  medicament?: Pick<IMedicament, 'id' | 'denumire'>;
  pacient?: Pick<IPacient, 'id' | 'nume' | 'prenume'>;
}
```

---

## 3. DecisionLog — Decision Audit Trail

### 3.1 Database Schema

Table: `decision_log`

| Column             | Type           | Nullable | Notes                                                 |
|--------------------|----------------|----------|-------------------------------------------------------|
| `id`               | BIGINT (PK)    | ✗        | Auto-generated sequence                               |
| `timestamp`        | TIMESTAMP      | ✗        | When the log entry was created                        |
| `actor_type`       | VARCHAR(255)   | ✗        | `MEDIC`, `SISTEM_AI`, or `VALIDATOR_EXTERN`           |
| `recomandare`      | TEXT           | ✓        | Human-readable recommendation produced by the engine  |
| `model_score`      | DOUBLE         | ✓        | Final decision score (0–100)                          |
| `reguli_triggered` | TEXT           | ✓        | Pipe-separated list of rule codes that fired          |
| `external_checks`  | TEXT           | ✓        | Audit summary: patient context, drug, comorbidities   |
| `final_decision`   | TEXT           | ✓        | Same as `recomandare` when automated                  |
| `decision_source`  | VARCHAR(255)   | ✓        | Who made the final decision (same enum as actor_type) |
| `override_reason`  | TEXT           | ✓        | Set when a safety rule is triggered                   |
| `alocare_id`       | BIGINT (FK)    | ✓        | The treatment allocation being evaluated              |

### 3.2 Read-Only Design

`DecisionLog` records are created **exclusively** by the `DecisionEngineService`.
The REST controller (`DecisionLogResource`) deliberately returns HTTP 405 for any
mutating request:

| HTTP Method | Endpoint                          | Allowed? |
|-------------|-----------------------------------|----------|
| `POST`      | `/api/decision-logs`              | ❌ 405   |
| `PUT`       | `/api/decision-logs/{id}`         | ❌ 405   |
| `PATCH`     | `/api/decision-logs/{id}`         | ❌ 405   |
| `DELETE`    | `/api/decision-logs/{id}`         | ❌ 405   |
| `GET`       | `/api/decision-logs`              | ✅ 200   |
| `GET`       | `/api/decision-logs/count`        | ✅ 200   |
| `GET`       | `/api/decision-logs/{id}`         | ✅ 200   |
| `GET`       | `/api/decision-logs/by-alocare/{alocareId}` | ✅ 200 |

This guarantees that the audit trail can never be altered after the fact.

### 3.3 How a DecisionLog Entry Is Created

`DecisionEngineService` exposes three public methods:

```
evaluate(alocare)
  └─ Runs all rules, scores the treatment, returns DecisionResult.
     Does NOT touch the database.

persistAudit(alocare, result, actorType)
  └─ Writes the DecisionLog row. Must be called AFTER alocare is saved
     (needs a valid FK id).

evaluateAndLog(alocare)
  └─ Convenience wrapper: calls evaluate() then persistAudit() together.
     Used for backward compatibility.
```

`AlocareTratamentService` calls `evaluateAndLog` (or `evaluate` +
`persistAudit` separately) whenever a treatment allocation is saved.

### 3.4 Frontend

Angular components located in
`src/main/webapp/app/entities/decision-log/`:

| Component file                              | Role                                    |
|---------------------------------------------|-----------------------------------------|
| `list/decision-log.component.ts`            | Paginated list with filtering           |
| `detail/decision-log-detail.component.ts`   | Read-only detail view                   |
| `service/decision-log.service.ts`           | HTTP client (read-only + query helpers) |

Additional helper in the service:

```typescript
queryByAlocareId(alocareId: number): Observable<HttpResponse<IDecisionLog[]>>
// Maps to GET /api/decision-logs/by-alocare/{alocareId}
```

---

## 4. The Decision Engine — Core Logic

`DecisionEngineService` (`src/main/java/…/service/DecisionEngineService.java`) is the
central component. It evaluates a proposed treatment and produces a numeric score (0–100)
and a human-readable recommendation.

### 4.1 Priority System

Six priority levels are defined as the enum `RulePriority`. Lower ordinal = higher clinical
importance. When multiple rules fire, the **one with the lowest ordinal wins**.

| Enum constant             | Priority | Description                            |
|---------------------------|----------|----------------------------------------|
| `P1_CONTRAINDICATION`     | 1 (top)  | Absolute contraindication in SmPC      |
| `P2_SEVERE_AE`            | 2        | Severe adverse reaction in patient history |
| `P3_MAJOR_INTERACTION`    | 3        | Major drug–drug interaction in SmPC    |
| `P4_INSUFFICIENT_EFFICACY`| 4        | Poor efficacy noted in patient history |
| `P5_AI_SCORE`             | 5        | ML model prediction (no safety rule triggered) |
| `P6_DEFAULT`              | 6        | No rule fired; clinical-factors only   |

### 4.2 Scoring Formula

```
baseScore = 100
  − 10 (if patient age > 65)   [PENALTY_ELDERLY]
  − 5  (if DZ2 comorbidity)    [PENALTY_DIABETES]
  clamped to [0, 100]

finalScore = applyPriorityScore(winningPriority, baseScore):
  P1 → 0.0                                  (hard stop)
  P2 → min(20.0, baseScore)
  P3 → max(0.0, baseScore − 30 × interactionCount)
  P4 → max(0.0, baseScore − 15.0)
  P5/P6 → baseScore (optionally adjusted by ML model)

If ML model available AND no P1–P4 rule fired:
  model prediction == 1 → finalScore = max(finalScore, 70.0)
  model prediction == 0 → finalScore = min(finalScore, 40.0)
```

### 4.3 Rule Evaluation (Step by Step)

The method `evaluate(AlocareTratament alocare)` proceeds as follows:

**Step 1 — Load context data**

```java
List<Administrare> administrari =
    administrareRepository
      .findAllByPacientIdWithMedicamentOrderByDataAdministrareDesc(pacientId);

List<ReactieAdversa> reactiiSevere =
    reactieAdversaRepository
      .findByPacientIdAndSeveritate(pacientId, SeveritateReactie.SEVERA);
```

**Step 2 — P1: Check absolute contraindications**

For each prior administration the patient received, `matchesSmpc` checks whether the
administered drug appears in the *contraindications* section of the proposed drug's SmPC
(`Medicament.contraindicatii`). The check is done:

1. By matching the linked `Medicament.substanta` (active substance) — reliable.
2. By matching the linked `Medicament.denumire` (brand name).
3. As a text fallback using `Administrare.tipTratament`.

If a match is found → `winningPriority = P1`, `finalScore = 0.0`.

**Step 3 — P2: Check severe adverse-event history**

Only runs if P1 did not trigger. For each `ReactieAdversa` with severity `SEVERA`:

```java
private boolean sameSubstance(Medicament a, Medicament b) {
    if (a.getId().equals(b.getId())) return true;                   // same DB record
    String substanceA = safeLower(a.getSubstanta());
    String substanceB = safeLower(b.getSubstanta());
    if (!substanceA.isBlank() && substanceA.equals(substanceB)) return true;  // same active substance
    String nameA = safeLower(a.getDenumire());
    String nameB = safeLower(b.getDenumire());
    return nameA.contains(nameB) || nameB.contains(nameA);          // brand name overlap
}
```

If the proposed drug matches a severe past reaction →
`winningPriority = P2`, `finalScore = min(20.0, baseScore)`.

**Step 4 — P3: Check major drug interactions**

For each prior administration, `matchesSmpc` checks against the *interactions* section of the
proposed drug's SmPC (`Medicament.interactiuni`). Each unique match increments
`interactionCount`. If any match:

`finalScore = max(0.0, baseScore − 30 × interactionCount)`

SmPC *warnings* (`Medicament.avertizari`) are also scanned at this stage and added as
informational codes (no priority escalation).

**Step 5 — P4: Check treatment efficacy history**

Reads `Pacient.toleranta` and `Pacient.istoricTratament` for keywords indicating poor
response: `slab`, `ineficient`, `fara raspuns`, `fără răspuns`, `rezistenta`, `rezistență`.

`finalScore = max(0.0, baseScore − 15.0)`

**Step 6 — Build clinical features**

`buildFeatures` creates a `DecisionFeatures` value-object with 11 numeric fields used both
for the ML model and for the audit log:

```
varsta, sexF, greutate, inaltime,
hasDiabet, hasHTA,
adminCount,
hasMetformin, hasInsulina,
isWegovy, isMounjaro
```

**Step 7 — Optionally apply ML model**

The SMILE RandomForest model is consulted only when no P1–P4 rule fired (preventing an
under-calibrated model from overriding safety rules). See §4.4.

**Step 8 — Assemble `DecisionResult`**

The record captures: `score`, `recomandare`, `warnings`, `reguliTriggered`,
`features`, `finalDecision`, `decisionSource`, `overrideReason`, `auditSummary`.

**Step 9 — Persist `DecisionLog`**

`persistAudit` maps the `DecisionResult` to a `DecisionLog` entity and saves it:

```java
DecisionLog log = new DecisionLog()
    .timestamp(Instant.now())
    .actorType(actorType)
    .recomandare(result.recomandare())
    .modelScore(result.score())
    .reguliTriggered(String.join(" | ", result.reguliTriggered()))
    .externalChecks(result.auditSummary())
    .finalDecision(result.finalDecision())
    .decisionSource(result.decisionSource())
    .overrideReason(result.overrideReason())
    .alocare(alocare);

decisionLogRepository.save(log);
```

### 4.4 Machine-Learning Integration

* **Model type:** SMILE `RandomForest` (binary classifier, class 0/1).
* **Loaded at runtime** by `MlTrainingService`; injected via `setSmileModel(model)`.
* **Features:** 11 numeric values from `DecisionFeatures.toArray()`.
* **Thresholds:**
  * Prediction `1` (likely positive outcome) → score raised to at least 70.0.
  * Prediction `0` (likely negative outcome) → score capped at 40.0.
* **Safety guard:** ML is ignored when any P1–P4 rule has already fired.

### 4.5 Recommendation Text

`buildRecomandare` produces a Romanian-language string based on the winning priority:

| Priority | Recommendation prefix                                                          |
|----------|--------------------------------------------------------------------------------|
| P1       | `NU recomand <drug>: contraindicație absolută detectată.`                      |
| P2       | `ATENȚIE: <drug> contraindicat – reacție adversă severă în istoric.`           |
| P3       | `PRECAUȚIE: <drug> – interacțiune majoră detectată. Necesită monitorizare.`    |
| P4       | `Reevaluați <drug> – eficacitate insuficientă înregistrată.`                   |
| P5/P6    | `Propune <drug> cu monitorizare`                                               |

---

## 5. How ReactieAdversa Feeds into DecisionLog

The two features are connected through the **P2 rule** inside the Decision Engine.

```
Timeline example
────────────────────────────────────────────────────────────────

T1  Doctor allocates Drug A to Patient 7.
    DecisionEngine runs P2 check → no severe AE found.
    DecisionLog #1 created: score=85, P6_DEFAULT

T2  Patient takes Drug A and has a severe allergic reaction.
    POST /api/reactie-adversas
      { "severitate": "SEVERA", "pacient": 7, "medicament": Drug A, ... }
    ReactieAdversa #10 stored in DB.

T3  Doctor proposes Drug A again (or re-evaluates).
    DecisionEngine reloads ReactieAdversa records for Patient 7.
    findByPacientIdAndSeveritate(7, SEVERA) now returns ReactieAdversa #10.
    sameSubstance(ra.getMedicament(), proposedDrug) → true.
    P2 rule fires.
    DecisionLog #2 created: score=20, P2_SEVERE_AE, override=Drug A
```

This loop ensures that the audit trail automatically reflects all new adverse-reaction
data without any manual intervention.

---

## 6. Entity Relationship Summary

```
Pacient (1) ────────────────────────────────────────────────────(N) ReactieAdversa
    │                                                                     │
    │ (1:N)                                                               │ (N:1)
    ▼                                                                     ▼
AlocareTratament (1) ──────────────────────────────(N) DecisionLog    Medicament
    │ (N:1)
    ▼
  Medicament
```

Key cardinalities:

* One `Pacient` → many `ReactieAdversa` records.
* One `Pacient` → many `AlocareTratament` records.
* One `AlocareTratament` → many `DecisionLog` records (one per evaluation call).
* Each `ReactieAdversa` points to one `Medicament` and one `Pacient`.
* Each `DecisionLog` points to one `AlocareTratament` (and indirectly to the patient
  and drug through that allocation).

---

## 7. API Quick Reference

### ReactieAdversa

| Method   | Path                          | Description                             |
|----------|-------------------------------|-----------------------------------------|
| `POST`   | `/api/reactie-adversas`       | Create a new adverse reaction record    |
| `GET`    | `/api/reactie-adversas`       | List all (pageable, filterable)         |
| `GET`    | `/api/reactie-adversas/count` | Count matching records                  |
| `GET`    | `/api/reactie-adversas/{id}`  | Get single record                       |
| `PUT`    | `/api/reactie-adversas/{id}`  | Full update                             |
| `PATCH`  | `/api/reactie-adversas/{id}`  | Partial update                          |
| `DELETE` | `/api/reactie-adversas/{id}`  | Delete record                           |
| `POST`   | `/api/adverse-reports`        | Shortcut: report by pacientId only      |

### DecisionLog (read-only)

| Method | Path                                    | Description                                    |
|--------|-----------------------------------------|------------------------------------------------|
| `GET`  | `/api/decision-logs`                    | List all (pageable, filterable)                |
| `GET`  | `/api/decision-logs/count`              | Count matching records                         |
| `GET`  | `/api/decision-logs/{id}`               | Get single log entry                           |
| `GET`  | `/api/decision-logs/by-alocare/{id}`    | Get all logs for one treatment allocation      |
