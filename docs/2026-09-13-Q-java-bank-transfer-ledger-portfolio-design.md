---
type: query-result
aliases:
  - Java 은행 백엔드 대표 포트폴리오 설계
  - 안전한 계좌이체 원장 프로젝트
description: "A scoped Java and Spring Boot portfolio design for proving transaction integrity, concurrency control, idempotency, SQL, testing, and operations in bank backend recruiting."
author:
  - Codex
date created: 2026-09-13
date modified: 2026-09-26
tags:
  - query-result
  - java
  - spring-boot
  - banking
  - backend
  - ledger
  - portfolio
source:
  - "[[Core Context]]"
  - "[[김규태 금융 시스템 개발자 취업 로드맵]]"
  - "[[김규태 Java-Spring-AWS 금융 포트폴리오 기초 학습 트랙]]"
  - "[[2026-09-13-Q-bank-backend-entry-readiness-gap]]"
  - "[[MOC-Portfolio]]"
  - https://www.yes24.com/product/goods/183794163
reusableFor:
  - 취업/포트폴리오
  - 프로젝트 증거
  - 백엔드/성능/금융 시스템
  - 독서/책/학습
confidence: high
verificationStatus: unverified
status: in-progress
---

# Java 은행 백엔드 대표 포트폴리오 설계

## 한 가지 추천

프로젝트 가칭은 `Reliable Bank Transfer Ledger Lab`으로 정한다.

> **동시 요청, 중복 요청과 서버 오류가 발생해도 돈이 두 번 빠지거나 사라지지 않는 Java 기반 계좌이체·거래원장 시스템**

일반 쇼핑몰 CRUD보다 은행의 계정계·코어뱅킹 업무와 직접 연결되고, 김규태에게 가장 필요한 Java·Spring·SQL·트랜잭션·동시성·테스트·운영 증거를 한 저장소에서 만들 수 있다. 주식 주문 시스템보다 첫 도메인 규칙이 작아 Java 입문 단계에서도 완주 가능하며, 이후 증권 주문·결제·외부기관 연동으로 확장할 수 있다.

## 현실 문제와 불변조건

### 현실 문제

사용자가 A 계좌에서 B 계좌로 10만 원을 이체한다. 네트워크 지연으로 같은 요청을 다시 보내거나 두 이체가 동시에 들어오고, 출금 직후 예외가 발생할 수 있다. 시스템은 이런 상황에서도 잔액과 거래 기록을 보존해야 한다.

### 반드시 지킬 불변조건

1. 이체 금액은 0보다 크다.
2. 출금 계좌와 입금 계좌는 다르다.
3. 출금 계좌 잔액은 음수가 될 수 없다.
4. 한 이체의 출금과 입금은 함께 성공하거나 함께 실패한다.
5. 동일한 멱등성 키의 요청은 한 번만 처리한다.
6. 성공한 이체 한 건에는 합계가 0인 차변·대변 원장 항목이 정확히 한 쌍 존재한다.
7. 성공한 거래 기록은 수정하지 않고 정정 거래로 보완한다.

## 최소 도메인 모델

- `Customer`: 고객 식별 정보
- `Account`: 계좌번호, 상태, 현재 잔액, version
- `Transfer`: 출금계좌, 입금계좌, 금액, 상태, 멱등성 키
- `LedgerEntry`: 거래별 차변·대변 원장 항목과 거래 후 잔액
- `IdempotencyRecord`: 동일 요청의 처리 상태와 기존 응답

첫 상태는 `REQUESTED → SUCCEEDED | FAILED` 정도로 작게 시작한다. 외부 은행 연동을 모의할 때만 `PROCESSING`, `REVERSED`를 추가한다.

## MVP API

- `POST /accounts`: 테스트용 계좌 생성
- `GET /accounts/{id}`: 계좌·잔액 조회
- `POST /transfers`: 계좌이체, `Idempotency-Key` 헤더 사용
- `GET /transfers/{id}`: 이체 상태 조회
- `GET /accounts/{id}/ledger`: 계좌 원장 조회
- `POST /admin/reconciliation`: 잔액과 원장 불일치 탐지

로그인·회원가입·프론트엔드는 첫 버전에서 제외한다. Swagger 또는 API 요청 파일로 시연한다.

## 기술 구성

- Java 21
- Spring Boot 4
- Gradle
- MySQL 8
- Spring Data JPA와 직접 작성한 SQL
- JUnit 5, Spring Boot Test, Testcontainers
- Docker Compose
- GitHub Actions
- k6 또는 Gatling 부하 테스트

Redis, Kafka, Kubernetes와 MSA는 문제를 먼저 재현한 뒤 필요성이 생길 때만 추가한다.

## 8주 구현 순서

| 주차 | 구현 | 검증 증거 |
|---|---|---|
| 1 | Java만으로 `Account`, `Transfer`, 예외 구현 | 잔액 부족·잘못된 금액 JUnit |
| 2 | 원장 항목과 이체 서비스 | 출금·입금 합계 0, 실패 시 상태 불변 |
| 3 | Spring REST API와 예외 응답 | Controller-Service-Repository 흐름 설명 |
| 4 | MySQL 스키마와 transaction | 출금 후 강제 예외에서 전체 rollback |
| 5 | 동시 출금 문제 재현 | 100개 동시 요청 뒤 잔액 음수 0건 |
| 6 | 조건부 UPDATE 또는 lock, 멱등성 | 같은 키 20회 요청에서 원장 한 쌍만 생성 |
| 7 | 대사와 인덱스 | 불일치 탐지, `EXPLAIN` 전후 비교 |
| 8 | Docker·CI·부하 테스트·README | 새 환경 실행, p50·p95·p99와 오류율 기록 |

## 《스프링 부트 4 개발자 되기 + AI》와 연결한 탑다운 학습법

책을 완독한 뒤 프로젝트를 시작하지 않는다. 먼저 `계좌이체 한 건이 API에서 DB까지 성공한다`는 상위 흐름을 잡고, 구현 중 만난 하위 개념을 책과 작은 실험으로 내려가서 학습한 뒤 다시 전체 흐름으로 올라온다.

```text
사용자 이체 요청
→ API 계약
→ Controller
→ TransferService
→ Account·Transfer·LedgerEntry
→ Repository/JPA
→ MySQL transaction
→ 응답
```

### 책에서 지금 사용할 부분

| 책 범위 | 프로젝트에 바로 적용할 것 | 산출물 |
|---|---|---|
| 00장 | IntelliJ, Spring Initializr, Postman | 프로젝트 생성과 `/health` 실행 |
| 01장 | server/client, RDB·SQL, IP·port, framework, annotation | 요청이 어디로 들어오는지 설명 |
| 02장 | Spring Boot, IoC·DI, bean, auto configuration | `TransferService`를 주입해 사용 |
| 03장 | presentation·service·persistence 계층, 요청-응답 | package와 호출 흐름 구성 |
| 04장 | JUnit과 테스트 패턴 | 잔액 부족 실패 테스트부터 작성 |
| 05장 | ORM, JPA, persistence context, repository | `Account`, `Transfer` 저장 |
| 06장 | REST API, entity, repository, service, controller | 계좌 생성·조회와 이체 API |
| 부록 A~C | validation, exception, debugging | 잘못된 금액 응답과 오류 추적 |
| 12장 | AWS·RDS·배포 | 로컬 MVP 완료 후 배포 |
| 13장 | GitHub Actions CI/CD | 테스트 자동 실행과 배포 자동화 |

07장 Thymeleaf, 08장 Spring AI, 09~11장 로그인·JWT·OAuth2는 첫 MVP에서 제외한다. 인증은 핵심 정합성 실험이 끝난 뒤 별도 단계로 판단한다.

### 책 밖에서 별도로 내려가야 할 필수 개념

- Java: class·constructor·method, access modifier, interface, enum, exception, collection, `record`, `BigDecimal`, 불변 객체, `equals/hashCode`
- SQL: DDL·DML, PK·FK·UNIQUE·CHECK, JOIN, index, `EXPLAIN`
- DB: ACID, transaction boundary, isolation level, lost update, row lock, optimistic/pessimistic lock
- Spring: `@Transactional`의 proxy 동작, rollback 조건, validation, exception handler
- 금융: 계좌 잔액 불변식, 차변·대변 원장, 멱등성 키, 대사
- 테스트: 단위·통합·동시성 테스트의 차이, Testcontainers
- 운영: 로그, 환경변수, Docker, CI, 부하 테스트, p95·p99

모든 개념을 미리 공부하지 않는다. 현재 기능에 필요한 만큼만 학습하고 작은 코드와 테스트로 확인한다.

### 기능 하나의 위아래 학습 루프

1. 사용자 관점의 완료 문장을 한 줄로 쓴다.
2. 절대 깨지면 안 되는 불변조건을 정한다.
3. HTTP 요청·응답 예시를 먼저 작성한다.
4. 실패하는 테스트를 하나 만든다.
5. Controller부터 DB까지 최소 코드로 연결한다.
6. 로그와 실제 SQL을 확인한다.
7. 막힌 개념만 책·공식 문서·최소 예제로 내려가 공부한다.
8. 다시 프로젝트로 올라와 테스트를 통과시킨다.
9. AI 답을 닫고 흐름을 설명하거나 작은 변경을 직접 한다.
10. `내 판단 / AI 도움 / 검증 / 아직 모름`을 기록한다.

## 첫 시작점

### Stage 0 — 순수 Java 도메인, Spring 없이 시작

첫 구현은 REST API가 아니라 아래 동작이다.

```text
잔액 100,000원인 A 계좌
잔액 0원인 B 계좌
A에서 B로 30,000원 이체
결과: A=70,000원, B=30,000원
```

필요한 클래스는 `Account`, `TransferService`, `InsufficientBalanceException` 세 개뿐이다. JUnit으로 정상 이체, 잔액 부족, 0원 이하 이체를 검증한다. 여기서 Java class·constructor·method·exception·`BigDecimal`을 아래로 공부한다.

통과 기준은 코드를 외워 쓰는 것이 아니라 다음 질문에 답하는 것이다.

- 잔액 필드를 왜 외부에서 직접 수정하지 못하게 했는가?
- `withdraw()` 안에서 잔액 검증을 하는 이유는 무엇인가?
- 돈을 `double`이 아니라 `BigDecimal`로 표현한 이유는 무엇인가?
- 예외가 발생했을 때 두 계좌의 상태는 어떻게 되는가?

### Stage 1 — Spring 수직 절편

책 00~04장을 따라 Spring Boot 프로젝트를 만들고 `POST /accounts`, `GET /accounts/{id}` 한 쌍만 구현한다. 처음에는 메모리 repository를 써도 된다. HTTP 요청이 Controller→Service→Repository를 통과하는 흐름과 DI를 설명하면 통과다.

### Stage 2 — MySQL과 JPA

책 05~06장을 읽으며 메모리 repository를 JPA repository로 교체한다. Docker Compose로 MySQL을 실행하고 `Account`를 저장한다. entity lifecycle, persistence context, PK와 UNIQUE 제약, 생성 SQL을 확인한다.

### Stage 3 — 이체 happy path

`POST /transfers`를 구현해 한 transaction 안에서 출금, 입금, `Transfer`, 두 개의 `LedgerEntry`를 저장한다. 아직 lock과 멱등성을 넣지 않는다. 먼저 단일 요청의 정상 흐름을 끝까지 완성한다.

### Stage 4 이후 — 실패를 하나씩 추가

정상 흐름이 작동한 뒤에만 `강제 예외 rollback → 동시 출금 → 멱등성 → 대사 → index·성능 → 배포` 순서로 확장한다. 각 단계는 실패를 먼저 재현하고, 개념을 공부하고, 가장 작은 해결책을 적용하고, 전후 결과를 남긴다.

## 대표 실험 네 가지

1. **원자성 실험**: 출금 반영 후 의도적으로 예외를 발생시켜 출금과 입금이 모두 rollback되는지 확인한다.
2. **동시성 실험**: 잔액 10만 원 계좌에 8만 원 이체 두 건을 동시에 보내 한 건만 성공하는지 확인한다.
3. **멱등성 실험**: 응답 유실을 가정하고 같은 멱등성 키로 20번 재시도해 실제 이체가 한 번만 발생하는지 확인한다.
4. **성능 실험**: 거래내역 조회에 인덱스를 적용하기 전후의 실행계획과 p95·p99를 비교한다.

## 포트폴리오 완료 기준

- [ ] 다른 사람이 README만 보고 실행한다.
- [ ] 정상·실패·경계·동시성 테스트가 자동 실행된다.
- [ ] 한 이체의 DB transaction 경계를 설명한다.
- [ ] 조건부 UPDATE, 낙관적 잠금과 비관적 잠금 중 선택 이유를 설명한다.
- [ ] JPA가 만든 SQL과 직접 작성한 SQL을 읽을 수 있다.
- [ ] 인덱스 적용 전후 `EXPLAIN`과 latency 수치가 있다.
- [ ] 핵심 기능을 10분 동안 자료 없이 설명한다.
- [ ] 30분 안에 1일 이체한도 같은 작은 요구사항을 직접 추가한다.
- [ ] README에 `AI gave / I decided / I verified / Unknown`을 남긴다.

## 면접에서 말할 한 문장

> 중복·동시 요청과 중간 장애가 발생해도 출금과 입금, 거래원장의 정합성을 보존하도록 Java와 Spring Boot로 계좌이체 시스템을 구현하고, 멱등성·잠금·rollback을 자동 테스트와 p99 수치로 검증했습니다.

이 문장은 실제 테스트와 측정이 끝난 뒤에만 사용한다.

## 하지 않을 것

- 인터넷뱅킹 전체 서비스 구현
- 실제 은행 API·오픈뱅킹 연결
- 프론트엔드 제작에 시간 사용
- 처음부터 Kafka·Redis·Kubernetes·MSA 도입
- 기능 수를 늘리기 위한 대출·환전·카드 추가
- AI가 만든 코드를 설명하지 못한 채 완료 처리

> [!note] Bias Check
> 이 설계는 현재 목표인 은행권 Java 백엔드와 확인된 기술 격차에 맞춘 처방이다. 아직 사용자의 Java 구현 속도와 프로젝트 코드가 없으므로 8주 일정과 기술 선택은 미검증이다. 2주차 Java 도메인 모델과 테스트 결과에 따라 범위를 줄일 수 있다.

## Related

- [[김규태 금융 시스템 개발자 취업 로드맵]]
- [[김규태 Java-Spring-AWS 금융 포트폴리오 기초 학습 트랙]]
- [[2026-09-13-Q-bank-backend-entry-readiness-gap]]
- [[MOC-Portfolio]]

이 답변은 취업/포트폴리오, 프로젝트 증거, 백엔드/성능/금융 시스템, 독서/책/학습에 활용 가능합니다 — 은행권 백엔드의 정합성 문제를 Java·SQL·CS 학습과 재현 가능한 프로젝트 증거로 연결했기 때문입니다.
