# Vue Exapmles

Vue 3 학습 및 연습을 위한 예제 모음집

각 컴포넌트는 독립적인 기능을 보여주며, `src/components/` 폴더에 정리되어 있습니다.

## 🚀 실행 방법

```bash
# 레포지토리 클론
git clone git@github.com:seonhyeongg/study.git
cd study/vue

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev
```

브라우저에서 http://localhost:5173 에 접속 후 예제를 확인하세요.

## 📂 프로젝트 구조

```bash
vue/
├── public/                 # 정적 자원
├── src/
│   ├── assets/             # 이미지, 아이콘 등 정적 파일
│   │   ├── common.css      # 추가 전역 스타일
│   ├── components/         # Vue 예제 모음
│   │   ├── Example1_DisplayInput/        # 입력값 출력 예제
│   │   ├── Example2_XXSExposureWithVHtml # v-html XSS 취약점 예제
│   │   ├── Example3_DynamicStyleBox/     # 동적 스타일 박스
│   │   ├── Example4_CommentForm/         # 댓글 작성 폼
│   │   ├── Example5_MouseTracker/        # 마우스 좌표 추적
│   │   ├── Example6_BMICalculator/       # BMI 계산기
│   │   ├── Example7_BMIStateManager/     # BMI 상태 관리
│   │   ├── Example8_ShowInputBox/        # 입력창 토글 & focus
│   │   ├── Example9_TabSwitchKeepAlive/  # 탭 전환 + 상태 유지
│   │   ├── Example10_Disguise/           # 라우팅으로 둔갑술 시뮬레이션
│   │   └── EmptyPage.vue                 # 빈 페이지 템플릿
│   │
│   ├── App.vue             # 루트 컴포넌트
│   ├── main.js             # 진입 스크립트
│   ├── router.js           # 라우터 설정
│   └── style.css           # 전역 스타일
│
├── index.html              # 진입점 HTML
├── package.json            # 프로젝트 설정 및 의존성
├── package-lock.json       # 의존성 버전 고정
├── vite.config.js          # Vite 설정
└── README.md               # 프로젝트 문서
```

## 📘 예제 목록

| 예제 번호 | 파일/폴더                       | 설명                                                       |
| --------- | ------------------------------- | ---------------------------------------------------------- |
| 1         | `Example1_DisplayInput`         | 입력값을 입력창에 입력하면 실시간 출력                     |
| 2         | `Example2_XXSExposureWithVHtml` | `v-html` 사용 시 발생할 수 있는 XSS 취약점 데모            |
| 3         | `Example3_DynamicStyleBox`      | 색상/크기를 입력하여 박스 스타일을 동적으로 변경           |
| 4         | `Example4_CommentForm`          | 댓글 입력 및 리스트 출력                                   |
| 5         | `Example5_MouseTracker`         | 마우스 위치 추적 후 좌표 출력                              |
| 6         | `Example6_BMICalculator`        | 키와 몸무게 입력 후 BMI 계산 및 상태 출력                  |
| 7         | `Example7_BMIStateManager`      | BMI 상태 관리 + WeightControl 자식 컴포넌트 이벤트 처리    |
| 8         | `Example8_ShowInputBox`         | 버튼 클릭 시 입력창 토글 + 표시 시 자동 포커스             |
| 9         | `Example9_TabSwitchKeepAlive`   | `<KeepAlive>`를 사용한 탭 전환 상태 유지                   |
| 10        | `Example10_Disguise`            | Vue Router를 사용한 둔갑술 예제 (메뉴 -> 결과 페이지 이동) |
