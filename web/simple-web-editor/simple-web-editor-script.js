function execCmd(command, value = null) {
  document.execCommand(command, false, value);
}

document.getElementById("toolbar").addEventListener("click", function (e) {
  const command = e.target.id;

  if (command === "createLink") {
    const url = prompt("Enter the URL:");
    if (url) execCmd(command, url);
  } else {
    execCmd(command);
  }
});

document.getElementById("showResult").addEventListener("click", function () {
  const editor = document.getElementById("editor");
  const html = editor.innerHTML;
  document.getElementById("output").textContent = html;
});
