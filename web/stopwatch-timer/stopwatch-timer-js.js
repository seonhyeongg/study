// 모드 전환 (Stopwatch/Timer) 라디오 버튼 및 관련 영역 가져오기
const radios = document.querySelectorAll('input[name="mode"]');
const contents = document.querySelectorAll(".mode");

// 라디오 버튼 변경 시 실행
radios.forEach((radio) => {
  radio.addEventListener("change", () => {
    // 모든 모드에서 active 클래스 제거
    contents.forEach((div) => div.classList.remove("active"));
    // 선택된 모드에만 active 클래스 추가
    const selected = document.getElementById(radio.value);
    if (selected) selected.classList.add("active");

    // 모드 전환 시 스탑워치, 타이머 정지
    clearInterval(stopwatch);
    stopwatch = null;
    clearInterval(timer);
    timer = null;
  });
});

// ===== 스탑워치 ==========

let stopwatch = null; // setInterval 저장
let stopwatchTime = 0; // 남은 시간 (초 단위)
const stopwatchDisplay = document.getElementById("stopwatch-display");
const stopwatchStart = document.getElementById("stopwatch-start");
const stopwatchStop = document.getElementById("stopwatch-stop");
const stopwatchReset = document.getElementById("stopwatch-reset");
const setTime = document.getElementById("set-time");

// 시간 입력 필드
const inputHours = document.getElementById("input-hours");
const inputMinutes = document.getElementById("input-minutes");
const inputSeconds = document.getElementById("input-seconds");

// 스탑워치 화면 업데이트
function updateStopwatchDisplay() {
  const hours = String(Math.floor(stopwatchTime / 3600)).padStart(2, "0");
  const minutes = String(Math.floor((stopwatchTime % 3600) / 60)).padStart(
    2,
    "0"
  );
  const seconds = String(stopwatchTime % 60).padStart(2, "0");

  stopwatchDisplay.textContent = `${hours}:${minutes}:${seconds}`;
}

// Set Time 버튼: 입력값을 스탑워치 시간으로 설정
setTime.addEventListener("click", () => {
  const hour = Number(inputHours.value) || 0;
  const min = Number(inputMinutes.value) || 0;
  const sec = Number(inputSeconds.value) || 0;
  stopwatchTime = hour * 3600 + min * 60 + sec;
  updateStopwatchDisplay();

  // 기존 실행 중인 스탑워치 중지
  clearInterval(stopwatch);
  stopwatch = null;

  // 입력 필드 초기화
  inputHours.value = "";
  inputMinutes.value = "";
  inputSeconds.value = "";
});

// Start 버튼: 카운트다운 시작
stopwatchStart.addEventListener("click", () => {
  if (stopwatch == null && stopwatchTime > 0) {
    stopwatch = setInterval(() => {
      if (stopwatchTime > 0) {
        stopwatchTime--; // 1초 감소
        updateStopwatchDisplay();
      } else {
        // 시간 종료 시 중지
        clearInterval(stopwatch);
        stopwatch = null;
        alert("Time out"); // 알림 표시
      }
    }, 1000);
  }
});

// Stop 버튼: 일시 정지
stopwatchStop.addEventListener("click", () => {
  clearInterval(stopwatch);
  stopwatch = null;
});

// Reset 버튼: 시간 초기화
stopwatchReset.addEventListener("click", () => {
  clearInterval(stopwatch);
  stopwatch = null;
  stopwatchTime = 0;
  updateStopwatchDisplay();
});

// ===== 타이머 ==========

let timer = null; // setInterval 저장
let timerTime = 0; // 경과 시간 (초 단위)
const timerDisplay = document.getElementById("timer-display");
const timerStart = document.getElementById("timer-start");
const timerStop = document.getElementById("timer-stop");
const timerReset = document.getElementById("timer-reset");

// 타이머 화면 업데이트
function updateTimerDisplay() {
  const hours = String(Math.floor(timerTime / 3600)).padStart(2, "0");
  const minutes = String(Math.floor((timerTime % 3600) / 60)).padStart(2, "0");
  const seconds = String(timerTime % 60).padStart(2, "0");

  timerDisplay.textContent = `${hours}:${minutes}:${seconds}`;
}

// Start 버튼: 타이머 시작
timerStart.addEventListener("click", () => {
  if (timer == null) {
    timer = setInterval(() => {
      timerTime++;
      updateTimerDisplay();
    }, 1000);
  }
});

// Stop 버튼: 일시 정지
timerStop.addEventListener("click", () => {
  clearInterval(timer);
  timer = null;
});

// Reset 버튼: 시간 초기화
timerReset.addEventListener("click", () => {
  clearInterval(timer);
  timer = null;
  timerTime = 0;
  updateTimerDisplay();
});
