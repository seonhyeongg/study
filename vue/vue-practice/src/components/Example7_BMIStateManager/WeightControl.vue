<!--
[Component] WeightControl.vue
- 목적: 사용자가 식사/운동 버튼을 눌러 몸무게 증감을 선택할 수 있는 컨트롤을 제공한다.
- 동작 흐름:
  1) 버튼 클릭 -> changeWeight (amount) 실행
  2) changeWeight -> emit("change", amount) 호출
  3) 부모 컴포넌트(BMI 계산기 등)가 이 이벤트를 받아 weight 상태를 갱신
- 이벤트 계약:
  - emits("change", amount:number)
  - amount > 0 -> 체중 증가, amount < 0 -> 체중 감소
- 장점: 자식은 UI와 이벤트 발생만 담당, 상태 업데이트 로직은 부모가 관리
-->

<script setup>
/**
 * 이벤트 정의
 * - 'change': 부모에 체중 증감값(amount:number)을 전달하는 이벤트
 */
const emit = defineEmits(["change"]);

/**
 * 함수: changeWeight
 * - 입력: amount (number)
 * - 동작: 부모에게 'change' 이벤트를 발생시켜 전달
 */
function changeWeight(amount) {
  emit("change", amount);
}
</script>

<template>
  <div class="weight-control">
    <section>
      <h2>Eat Food</h2>
      <div class="controls">
        <button class="control-btn" @click="changeWeight(1)">
          Light Meal: +1kg
        </button>
        <button class="control-btn" @click="changeWeight(2)">
          Heavy Meal: +2kg
        </button>
      </div>
    </section>

    <section>
      <h2>Exercise</h2>
      <div class="controls">
        <button class="control-btn" @click="changeWeight(-1)">
          Light Exercise: -1kg
        </button>
        <button class="control-btn" @click="changeWeight(-2)">
          Intense Exercise: -2kg
        </button>
      </div>
    </section>
  </div>
</template>

<style scoped>
.weight-control {
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.weight-control h2 {
  font-size: 18px;
  text-align: left;
  margin-bottom: 8px;
}

.controls {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.control-btn {
  width: 200px;
  height: 40px;
  padding: 10px;
  text-align: center;
  border-radius: 6px;
  background: #007bff;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
}
</style>
