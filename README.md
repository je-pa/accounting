# 🧪 실행 및 테스트 가이드

이 프로젝트는 `Docker Compose`를 사용하여 PostgreSQL을 실행한 후, Spring Boot 애플리케이션을 실행하여 API를 테스트할 수 있습니다. 테스트는 Swagger UI를 통해 간편하게 수행할 수 있습니다.


### 1. 📦 Docker Compose로 PostgreSQL 실행

```bash
docker-compose up -d
```

* `docker-compose.yml`에 정의된 PostgreSQL 컨테이너가 실행됩니다.
* 실행 후, PostgreSQL은 기본적으로 `localhost:5432`에서 접속 가능합니다.


### 2. 🚀 Spring Boot 애플리케이션 실행

IDE에서 `Application.java`를 실행하거나, 터미널에서 아래 명령어로 실행합니다:

```bash
./gradlew bootRun
```


### 3. 🌐 Swagger UI로 API 테스트

애플리케이션이 정상적으로 실행되면, 아래 URL로 접속하여 API 문서를 확인하고 직접 테스트할 수 있습니다:

🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

* 테스트하려는 API의 Try it out 버튼을 통해 요청을 시도할 수 있습니다.
* 각 API의 설명과 요청/응답 스펙을 확인할 수 있으며, UI를 통해 직접 요청도 보낼 수 있습니다.
* 파일 업로드 등 multipart 요청도 Swagger UI에서 테스트할 수 있습니다.



아래는 요청하신 내용을 반영한 `README.md`의 **실행 및 테스트 가이드**와 **설계 및 보안 아키텍처** 섹션 예시입니다. 복사하여 `README.md`에 붙여 넣으시면 됩니다.


# 설계 및 보안 아키텍처

## 시스템 아키텍처 기술 스택

* **언어:** Java 21
* **프레임워크:** Spring Boot
  → REST API 설계에 적합하며 다양한 개발 생산성 도구(Spring Data JPA, Validation, Web 등)를 제공하여 빠르고 견고한 백엔드 구축이 가능합니다.
* **DBMS:** PostgreSQL
  → 정합성과 확장성이 뛰어나며, JSONB 등의 유연한 데이터 표현도 지원해 다중 사업체와 거래 데이터 처리에 적합합니다.
* **ORM:** Spring Data JPA + QueryDSL
  → 유지보수가 쉬운 도메인 기반 데이터 접근과, 복잡한 조회 쿼리를 타입 안정성과 함께 제공하기 위해 QueryDSL을 병행 사용했습니다.
* **API 문서화:** SpringDoc + Swagger UI
  → API 테스트 및 명세 확인을 위한 직관적인 문서화 도구입니다.
* **CSV 파싱:** OpenCSV
  → 거래 내역 등의 대량 파일 업로드 및 파싱 기능 구현에 활용됩니다.

## DB 스키마

다중 사업체와 분류(Category), 거래 내역(Transaction)을 효율적으로 저장하고 관리할 수 있도록 아래와 같이 설계되었습니다.

#### 💽 SQL CREATE TABLE

```sql
create table company
(
    id   varchar(255) not null
        primary key,
    name varchar(255)
);

create table category
(
    company_id varchar(255)
        constraint fk2twm010w181ypxiegra4o0rgc
            references company,
    id         varchar(255) not null
        primary key,
    name       varchar(255)
);

create table transaction
(
    balance_after         numeric(38, 2),
    deposit_amount        numeric(38, 2),
    withdrawal_amount     numeric(38, 2),
    id                    bigint not null
        primary key,
    transaction_date_time timestamp(6),
    branch                varchar(255),
    category_id           varchar(255)
        constraint fkgik7ruym8r1n4xngrclc6kiih
            references category,
    company_id            varchar(255)
        constraint fk663p7ti35m6atop4eipkfb0t
            references company,
    description           varchar(255)
);
```

## 자동 분류 로직

이 프로젝트는 사용자가 업로드한 거래 내역(CSV) 파일과 분류 규칙(JSON)을 기반으로, 거래 데이터를 자동 분류하고 저장하는 시스템입니다.

### ✅ 동작 흐름

1. **규칙 파일(JSON) 파싱 및 저장**
   * 업로드된 `rules.json` 파일을 파싱한 뒤, 규칙 정보를 저장합니다.
2. **거래 내역 파일(CSV) 파싱**
   * 사용자가 업로드한 거래 내역 CSV 파일을 `List<BankTransactionCsvRow>` 형태로 파싱합니다.
3. **Chunk 단위 처리**
   * 거래 내역을 100개 단위(Chunk Size)로 분할하여 처리합니다.
   * 각 Chunk마다 이력을 저장하고, 분류 성공 여부를 기록합니다.
4. **자동 분류 실행**
   * 각 거래 항목에 대해 규칙을 적용하여 일치하는 회사를 찾아 자동 분류합니다.
   * 분류된 항목은 `companyId`가 할당되고, 그렇지 않은 항목은 `unclassified`로 간주됩니다.
5. **결과 집계 및 반환**
   * 총 거래 수, 분류 성공 수, 실패 수를 집계하여 최종 결과로 반환합니다.

### 🛠 관련 주요 클래스 요약

| 클래스                  | 역할                     |
| -------------------- | ---------------------- |
| `FileParsingService` | rules.json 및 CSV 파일 파싱 |
| `RuleService`        | 파싱된 규칙 저장 및 조회         |
| `AccountingService`  | 분류 로직 수행 및 DB 저장       |
| `ChunkService`       | chunk 단위 저장 이력 관리      |
| `AccountingFacade`   | 전체 프로세스 총괄             |

### 💡 규칙이 복잡해질 경우의 확장 아이디어

예: 규칙이 다음과 같이 확장되는 경우

* 금액 구간 조건 (e.g. `"amount": { "min": 10000, "max": 50000 }`)
* 제외 키워드 조건 (e.g. `"exclude": ["시험", "테스트"]`)

#### 🚀 NoSQL 기반 확장 방안

* 규칙 조건이 복잡해지고 조합이 많아질수록 RDB에서 쿼리로 필터링하기엔 한계가 존재합니다.
* **MongoDB**와 같은 NoSQL을 사용하면 규칙 객체를 **JSON 문서 그대로 저장**하고,
  `$regex`, `$gt`, `$lt`, `$not` 등의 다양한 쿼리 연산자로 유연하게 조건 평가가 가능합니다.
* 또한 조건 기반 rule 검색이 빈번한 시스템에서는 NoSQL의 **문서 기반 질의 성능**이 유리할 수 있습니다.

### 보안 강화 방안

공인인증서 파일 및 비밀번호와 같은 민감한 정보는 암호화된 형태로 저장하고, 
HSM(Hardware Security Module) 또는 KMS(Key Management Service) 기반 키 관리 시스템을 활용하여 보안성을 강화합니다. 
접근 권한은 최소화하며 민감 데이터 접근은 로깅 및 감사를 통해 추적할 수 있도록 구성합니다.

### 문제 상황 해결책 제시
   
문제 발생 시, 해당 기능을 즉시 중단하고 로그를 통해 상세히 추적합니다. 
고객 구분 로직과 인증 정보를 점검하여 데이터 분리 기준의 오류 여부를 분석하고, 
테스트 케이스 강화 및 데이터 접근 제어 정책을 재정비하여 재발을 방지합니다.

아래는 당신이 실제로 진행한 변경 사항들을 바탕으로 작성한 **README.md의 "AI 활용 관련 내용" 섹션 예시**입니다. 당신의 의도와 오늘의 구조 개선 논의 내용을 반영했습니다:

## 🤖 AI 활용 및 실제 구현 결정 사항

이 프로젝트는 ChatGPT를 비롯한 LLM 기반 AI의 도움을 받아 개발 방향 설정, 구조 개선, 리팩토링 등의 과정을 진행하였습니다. 그 과정에서 다음과 같은 차이점이 발생했고, 이에 대한 판단과 선택의 배경은 다음과 같습니다.

### ✅ 구조 설계 관련 변경

* **초기 AI 제안**에서는 Controller → Service → Repository 구조로 구현 예시를 제시했지만, 실제 구현에서는 다음과 같이 변경하였습니다:

  * `Facade → Service → Repository` 구조로 계층을 명확히 분리하고 도메인 책임을 세분화하였습니다.
  * 사용자 입력과 데이터 흐름에 대한 통합 처리를 담당하는 `Facade` 계층을 추가하여 책임 분리를 강화하였습니다.
* **인프라 계층(`infrastructure`) 추가**

  * JPA, QueryDSL, OpenCSV 같은 외부 기술 의존성을 `infrastructure` 하위로 분리하였습니다.
  * `infrastructure/opencsv` 패키지로 외부 파일 파싱 책임을 격리시켜서, 도메인 로직에서 인프라 기술을 분리하였습니다.

### ✅ DTO 설계 및 리팩토링

* **초기 DTO는 일반 클래스였으나**, 이후 record 기반으로 전환하여 불변성과 간결성을 확보했습니다.

  * 예: `TransactionDto`, `BankTransactionCsvRow` → record 변환
  * 기존 클래스에서 record로 변환하는 메서드(`toRecord()` 등)를 추가하여 유연성도 확보함

### ✅ 기능적 개선

* **Chunk 단위의 거래 처리 방식 도입**

  * 최초에는 전체 거래 데이터를 한 번에 처리하였으나, 트랜잭션 규모가 커질수록 실패 시 복구가 어려워지는 문제를 고려해 chunk 단위(100건 기준)로 처리하도록 개선하였습니다.
  * 각 chunk에 대한 처리 이력(`ChunkHistory`)을 저장하고, 실패 시 재처리가 가능하도록 구현하였습니다.

