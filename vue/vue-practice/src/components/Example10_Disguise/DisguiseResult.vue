<!--
[Component] DisguiseResult.vue
- 목적: 쿼리 파라미터로 전달된 변신 대상 (target)을 보여주고, "메뉴로 돌아가기" 기능을 제공한다.
- 주요 흐름:
  1) DisguiseMenu에서 router.push({ path:'/result', query:{ target } }) 호출
  2) 이 컴포넌트가 마운트되면 route.query.target 값을 읽어 target에 저장
  3) target이 존재하면 화면에 표시
  4) "Back to Menu" 버튼 → /menu 라우트로 이동
- Vue 기능:
  - useRoute: 현재 라우트 정보 접근
  - useRouter: 프로그래밍적 페이지 이동 수행
  - ref: 반응형 상태 (target) 저장
  - onMounted: 마운트 시점에 초기 처리 실행
-->

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const target = ref(route.query.target ?? "");

/**
 * 라이프사이클: onMounted
 * - 동작: target이 존재할 경우 router.replace 호출
 *   -> query를 제거하고 동일 path (/result)로 교체
 * - 이유: URL에서 target 쿼리를 숨겨, 깔끔한 주소 유지
 *   (하지만 target 상태는 ref에 보존되어 화면 출력에는 영향 없음)
 */
onMounted(() => {
  if (target.value) {
    router.replace({ path: route.path });
  }
});

/**
 * 함수: goBack
 * - 동작: /menu 라우트로 이동
 * - 사용: "Back to Menu" 버튼 클릭 시 호출
 */
function goBack() {
  router.push({ path: "/menu" });
}
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>Disguise Activated!</h1>
    </header>

    <main class="stage">
      <div class="card-common">
        <p>
          Hong Gil-dong has transformed into a
          <strong>{{ target }}</strong
          >!
        </p>
        <button @click="goBack">Back to Menu</button>
      </div>
    </main>
  </div>
</template>
