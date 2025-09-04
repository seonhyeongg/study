<!--
[Component] BMICalculator.vue
- 목적: 키 (cm)와 체중 (kg)을 입력받아 BMI를 계산하고, 상태 (저체중/정상/과체중/비만)를 표시한다.
- 계산식: BMI = weight (kg) / (height (m))^2 = weight / (height (cm)/100)^2
- 분류 기준:
  <18.5 저체중, 18.5~22.9 정상, 23.0~24.9 과체중, >=25 비만
-->

<script setup>
import { ref, computed, watch } from "vue";

const height = ref(170);
const weight = ref(60);

/**
 * 계산: bmi
 * - height가 양수일 때만 BMI 계산, 그 외(0/NaN/음수)는 0 처리
 * - 소수점 자리수 표시는 템플릿에서 toFixed(2)로 처리
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
 * - immediate: true -> 마운트 시 즉시 1회 실행 (초기 status 세팅)
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
</script>

<template>
  <div class="page">
    <header class="topbar">
      <h1>BMI Calculator</h1>
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
        <p>BMI: {{ bmi.toFixed(2) }}</p>
        <p>Status: {{ status }}</p>
        <p v-show="isOverweight">Please go on a diet</p>
      </div>
    </main>
  </div>
</template>
