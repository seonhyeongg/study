# SKALA STOCK UI Practice

Vue 3 기반 주식 거래 UI 연습 프로젝트

플레이어 로그인/회원가입, 보유 주식 조회, 주식 매수/매도, 주식 목록 관리 등의 기능을 제공합니다.

> 📢 이 프로젝트는 [skala-stock-ui](https://github.com/lsmin625/skala-stock-ui) 레포지토리의 practice 브랜치를 기반으로 수정하였습니다.

## 📌 주요 기능

- **플레이어**

  - 로그인 / 회원가입
  - 보유 자산 및 주식 현황 확인

- **주식 관리**

  - 전체 주식 리스트 조회
  - 주식 추가 / 수정

- **주식 거래**

  - 보유 주식 테이블 표시
  - 매수 / 매도

## 🚀 실행 방법

```bash
# 레포지토리 클론
git clone git@github.com:seonhyeongg/study.git
cd study/vue-practice/skala-stock-ui-practice

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev
```

브라우저에서 http://localhost:5173 에 접속하세요.

## 📂 프로젝트 구조

```bash
skala-stock-ui-practice/
├── public/                        # 정적 자원
├── src/
│   ├── App.vue
│   ├── main.js
│   ├── router.js
│   ├── components/                # 공용 UI 컴포넌트
│   ├── scripts/                   # 공통 로직
│   ├── pages/
│   │   ├── start/
│   │   │   └── StartMain.vue      # 로그인/회원가입 페이지
│   │   └── stock/
│   │       ├── StockMain.vue      # 주식 메인 페이지
│   │       └── components/
│   │           ├── PlayerStocks.vue   # 플레이어 보유 주식 관리 (조회/매수/매도)
│   │           └── StockList.vue      # 주식 목록 관리 (추가/변경, 페이징)
├── index.html
├── package.json
├── vite.config.js
└── README.md
```
