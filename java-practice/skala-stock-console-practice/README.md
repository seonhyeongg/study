# SKALA STOCK Console Practice

Java Console 기반 주식 거래 연습 프로젝트

플레이어를 생성하고 초기 자금을 설정한 뒤, 주식을 매수/매도하여 포트폴리오를 관리하는 기능을 제공합니다.

> 📢 이 프로젝트는 [skala-stock-console](https://github.com/lsmin625/skala-stock-console) 레포지토리의 practice 브랜치를 기반으로 수정하였습니다.

## 📌 주요 기능

- **플레이어 관리**

  - 플레이어 ID 및 초기 투자금 입력
  - 플레이어 정보 조회

- **주식 관리**

  - 전체 주식 리스트 조회
  - 기본 주식 데이터 / 파일 로드

- **주식 거래**

  - 주식 매수 / 매도
  - 포트폴리오 자동 갱신

## 🚀 실행 방법

```bash
# 레포지토리 클론
git clone git@github.com:seonhyeongg/study.git
cd study/java-practice/skala-stock-console-practice

# 컴파일
javac -d bin src/*.java

# 실행
java -cp bin App
```

## 📂 프로젝트 구조

```bash
skala-stock-console-practice/
├── data/                              # 데이터 파일 디렉토리
│   ├── players.txt
│   └── stocks.txt
├── src/
│   ├── App.java
│   ├── FilePortfolioFormatter.java    # 포트폴리오를 파일 저장용 문자열로 변환하는 Formatter
│   ├── MenuPortfolioFormatter.java    # 포트폴리오를 콘솔 메뉴 형식으로 출력하는 Formatter
│   ├── Player.java                    # 플레이어 정보
│   ├── PlayerMapper.java              # Player <-> 문자열 변환
│   ├── PlayerRepository.java          # Player 데이터 저장/로드
│   ├── Portfolio.java                 # 플레이어 보유 주식 관리
│   ├── PortfolioFormatter.java        # 포트폴리오 출력 형식 정의 인터페이스
│   ├── SkalaStockMarket.java          # 전체 프로그램 실행 흐름 제어
│   ├── Stock.java                     # 주식 정보
│   ├── StockMapper.java               # Stock <-> 문자열 변환
│   ├── StockRepository.java           # 주식 데이터 저장/로드
│   ├── StockService.java              # 매수/매도 비즈니스 로직 처리
│   └── StockView.java                 # 콘솔 UI(View) - 사용자 입력/출력 처리
└── README.md
```
