<!--
[Component] XssExposuerWithVHtml.vue
- 목적: 사용자가 입력한 메시지를 v-html로 출력한다.
- 위험 요소: v-html 사용 시 입력 값이 그대로 HTML로 렌더링되어 XSS 취약점 발생 가능
- 예시 공격: <img src="x" onerror="window.location.href='https://google.com'"> 입력 시 구글로 강제 리다이렉트된다.
-->

<script setup>
import { ref } from "vue";

const inputMessage = ref("");
const outputMessage = ref("");

/**
 * 함수: updateMessage
 * - 동작: 현재 inputMessage 값을 outputMessage로 복사
 * - 부작용: v-html을 통해 DOM에 그대로 삽입 -> 악성 스크립트 실행 가능
 */
function updateMessage() {
  outputMessage.value = inputMessage.value;
}
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>XSS Exposure with v-html</h1>
      <div class="controls">
        <input v-model="inputMessage" placeholder="Enter a message" />
        <button @click="updateMessage">Confirm</button>
      </div>
    </header>

    <main class="stage">
      <div class="output" v-html="outputMessage"></div>
    </main>
  </div>
</template>
