<!--
[Component] MouseTracker.vue
- 목적: 지정된 박스 영역 안에서 마우스 좌표를 추적하고, 밖으로 나가면 상태 메시지를 표시한다.
- 주요 기능:
  1) 박스 안에서 마우스를 움직이면 offset 좌표(X, Y)를 실시간 추적
  2) 박스 밖으로 나가면 좌표 대신 "Mouse is outside the box" 메시지 표시
- Vue 기능:
  - ref: 반응형 상태 관리 (isInside, posX, posY)
  - v-if / v-else: 상태에 따른 조건부 렌더링
  - 이벤트 바인딩: @mousemove, @mouseleave
-->

<script setup>
import { ref } from "vue";

const isInside = ref(false);
const posX = ref(0);
const posY = ref(0);

/**
 * 함수: handleMouseMove
 * - 이벤트: @mousemove
 * - 동작:
 *   1) isInside = true로 설정
 *   2) 이벤트 객체의 offsetX, offsetY 값을 posX, posY에 저장
 */
function handleMouseMove(event) {
  isInside.value = true;
  posX.value = event.offsetX;
  posY.value = event.offsetY;
}

/**
 * 함수: handleMouseLeave
 * - 이벤트: @mouseleave
 * - 동작: 마우스가 박스 밖으로 나가면 isInside를 false로 설정
 */
function handleMouseLeave() {
  isInside.value = false;
}
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Mouse Tracker</h1>
    </header>

    <main class="stage">
      <div
        class="track-box"
        @mousemove="handleMouseMove"
        @mouseleave="handleMouseLeave"
      ></div>
      <p class="coords">
        <span v-if="isInside">X: {{ posX }}, Y: {{ posY }}</span>
        <span v-else>Mouse is outside the box</span>
      </p>
    </main>
  </div>
</template>

<style scoped>
.track-box {
  width: 400px;
  height: 400px;
  background: #ffffff;
  border: 2px solid #333;
  border-radius: 8px;
}
</style>
