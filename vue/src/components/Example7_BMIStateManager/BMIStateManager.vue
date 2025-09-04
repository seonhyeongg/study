<!--
[Component] BMIStateManager.vue
- 목적: 키/몸무게를 입력하면 BMI를 계산하고 상태를 표시한다. 자식 컴포넌트(WeightControl)에서 몸무게 증감 이벤트를 받아 반영한다.
- 주요 흐름:
  1) height/weight 입력 -> bmi(computed) 자동 갱신
  2) watch(bmi, immediate): BMI 구간에 따라 status 갱신
  3) <WeightControl @change="changeWeight">: 자식이 내보낸 증감값(amount)을 수신해 weight 갱신
- 분류 기준
  <18.5 저체중, 18.5~22.9 정상, 23.0~24.9 과체중, >=25 비만
-->

<script setup>
import { ref, computed, watch } from "vue";
import WeightControl from "./WeightControl.vue";

const height = ref(170);
const weight = ref(60);

/**
 * 계산: bmi
 * - height가 양수일 때만 계산. 그 외는 0으로 처리
 */
const bmi = computed(() =>
  height.value > 0 ? weight.value / (height.value / 100) ** 2 : 0
);

const status = ref("");

/**
 * 감시자: bmi
 * - 동작: bmi가 변할 때마다 상태 라벨을 갱신
 * - 분류 기준:
 *   - value === 0 -> '-' (미입력/잘못된 입력 가드)
 *   - <18.5 -> 'Underweight'
 *   - <23   -> 'Normal'
 *   - <25   -> 'Overweight'
 *   - else  -> 'Obese'
 * - immediate: true -> 마운트 시 1회 즉시 실행 (초기 status 설정)
 */
watch(
  bmi,
  (value) => {
    if (!value) {
      status.value = "-";
      return;
    }
    if (value < 18.5) {
      status.value = "Underweight";
    } else if (value < 23) {
      status.value = "Normal";
    } else if (value < 25) {
      status.value = "Overweight";
    } else {
      status.value = "Obese";
    }
  },
  { immediate: true }
);

/**
 * 계산: isOverweight
 * - 과체중 이상 (23 이상) 여부 플래그
 * - 경고 문구 노출 제어에 사용
 */
const isOverweight = computed(() => bmi.value >= 23);

/**
 * 함수: changeWeight
 * - 입력: amount(number) — 자식 컴포넌트가 emit하는 증감값
 * - 동작: weight를 amount만큼 증감
 */
function changeWeight(amount) {
  weight.value += amount;
}
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>BMI State</h1>
      <div class="controls">
        <input
          v-model.number="height"
          type="number"
          placeholder="Height (cm)"
          min="50"
          max="250"
          step="0.1"
        />
        <input
          v-model.number="weight"
          type="number"
          placeholder="Weight (kg)"
          min="10"
          max="300"
          step="0.1"
        />
      </div>
    </header>

    <main class="stage">
      <div class="card-common">
        <p>Height: {{ height }} cm</p>
        <p>Weight: {{ weight }} kg</p>
      </div>

      <WeightControl @change="changeWeight" />

      <div class="card-common">
        <p>BMI: {{ bmi.toFixed(1) }}</p>
        <p>Status: {{ status }}</p>
        <p v-show="isOverweight">Please go on a diet</p>
      </div>
    </main>
  </div>
</template>
