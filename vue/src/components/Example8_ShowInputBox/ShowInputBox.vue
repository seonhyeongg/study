<!--
[Component] ShowInputBox.vue
- 목적: 버튼 클릭으로 입력창을 토글 표시하고, 표시될 때 자동으로 focus 처리한다.
- 주요 흐름:
  1) 버튼 클릭 -> isVisible 토글
  2) isVisible이 true가 되면 watch 훅 실행
     -> nextTick() 후 inputRef.focus() 실행 (DOM이 렌더링된 후 포커스 보장)
  3) 사용자가 입력한 메시지를 message (ref)에 저장, 입력이 있으면 바로 아래에 출력
- Vue 기능:
  - ref: 반응형 단일 값 상태 관리 (isVisible, inputRef, message)
  - watch: isVisible의 변화를 감시하여 부가 동작 수행
  - nextTick: DOM 업데이트가 반영된 다음 시점에 콜백 실행 (focus 보장)
  - v-show: DOM 유지하면서 표시 여부만 토글
-->

<script setup>
import { nextTick, ref, watch } from "vue";

const isVisible = ref(false);
const inputRef = ref(null);
const message = ref("");

/**
 * 함수: toggleInputBox
 * - 동작: isVisible 값을 true/false로 토글
 * - 버튼 클릭 시 실행
 */
function toggleInputBox() {
  isVisible.value = !isVisible.value;
}

/**
 * 감시자: isVisible
 * - 동작: isVisible이 true로 변경될 때 inputRef에 focus()
 * - ? 연산자 사용: inputRef.value가 null일 수 있으므로 안전하게 접근
 */
watch(isVisible, async (value) => {
  if (value) {
    await nextTick();
    inputRef.value?.focus();
  }
});
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Input Box Toggle</h1>
      <div class="controls">
        <button @click="toggleInputBox">
          {{ isVisible ? "Hide" : "Show" }} Input
        </button>
      </div>
    </header>

    <main class="stage">
      <div v-show="isVisible" class="card-common">
        <input ref="inputRef" v-model="message" placeholder="Enter a message" />
        <p v-show="message">Message:<br />{{ message }}</p>
      </div>
    </main>
  </div>
</template>
