<!--
[Component] StartMain.vue
- 목적: 플레이어가 로그인하거나, 로그인 실패 시 회원가입을 진행할 수 있는 인증 화면
- 주요 기능
  1) 입력 바인딩: v-model로 playerId/playerPassword 입력값을 상태에 저장
  2) 로그인 시도
     - 유효성 검사 (ID/Password 공백 체크) -> 실패 시 안내
     - /api/players/login POST 호출 -> 성공 시 storePlayer로 전역 상태 저장 후 /stock 라우팅
     - 실패 시 isNewPlayer=true로 전환하여 회원가입 버튼 표시 및 안내
  3) 회원가입 시도
     - /api/players POST 호출 -> 신규 플레이어 생성 (초기 playerMoney=0)
     - 성공 시 isNewPlayer=false로 되돌리고 login() 재호출 -> 자동 로그인
     - 실패 시 안내
-->

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import apiCall from "@/scripts/api-call";
import { storePlayer } from "@/scripts/store-player";
import { notifyInfo } from "@/scripts/store-popups";

const router = useRouter();

// ===== 상태 ==========
/** 플레이어 ID 입력값 */
const playerId = ref("");
/** 플레이어 비밀번호 입력값 */
const playerPassword = ref("");
/** 플레이어 보유 자금 입력값 (초기값만 선언) */
const playerMoney = ref("");
/** 회원가입 모드 토글: 로그인 실패 시 true로 전환하여 회원가입 버튼 노출 */
const isNewPlayer = ref(false);

/**
 * 함수: login
 * - 이벤트: 로그인 버튼 클릭 (@click)
 * - 입력: playerId, playerPassword
 * - 동작
 *   1) 입력값 유효성 검사
 *   2) /api/players/login POST 호출
 *   3) 성공:
 *      - storePlayer로 전역 상태 저장
 *      - router.push("/stock")로 주식 화면 이동
 *      실패:
 *      - isNewPlayer.value=true
 *      - 안내 팝업 노출
 */
const login = async () => {
  if (playerId.value === "" || playerPassword.value === "") {
    notifyInfo("ID 또는 비밀번호를 입력하세요.");
    return;
  }

  const url = "/api/players/login";

  const requestBody = {
    playerId: playerId.value,
    playerPassword: playerPassword.value,
  };

  const response = await apiCall.post(url, null, requestBody);

  if (response.result === apiCall.Response.SUCCESS) {
    storePlayer(response.body);
    router.push("/stock");
  } else {
    isNewPlayer.value = true;
    notifyInfo("로그인에 실패했습니다. 회원가입을 진행해주세요.");
  }
};

/** 함수: signup
 * - 이벤트: 회원가입 버튼 클릭 (@click)
 * - 입력: playerId, playerPassword
 * - 동작
 *   1) /api/players POST 호출 -> 신규 계정 생성 (초기 보유 자금 0)
 *   2) 성공:
 *      - isNewPlayer.value=false
 *      - login() 재호출 -> 생성한 계정으로 로그인 시도
 *      실패:
 *      - 안내 팝업 노출
 */
const signup = async () => {
  const url = "/api/players";

  const requestBody = {
    playerId: playerId.value,
    playerPassword: playerPassword.value,
    playerMoney: 0,
  };

  const response = await apiCall.post(url, null, requestBody);

  if (response.result === apiCall.Response.SUCCESS) {
    isNewPlayer.value = false;
    login();
  } else {
    notifyInfo("회원가입에 실패했습니다. 다시 시도해주세요.");
  }
};
</script>

<template>
  <div class="container-sm mt-3 border border-2 p-1" style="max-width: 600px">
    <div class="bss-background p-1">
      <div class="mt-3 d-flex justify-content-center" style="height: 230px">
        <span class="text-center text-danger fs-1 fw-bold mt-4"
          >SKALA STOCK Market</span
        >
      </div>
      <div class="row bg-info-subtle p-2 m-1" style="opacity: 95%">
        <div class="col">
          <InlineInput
            label="플레이어ID"
            class="mb-1"
            type="text"
            placeholder="플레이어ID"
            v-model.lazy.trim="playerId"
          />
          <InlineInput
            label="비밀번호"
            class="mb-1"
            type="password"
            placeholder="비밀번호"
            v-model.lazy.trim="playerPassword"
          />
        </div>
        <div class="d-flex justify-content-end">
          <button
            class="btn btn-primary btn-sm"
            v-if="isNewPlayer"
            @click="signup"
          >
            회원가입
          </button>
          <button class="btn btn-primary btn-sm" v-else @click="login">
            로그인
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.bss-background {
  width: 590px;
  height: 380px;
  background-image: url("/logo.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}
</style>
