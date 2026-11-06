# ledger-reliability-lab

A money-transfer and ledger backend used as a test bed for **measured** backend engineering:
throughput limits and scaling behavior, concurrency correctness, failure reliability, and large-batch processing.

Money is a domain where a single wrong row matters, which makes it a good subject for verifying correctness and performance in the same experiment.
Every claim in this project is meant to be backed by a reproducible experiment (environment, script, raw results, commit hash).

> **Status (2026-11-07):** early stage. The account/transfer REST API and domain rules are implemented and tested; persistence is moving from an in-memory repository to Spring Data JPA + MySQL.
> Everything under [Roadmap](#roadmap) is planned work, not a finished result.

## Tech stack

Java 25 · Spring Boot 4 · Spring Data JPA · MySQL 8.4 · H2 (tests) · JUnit 5 · AssertJ · Docker Compose

Planned: GitHub Actions · k6 · Prometheus · Grafana · cloud VMs for load testing

## What works today

- REST API: create account, get account, deposit, withdraw, transfer between accounts
  - `POST /accounts`, `GET /accounts/{id}`, `POST /accounts/{id}/deposits`, `POST /accounts/{id}/withdrawals`, `POST /accounts/{fromAccountId}/transfers`
- Domain rules enforced inside the `Account` entity: positive `BigDecimal` amounts only, no negative balance, no self-transfer
- Structured error responses for not-found / insufficient-balance / invalid-input via a global exception handler
- Unit tests for failure paths, including "a rejected withdrawal leaves the balance unchanged"

## How to run

```bash
# tests (H2)
./gradlew test

# local MySQL
cp .env.example .env          # then set two different random passwords
docker compose up -d
./gradlew bootRun --args='--spring.profiles.active=mysql'
```

## Roadmap

The project is organized as four experiments. Each one has a fixed structure: problem → cause → solution with a comparison table → measured results with conditions and limits.

| # | Question | Key measurements |
|---|---|---|
| 1 | **How far does one server go, and does adding servers help?** Max stable throughput, scale-out from 1 → 3 API instances, hot-account load, connection-pool sizing, bottleneck identification (CPU / pool / DB locks / IO) | max stable RPS, scaling factor, p99, saturated resource |
| 2 | **Why doesn't a hot account scale?** Compare pessimistic locking, optimistic locking and conditional `UPDATE`; remove deadlocks by acquiring row locks in account-id order | deadlocks, retries, hot-account TPS, p99, invariant violations |
| 3 | **What happens when a worker dies or a response is lost mid-transfer?** Transactional outbox, worker leases with `SKIP LOCKED`, idempotency keys, an explicit `UNKNOWN` state resolved by status lookup; verified with `kill -9` | lost jobs, duplicate money movement, recovery time |
| 4 | **Can reconciliation run on a large ledger without hurting online traffic?** Keyset pagination and indexing, chunk checkpoints for resume, throttling under concurrent API load | batch duration, reprocessing on resume, API p99 impact, detection accuracy |

### Measurement environment (planned)

Load generator, API instances and MySQL run on **separate, non-burstable VMs** in the same zone.
API containers have fixed CPU/memory limits behind nginx.
Load uses k6 `constant-arrival-rate`; each run is 2 min warm-up + 5 min measurement, repeated 3 times (median reported).
"Max stable throughput" = highest RPS that keeps p99 < 200 ms and error rate < 0.1 %.
Every experiment is stored under `experiments/<id>/` with the commit hash, k6 script, raw results and Grafana snapshots.

### Design docs

An RFC-style design document (`docs/rfc/0001-reliable-external-transfer.md`) will be written **before** implementing experiment 3, and its Results section added after the experiment.

## Results

_No load-test results yet. This table will be filled as experiments complete._
