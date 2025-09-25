# SKALA STOCK API Practice

Spring Boot 기반 주식 거래 백엔드 API + Streamlit 기반 웹 클라이언트 연습 프로젝트

플레이어를 생성하고 초기 자금을 설정한 뒤, 주식을 매수/매도하여 포트폴리오를 관리할 수 있습니다.

> 📢 이 프로젝트는 [skala-stock-api](https://github.com/lsmin625/skala-stock-api) 레포지토리의 practice 브랜치를 기반으로 수정하였습니다.

> 백엔드와 프론트엔드가 Dockerfile 및 docker-compose로 연결됩니다.

## 📌 주요 기능

- **플레이어 관리**

  - 플레이어 생성 및 로그인
  - JWT 기반 세션 인증
  - 플레이어 정보 조회

- **주식 관리**

  - 전체 주식 리스트 조회
  - 주식 생성 / 수정 / 삭제

- **주식 거래**

  - 주식 매수 / 매도
  - 포트폴리오 자동 갱신

- **프론트엔드 (Streamlit)**

  - 플레이어 / 주식 페이지
  - 백엔드 API 연동
  - API 호출 결과 확인 가능

## 🚀 실행 방법

### 1. Docker Compose 실행

```bash
# 레포지토리 클론
git clone git@github.com:seonhyeongg/study.git
cd study/java-practice/skala-stock-api-practice

# 도커 실행
docker-compose up --build
```

- 백엔드 API: [http://localhost:8080/api](http://localhost:8080/api)
- 프론트엔드 UI: [http://localhost:8501](http://localhost:8501)

### 2. 직접 실행 (옵션)

#### 백엔드 (Spring Boot)

```bash
cd skala-stock-api-backend
mvn spring-boot:run
```

#### 프론트엔드 (Streamlit)

```bash
cd skala-stock-api-client
streamlit run app.py
```

## 📂 프로젝트 구조

```bash
skala-stock-api-practice/
├── docker-compose.yml                  # 백엔드 + 프론트엔드 컨테이너 실행 설정
├── skala-stock-api-backend/            # Java Spring Boot 기반 API 서버
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       ├── main/
│       │   ├── java/com/sk/skala/stockapi/
│       │   │   ├── controller/         # REST API 컨트롤러
│       │   │   ├── service/            # 비즈니스 로직
│       │   │   ├── data/               # DTO, Entity, Repository
│       │   │   └── config/             # JWT, SessionHandler 등 설정
│       │   └── resources/
│       │       └── application.yml     # 스프링 부트 환경 설정
│       └── test/                       # 테스트 코드
│
└── skala-stock-api-client/             # Python Streamlit 기반 UI
    ├── app.py                          # Streamlit 진입점
    ├── Dockerfile
    ├── core/                           # 프론트엔드 핵심 모듈
    │   ├── config.py
    │   ├── http.py                     # API 요청
    │   ├── ui.py                       # UI 컴포넌트
    │   ├── state.py                    # 전역 상태 관리
    │   └── table.py                    # 표 렌더링
    └── pages/                          # Streamlit 멀티 페이지
        ├── 1_players.py                # 플레이어 관리 페이지
        └── 2_stocks.py                 # 주식 거래 페이지

```
