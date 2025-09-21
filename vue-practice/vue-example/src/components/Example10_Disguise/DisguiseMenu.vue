<!--
[Component] DisguiseMenu.vue
- 목적: 사용자가 변신할 대상을 선택하면 결과 페이지로 이동시킨다.
- 주요 흐름:
  1) 버튼 클릭 시 goToDisguise(target) 호출
  2) router.push로 /result 라우트로 이동하며 query에 target 전달
  3) 결과 페이지(DisguiseResult)에서 target 값을 읽어 표시
- Vue 기능:
  - useRouter(): 라우터 인스턴스를 가져와 프로그래매틱 네비게이션 수행
  - @click: 버튼 클릭 이벤트 바인딩
-->

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

/**
 * 함수: goToDisguise
 * - 입력: target(string) — 변신할 대상 이름
 * - 동작: /result 경로로 이동하면서 query에 target 전달
 *   예: goToDisguise("Tiger") -> /result?target=Tiger
 * - 부작용: 현재 페이지에서 결과 페이지로 전환됨
 */
function goToDisguise(target) {
  router.push({ path: "/result", query: { target } });
}
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Disguise Selection Menu</h1>
      <div class="controls">
        <button @click="goToDisguise('Tiger')">Transform into a Tiger</button>
        <button @click="goToDisguise('Goblin')">Transform into a Goblin</button>
      </div>
    </header>

    <main class="stage">
      <div class="card-common">
        <p>Select a disguise and proceed.</p>
      </div>
    </main>
  </div>
</template>
