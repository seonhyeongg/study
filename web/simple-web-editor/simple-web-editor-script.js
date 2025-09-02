// 명령 실행 함수
// command: 실행할 명령어 (ex. bold, italic, createLink, etc.)
// value: 명령어에 필요한 값(ex. createLink의 경우 URL)
function execCmd(command, value = null) {
  document.execCommand(command, false, value);
}

// toolbar 클릭 이벤트 리스너
document.getElementById("toolbar").addEventListener("click", function (e) {
  const command = e.target.id; //클릭된 버튼의 id를 명령어로 사용

  if (command === "createLink") {
    // createLink 버튼 클릭 시 URL 입력 요청
    const url = prompt("Enter the URL:");
    if (url) execCmd(command, url); // 입력된 URL로 링크 생성
  } else {
    // 그 외 명령어 실행
    execCmd(command);
  }
});

// 결과 보기 버튼 클릭 이벤트 리스너
document.getElementById("showResult").addEventListener("click", function () {
  const editor = document.getElementById("editor"); // 편집 영역 가져오기
  const html = editor.innerHTML; // 편집기 내무 HTML 가져오기
  document.getElementById("output").textContent = html; // 결과 영역에 출력
});
