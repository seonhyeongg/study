<!--
[Component] DisplayInput.vue
- 목적: 사용자가 입력한 텍스트를 실시간으로 화면에 출력한다.
- 주요 동작: input에 입력 -> message (ref)에 바인딩 -> 화면에 즉시 반영
- Vue 기능: ref (반응형 상태), v-model (양방향 데이터 바인딩)
-->

<script setup>
import { ref } from "vue";

const message = ref("");
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Message Demo</h1>
      <div class="controls">
        <input v-model="message" placeholder="Enter a message" />
      </div>
    </header>

    <main class="stage">
      <p class="output">Message:<br />{{ message }}</p>
    </main>
  </div>
</template>
