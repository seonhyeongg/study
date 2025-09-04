<!--
[Component] TabSwitchKeepAlive.vue
- 목적: 버튼 클릭으로 서로 다른 탭 (TabOne, TabTwo) 컴포넌트를 전환하면서,
        각 탭의 상태를 <KeepAlive>로 캐싱하여 유지한다.
- 주요 흐름:
  1) currentTab (ref)에 현재 표시할 컴포넌트 (TabOne/TabTwo)를 저장
  2) 버튼 클릭 시 selectTab(tab) 호출 -> currentTab 갱신
  3) <component :is="currentTab"> 로 동적 컴포넌트 렌더링
  4) <KeepAlive> 로 감싸 캐싱 -> 전환 시 컴포넌트 상태가 초기화되지 않음
- Vue 기능:
  - ref: 반응형 변수 (currentTab)
  - markRaw: 컴포넌트를 Proxy로 감싸지 않고 원본 유지 (성능/호환성 목적)
  - <component :is="...">: 동적 컴포넌트 렌더링
  - <KeepAlive>: 동적 컴포넌트 상태 캐싱
-->

<script setup>
import { ref, markRaw } from "vue";
import TabOne from "./TabOne.vue";
import TabTwo from "./TabTwo.vue";

const currentTab = ref(markRaw(TabOne));

/**
 * 함수: selectTab
 * - 입력: tab (컴포넌트, ex. TabOne/TabTwo)
 * - 동작: currentTab을 전달된 컴포넌트로 교체
 * - 결과: <component :is="currentTab"> 가 해당 컴포넌트를 렌더링
 */
const selectTab = (tab) => {
  currentTab.value = markRaw(tab);
};
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Tab Switch with KeepAlive</h1>
      <div class="controls">
        <button @click="selectTab(TabOne)">Tab 1</button>
        <button @click="selectTab(TabTwo)">Tab 2</button>
      </div>
    </header>

    <main class="stage">
      <div class="card-common">
        <KeepAlive>
          <component :is="currentTab" />
        </KeepAlive>
      </div>
    </main>
  </div>
</template>
