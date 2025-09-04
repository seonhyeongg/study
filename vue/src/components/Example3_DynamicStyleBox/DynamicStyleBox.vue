<!--
[Component] DynamicStyleBox.vue
- 목적: 사용자가 입력한 색상과 크기 값으로 박스 스타일을 동적으로 변경한다.
- 주요 기능:
  1) 색상 (color) 입력 -> 박스 배경색 변경
  2) 크기 (size) 입력 -> 박스 가로/세로 크기 변경
- Vue 기능:
  - reactive: 객체 형태의 반응형 상태 (boxStyle)
  - ref: 단일 값의 반응형 상태 (colorInput, sizeInput)
  - v-model.trim / v-model.number: 입력값 자동 가공 (공백 제거, 숫자 변환)
- 주의 사항:
  - size 값이 0 이하이거나 NaN이면 업데이트 무시
  - style 바인딩은 인라인 CSS 형태로 직접 적용
-->

<script setup>
import { reactive, ref } from "vue";

const boxStyle = reactive({
  backgroundColor: "white",
  width: "150px",
  height: "150px",
  border: "2px solid #333",
  borderRadius: "10px",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  fontWeight: "bold",
  color: "#333",
});

const colorInput = ref("");
const sizeInput = ref("");

/**
 * 함수: updateBoxStyle
 * - 입력:
 *   - color (string): 적용할 색상
 *   - size (number|string): 적용할 크기 (px 단위)
 * - 동작:
 *   1) size를 숫자로 변환 후, 유효한 양수일 경우 width/height에 반영
 *   2) color 값이 비어있지 않으면 backgroundColor에 반영
 */
function updateBoxStyle(color, size) {
  const n = Number(size);
  if (!Number.isNaN(n) && n > 0) {
    boxStyle.width = n + "px";
    boxStyle.height = n + "px";
  }
  if (color) {
    boxStyle.backgroundColor = color;
  }
}
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Dynamic Style Box</h1>
      <div class="controls">
        <input v-model.trim="colorInput" placeholder="Enter color" />
        <input v-model.number="sizeInput" placeholder="Enter size" />
        <button @click="updateBoxStyle(colorInput, sizeInput)">Apply</button>
      </div>
    </header>

    <main class="stage">
      <div class="box" :style="boxStyle">Style Box</div>
    </main>
  </div>
</template>

<style scoped>
.box {
  max-width: min(90vw, 900px);
  max-height: min(70vh, 900px);
}
</style>
