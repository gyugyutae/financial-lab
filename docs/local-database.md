# 로컬 MySQL 환경

Windows의 Java/Gradle과 PowerShell을 그대로 사용하고, Docker Desktop의 WSL 2 엔진에서 MySQL 8.4를 실행한다.

## 최초 준비

Docker Desktop을 실행한다. 새 환경에서는 `.env.example`을 `.env`로 복사하고 두 비밀번호를 서로 다른 임의의 영문·숫자 문자열로 바꾼다. `.env`는 Git에서 제외된다. 현재 PC의 `.env`는 환경 구축 시 생성했다.

설치 전부터 열어 둔 터미널에서 `docker`를 찾지 못하면 PowerShell을 새로 연다. 계속 인식되지 않으면 현재 세션에 경로를 추가한다.

```powershell
$env:Path = "$env:LOCALAPPDATA\Programs\DockerDesktop\resources\bin;$env:Path"
```

저장소 루트에서 실행한다.

```powershell
docker compose up -d --wait
docker compose ps
.\gradlew.bat bootRun --args="--spring.profiles.active=mysql"
```

IDE에서는 작업 디렉터리를 저장소 루트로, 활성 프로필을 `mysql`로 설정한다. `config/application-mysql.properties`가 `.env`에서 접속 비밀번호를 읽는다. 기본 프로필의 테스트는 기존 설정을 사용한다.

## 접속 정보

| 항목 | 값 |
|---|---|
| 호스트 / 포트 | `127.0.0.1:3306` |
| 데이터베이스 | `financial_lab` |
| 애플리케이션 사용자 | `financial_lab` |
| 비밀번호 | 로컬 `.env`의 `MYSQL_PASSWORD` |
| 데이터 볼륨 | `financial-lab_mysql-data` |

DB 셸 접속 후 비밀번호를 입력한다.

```powershell
docker compose exec mysql mysql -u financial_lab -p financial_lab
```

접속 확인용 SQL:

```sql
SELECT DATABASE(), CURRENT_USER(), VERSION();
SHOW TABLES;
```

## 중지와 재시작

```powershell
docker compose stop
docker compose up -d --wait
```

`docker compose down`도 데이터 볼륨을 보존한다. `docker compose down -v`는 DB 데이터를 삭제하므로 사용하지 않는다. `.env`의 비밀번호 변경만으로 이미 생성된 DB 계정의 비밀번호가 바뀌지는 않는다.

## 이번 단계의 범위

DB 실행과 Spring 접속 환경을 준비한다. 계좌 엔티티와 JPA Repository 구현은 이후 사용자가 직접 작성한다. 현재 계좌 API의 저장 방식이 자동으로 DB로 바뀌지는 않는다.

`ddl-auto=validate`는 기존 스키마를 검사하며 테이블을 생성하지 않는다. 엔티티를 추가할 때 테이블 설계와 생성 방법도 함께 정한다. 재시작 후 계좌 유지와 이체 rollback은 별도로 구현·검증해야 한다.

로컬 JDBC 설정의 TLS 비활성화는 이 PC 내부 개발 접속용이다.

## 환경 구축 검증 — 2026-09-28

- Docker Desktop 4.92.0, Docker Engine 29.8.0, Compose 5.5.1 설치 및 실행 확인.
- `mysql:8.4` 이미지로 실행한 MySQL 8.4.11에서 프로젝트 계정의 접속 및 SQL 실행 확인.
- 임시 테이블의 `DECIMAL(19,2)` 값 `12345.67`이 DB 컨테이너 재시작 후 유지됨을 확인하고 임시 테이블 삭제.
- `mysql` 프로필로 Spring을 실행해 MySQL JDBC 연결과 서버 기동 확인 후 검증용 Spring 프로세스 종료.
- 기존 테스트 재실행: 16개 통과, 실패·오류·건너뜀 0개.
- 계좌 API의 DB 저장 및 이체 rollback은 이번 검증 범위에 포함되지 않음.
