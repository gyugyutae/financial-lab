---
type: wiki-page
aliases:
  - Reliable Bank Transfer Ledger Lab
  - Financial Lab
description: "Project hub for 김규태's self-implemented Java and Spring financial backend portfolio, including goals, milestones, evidence, and learning decisions."
author:
  - "[[김규태]]"
date created: 2026-09-26
date modified: 2026-10-16
tags:
  - project
  - portfolio
  - java
  - spring-boot
  - banking
  - backend
source:
  - "[[2026-09-13-Q-java-bank-transfer-ledger-portfolio-design]]"
  - "[[김규태 Java-Spring-AWS 금융 포트폴리오 기초 학습 트랙]]"
related:
  - "[[MOC-Portfolio]]"
  - "[[김규태 금융 시스템 개발자 취업 로드맵]]"
confidence: low
layer: entities
explored: true
claimType: mixed
evidenceScope: multi-source-mixed
verificationStatus: disputed
verifiedAt: 2026-10-15
verifiedBy: agent
disputed: true
status: active
---

# financial-lab

> [!warning] Disputed Claim
> 2026-10-15 감사에서 접근한 `C:\Users\user\Desktop\financial-lab` 사본(HEAD `b9d38142a9b478f704a8528f36ce6d49ed69685e`, 미커밋 변경 있음)은 `AccountService.createAccount`만 있고 `AccountController`는 비어 있었다. 아래 09-26 이체 API·테스트 완료 기록의 기준 경로 `C:\Users\kym70\financial-lab`는 현재 접근되지 않는다. 서로 다른 사본·시점·브랜치일 수 있으므로 아래 과거 기록을 삭제하지 않으며, 현재 구현 완료 상태로 재사용하지 않는다. 기준 저장소와 해당 commit·테스트 로그의 연결이 확인될 때까지 충돌은 미해결이다.

## 2026-10-15 원본 대조

- 현재 접근한 코드: `/mnt/c/Users/user/Desktop/financial-lab/src/main/java/com/gyutae/financiallab/account/application/AccountService.java`, `account/api/AccountController.java`, `account/domain/Account.java`.
- 계좌 도메인의 입금·출금·잔액 검증 코드는 있다. 이 사본에서 이체 API·이체 서비스·당시 실패 테스트 완료는 확인하지 못했다.
- `bash ./gradlew test --console=plain`은 compileTestJava에서 7개 오류로 실패했다. 테스트 파일명과 public class 불일치, 잘못 참조한 AccountService, 선언되지 않은 account 등이 원인이다. 테스트 실행 전 컴파일 실패이므로 테스트 실패율이나 금융 로직의 정상 여부를 판정하지 않는다.
- 이번 코드 열람은 작성자·과거 구현 부재·전체 학습 성과를 판정하지 않는다. 10-14 질의에 기록된 JUnit 검증 자기보고도 별도 시점의 자료로 보존한다.
- 후속 증거: 실제 작업 저장소 경로, branch/commit, 이체 구현 파일, 해당 commit의 테스트 결과. 상세 대조는 [[2026-10-15-Q-wiki-verification-results]] 참조.

> [!note] Bias Check
> Counter-argument: 동명 사본이 오래된 복제본이라면 현재 코드와 과거 완료 기록의 차이는 역량이나 기록 오류를 뜻하지 않는다.
> Data gap: 09-26 기준 사본·commit·테스트 로그와 현재 작업 사본 사이의 이력이 연결되지 않았다.

## 목표

김규태의 1차 취업 목표인 은행·증권사·핀테크 금융권 Java 백엔드 신입 직무를 위해, 계좌 이체와 거래 정합성을 직접 설계·구현·검증하는 대표 포트폴리오를 만든다. 코드 저장소 이름은 `financial-lab`, 포트폴리오 프로젝트명은 `Reliable Bank Transfer Ledger Lab`을 사용한다.

원래 목표는 단순한 계좌 CRUD가 아니다. 동시 요청, 중복 요청, 중간 장애 상황에서 잔액과 거래 기록을 보존하는 이체 시스템으로 확장하고, 그 근거를 테스트·설계 기록·재현 가능한 실행 절차로 남기는 것이다. 상세 범위와 단계는 [[2026-09-13-Q-java-bank-transfer-ledger-portfolio-design]]을 기준으로 한다.

## 작업 공간의 역할

| 공간 | 기준으로 둘 것 |
|---|---|
| `C:\Users\kym70\financial-lab` | 실제 Java 코드와 테스트의 기준 저장소. 코딩 Codex 세션은 이 디렉터리를 작업 루트로 연다. |
| 이 위키의 `financial-lab` 페이지 | 목표, 현재 단계, 결정 이유, 막힌 점과 해결, 학습 진척, 포트폴리오 증거의 허브 |
| `financial-lab/AGENTS.md` | 코딩 세션이 따를 프로젝트 목표와 멘토링 방식. 사용자가 코드를 직접 작성한다는 지속 규칙을 둔다. |

코드 작업과 위키 정리는 서로 다른 저장소에서 한다. 코드는 `financial-lab`에서 수정한다. 프로젝트 기록은 사용자가 하루 작업 종료와 기록을 명시적으로 요청했을 때만 그날 내용을 모아 이 페이지와 Wiki 활동 로그에 반영한다. 일회성 개념 질문은 기록 요청이 없는 한 프로젝트 일지에 옮기지 않는다.

## 협업 원칙

- 사용자가 Java와 테스트 코드를 직접 작성한다.
- Codex는 목표를 한 번에 작은 구현 단위로 나누고, 필요한 개념·흐름·힌트를 설명한다.
- 사용자가 작성한 코드는 리뷰하고, 컴파일·런타임·테스트 오류의 원인을 설명한다.
- 전체 구현 코드를 먼저 제공하지 않는다. 사용자가 직접 작성할 수 있도록 요구사항, 순서, 의사 코드와 검증 기준을 제시한다.
- 기능을 완료해도 매번 위키 기록을 갱신하지 않는다. 사용자가 하루 작업 종료와 기록을 명시적으로 요청할 때만 그날의 작업을 한 번에 묶어 기록한다.

## 현재 구현 상태 — 2026-09-26

현재 단계는 **메모리 저장소를 이용한 계좌 이체 API의 첫 수직 절편**이다.

- `Account` 도메인에서 `BigDecimal` 잔액, 입금·출금 금액 검증, 잔액 부족 규칙을 다룬다.
- API에서 계좌 생성·조회·입금·출금·이체 요청을 받고 DTO로 응답한다.
- `AccountService`가 `ConcurrentHashMap`과 `AtomicLong`을 사용해 계좌를 메모리에 보관하고 업무 흐름을 처리한다.
- 이체 서비스는 같은 계좌 여부를 검사하고 양쪽 계좌를 조회한 뒤 출금·입금을 수행한다.
- 계좌 도메인 테스트와 정상 이체, 잔액 부족·수취 계좌 미존재·동일 계좌 이체의 예외 및 잔액 유지 테스트가 있다. 2026-09-26 확인한 Gradle 테스트는 통과했다.
- 사용자는 정상 이체를 실제 HTTP API 요청으로도 확인했다고 보고했다.

### 현재 저장소 재확인 — 2026-09-28

읽기 전용 코드 점검에서 financial-lab 저장소는 main 브랜치의 8d7234c에 있고 작업 트리는 깨끗했다. 이번 점검에서는 테스트를 실행하지 않았다.

- AccountService는 ConcurrentHashMap<Long, Account>와 AtomicLong을 사용하고, transfer는 송금·수취 계좌를 찾은 뒤 출금하고 입금한다. 저장소와 DB transaction은 아직 연결되어 있지 않다.
- AccountTest에는 계좌 입금·출금·잔액 부족·0원 입금 등 도메인 테스트와 정상 이체 테스트가 있다. FinancialLabApplicationTests에는 contextLoads 테스트가 있다.
- 현재 테스트 파일에는 AccountService 이체의 잔액 부족, 수취 계좌 미존재, 동일 계좌, 예외 주입 후 상태 복구, 동시 요청 테스트가 보이지 않는다. 이는 2026-09-26 작업 기록과 현재 checkout 사이의 차이다. 과거 사용자 보고는 기록으로 보존하되, 현재 재현 가능한 증거와 구분한다.
- Gradle에는 Spring Data JPA, H2, MySQL 의존성이 선언되어 있지만 현재 Java 소스에는 JPA Entity, Repository, Migration 구현이 없다. 의존성 선언을 DB 영속화 완료로 표현하지 않는다.

### 아직 구현하지 않은 핵심

- Repository 계층과 영속 데이터베이스 저장
- JPA 엔티티·저장소 구성 및 DB 트랜잭션 rollback
- 거래/이체 상태와 차변·대변 `LedgerEntry` 원장
- 동시 출금 제어, 잠금 전략, 멱등성 키
- 원장과 잔액 대사, 부하·성능 측정, 배포 자동화

현재 `ConcurrentHashMap`은 서버 메모리 안에서만 데이터를 보관한다. 데이터베이스 커밋·rollback이 없으므로 이 상태를 금융 거래의 원자성이나 영속성이 완성됐다고 표현하지 않는다. 현재 코드는 포트폴리오 완성본이 아니라 직접 구현 능력을 쌓는 초기 단계다.

## 단계별 진행 방향

1. **메모리 기반 기능과 예외 테스트**: 계좌 및 이체 성공·실패·경계 조건을 직접 구현하고 검증한다.
2. **Repository와 JPA/DB**: 메모리 저장을 영속 저장으로 교체하고 SQL과 제약조건을 확인한다.
3. **이체 트랜잭션과 원장**: 강제 실패에서 출금·입금·원장 기록이 함께 rollback되는지 검증한다.
4. **동시성 및 중복 요청**: 재현 테스트를 먼저 만들고 잠금·멱등성 설계를 비교한다.
5. **대사·성능·운영**: 불일치 탐지, 인덱스 전후 실행계획·지연시간, 배포와 장애 복구를 증거화한다.

한 번에 다음 한 기능만 진행한다. 세부 설계가 현재 이해도나 코드와 맞지 않으면 [[2026-09-13-Q-java-bank-transfer-ledger-portfolio-design]]을 실제 관찰에 맞춰 조정한다.

## 진행 기록

### 일일 기록 운영 방식

- `완료`, 테스트 리뷰, 짧은 질문만으로 프로젝트 진행 로그를 추가하지 않는다. 사용자가 하루 기록을 요청할 때만 기록한다.
- 하루 기록은 목표·금융 불변조건, 사용자가 선택한 설계와 이유, 문제 증상·원인·해결, 테스트/API 검증, 사용자가 배운 점, 사용자와 AI의 기여 경계, 다음 단계로 구성한다.
- 기록 전, 사용자의 의도·학습처럼 코드에서 확인할 수 없는 내용은 짧게 질문한다. 포트폴리오 증거를 강화할 추가 질문도 1~3개로 묶어 묻는다. 설계 대안·트레이드오프, 실패 원인 분석, 정량 검증, 한계 중 아직 확인되지 않은 항목을 우선한다.
- 답을 받지 못한 주관적 학습이나 성과는 만들어내지 않고 미확인으로 남긴다. 코드에서 본 사실, 사용자가 보고한 사실, 아직 검증하지 않은 내용을 분리한다.
- 같은 날짜 작업은 이 페이지의 일일 항목 하나와 Wiki `log.md`의 일일 기록 하나로 합친다. 단계마다 별도 기록을 만들지 않는다.

### 2026-09-26 — 이체 API와 서비스 테스트의 첫 단계

- **목표**: 계좌 간 이체의 성공 흐름과 잔액 부족 실패 흐름을 구현한다.
- **구현/검증**: 사용자가 컨트롤러부터 서비스까지 직접 연결했고, 정상 이체 API와 서비스 단위 테스트를 확인했다. 잔액 부족 테스트는 예외 타입과 양쪽 계좌 잔액이 유지되는지 검증하도록 작성했다. 테스트 실행 결과 `BUILD SUCCESSFUL`을 확인했다.
- **학습/판단**: `TransferRequest`는 외부 입력, `TransferResult`는 서비스 결과, `TransferResponse`는 외부 응답으로 구분했다. 실패 테스트는 기대한 예외 타입(필요하면 메시지까지) 발생 여부만 확인하고 끝내지 않고, 실패 후 원래 송금 계좌 ID로 다시 조회해 잔액이 그대로인지도 확인해야 한다는 점을 배웠다. `IllegalArgumentException`이나 `AccountNotFoundException`이 발생해도 이미 존재하는 계좌의 잔액 불변조건은 별도로 검증해야 한다.
- **현재 다음 작업**: `AccountService`에서 메모리 저장 책임을 분리해 Repository 경계를 만든다. 먼저 메모리 저장 방식을 유지해 계층 책임과 의존성 주입을 익힌다.
- **기여 경계**: 사용자 보고와 저장소에서 확인된 코드·테스트 결과를 구분해 기록한다. 이후 포트폴리오 문장으로 쓰기 전에는 각 주장을 다시 실행하거나 설명할 수 있는지 확인한다.

### 2026-09-26 — 존재하지 않는 수취 계좌 테스트

- **목표**: 수취 계좌를 찾지 못했을 때 출금이 먼저 일어나지 않는지 확인한다.
- **구현/검증**: 사용자가 존재하지 않는 수취 ID로 이체하는 테스트를 추가했다. `AccountNotFoundException` 발생과 송금 계좌 잔액 27,000원 유지까지 검증했고, `gradlew test`가 통과했다.
- **학습/판단**: 이체 전에 양쪽 계좌를 모두 찾는 순서가 수취 계좌 누락으로 인한 송금 계좌의 선출금을 방지한다.
- **다음 한 단계**: 같은 계좌 ID를 송금·수취 양쪽에 넣었을 때 예외가 발생하고 잔액이 유지되는지 검증한다.

### 2026-09-26 — 동일 계좌 이체 테스트

- **목표**: 송금 계좌와 수취 계좌가 같으면 이체를 거절하고 잔액을 유지한다.
- **구현/검증**: 사용자가 같은 계좌 ID와 양수 금액으로 이체하는 단위 테스트를 추가했다. `IllegalArgumentException` 발생과 잔액 유지를 확인했다. 리뷰 중 전체 Gradle 테스트가 통과했다.
- **다음 한 단계**: 서비스가 직접 들고 있는 Map 저장 책임을 별도 Repository 경계로 옮긴다. 우선 메모리 구현을 유지해 동작을 보존한다.

### 다음 기록 템플릿

- **날짜 / 완료한 기능**:
- **지키려던 금융 규칙**:
- **내가 선택한 설계와 이유**:
- **막힌 증상 → 원인 → 해결**:
- **테스트·API·로그로 확인한 결과**:
- **내가 설명·수정할 수 있게 된 개념**:
- **AI가 도운 범위 / 내가 직접 결정·작성·검증한 범위**:
- **다음 한 단계**:

## 포트폴리오 표현 경계

현재 checkout에서 코드로 말할 수 있는 것은 “Java/Spring Boot로 메모리 기반 계좌 생성·조회·입금·출금·이체 API를 구현하고, 계좌 도메인 규칙과 정상 이체 테스트를 작성했다” 정도다. 2026-09-26 Wiki 기록의 추가 이체 실패 테스트와 HTTP 검증은 현재 테스트 파일과 대조하여 재확인하기 전까지 최신 이력서에 넣지 않는다. DB 영속성, 실제 transaction rollback, 원장, 멱등성, 동시성, 성능 수치를 구현·측정하기 전에는 완료 경험으로 표현하지 않는다.

## Related

- [[MOC-Portfolio]]
- [[2026-09-13-Q-java-bank-transfer-ledger-portfolio-design]]
- [[김규태 금융 시스템 개발자 취업 로드맵]]
- [[김규태 Java-Spring-AWS 금융 포트폴리오 기초 학습 트랙]]

## Sources

- [[2026-09-13-Q-java-bank-transfer-ledger-portfolio-design]]
- [[김규태 Java-Spring-AWS 금융 포트폴리오 기초 학습 트랙]]

## 객체지향 설계 학습 자료 — 2026-10-16

- [[financial-lab 객체지향 설계 학습 및 검토 가이드]] — 사용자 수집 목적에 따른 설계 학습 기준.
- [[객체지도와 의사 코드로 AI 설계를 검토하는 질문]] — 코드 근거·책임·실패 조건으로 AI 설계를 검토하는 질문.
- [[2026-10-16-YouTube-객체지향 마스터 클래스와 financial-lab 설계 메모]] — 강의 전사와 문서 말미 사용자 메모.

이 연결은 학습 자료 인제스트이며 일일 구현 기록이 아니다. 기존 구현 상태와 검증 일자는 이번 작업에서 갱신하지 않았다.
