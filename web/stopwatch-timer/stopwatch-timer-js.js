const radios = document.querySelectorAll('input[name="mode"]');
const contents = document.querySelectorAll(".mode");

radios.forEach((radio) => {
  radio.addEventListener("change", () => {
    contents.forEach((div) => div.classList.remove("active"));
    const selected = document.getElementById(radio.value);
    if (selected) selected.classList.add("active");

    clearInterval(stopwatch);
    stopwatch = null;
    clearInterval(timer);
    timer = null;
  });
});

let stopwatch = null;
let stopwatchTime = 0;
const stopwatchDisplay = document.getElementById("stopwatch-display");
const stopwatchStart = document.getElementById("stopwatch-start");
const stopwatchStop = document.getElementById("stopwatch-stop");
const stopwatchReset = document.getElementById("stopwatch-reset");
const setTime = document.getElementById("set-time");

const inputHours = document.getElementById("input-hours");
const inputMinutes = document.getElementById("input-minutes");
const inputSeconds = document.getElementById("input-seconds");

function updateStopwatchDisplay() {
  const hours = String(Math.floor(stopwatchTime / 3600)).padStart(2, "0");
  const minutes = String(Math.floor((stopwatchTime % 3600) / 60)).padStart(2, "0");
  const seconds = String(stopwatchTime % 60).padStart(2, "0");

  stopwatchDisplay.textContent = `${hours}:${minutes}:${seconds}`;
}

setTime.addEventListener("click", () => {
  const hour = Number(inputHours.value) || 0;
  const min = Number(inputMinutes.value) || 0;
  const sec = Number(inputSeconds.value) || 0;
  stopwatchTime = hour * 3600 + min * 60 + sec;
  updateStopwatchDisplay();

  clearInterval(stopwatch);
  stopwatch = null;

  inputHours.value = "";
  inputMinutes.value = "";
  inputSeconds.value = "";
});

stopwatchStart.addEventListener("click", () => {
  if (stopwatch == null && stopwatchTime > 0) {
    stopwatch = setInterval(() => {
      if (stopwatchTime > 0) {
        stopwatchTime--;
        updateStopwatchDisplay();
      } else {
        clearInterval(stopwatch);
        stopwatch = null;
        alert("Time out");
      }
    }, 1000);
  }
});

stopwatchStop.addEventListener("click", () => {
  clearInterval(stopwatch);
  stopwatch = null;
});

stopwatchReset.addEventListener("click", () => {
  clearInterval(stopwatch);
  stopwatch = null;
  stopwatchTime = 0;
  updateStopwatchDisplay();
});

let timer = null;
let timerTime = 0;
const timerDisplay = document.getElementById("timer-display");
const timerStart = document.getElementById("timer-start");
const timerStop = document.getElementById("timer-stop");
const timerReset = document.getElementById("timer-reset");

function updateTimerDisplay() {
  const hours = String(Math.floor(timerTime / 3600)).padStart(2, "0");
  const minutes = String(Math.floor((timerTime % 3600) / 60)).padStart(2, "0");
  const seconds = String(timerTime % 60).padStart(2, "0");

  timerDisplay.textContent = `${hours}:${minutes}:${seconds}`;
}

timerStart.addEventListener("click", () => {
  if (timer == null) {
    timer = setInterval(() => {
      timerTime++;
      updateTimerDisplay();
    }, 1000);
  }
});

timerStop.addEventListener("click", () => {
  clearInterval(timer);
  timer = null;
});

timerReset.addEventListener("click", () => {
  clearInterval(timer);
  timer = null;
  timerTime = 0;
  updateTimerDisplay();
});
