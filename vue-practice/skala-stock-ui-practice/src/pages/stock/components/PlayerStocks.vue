<!--
[Component] PlayerStocks.vue
- 목적: 플레이어 보유 주식 조회, 매수/매도, 자산 현황 관리 화면
- 주요 기능
  1) onMounted 시점에 전체 주식 목록 (getStockList)과 플레이어 정보(getPlayerInfo) 조회
  2) 플레이어 정보
     - playerId, playerMoney, 보유 주식 (table.items) 표시
  3) 주식 거래
     - 구매 (buyPlayerStock): 선택한 주식ID와 수량 POST -> 성공 시 playerMoney 차감 및 보유 목록 갱신
     - 판매 (sellPlayerStock): 선택한 주식ID와 수량 POST -> 성공 시 playerMoney 증가 및 보유 목록 갱신
-->
<script setup>
import { ref, reactive, onMounted } from "vue";
import { usePlayer } from "@/scripts/store-player";
import apiCall from "@/scripts/api-call";
import { notifyInfo } from "@/scripts/store-popups";

// ===== 상태 ==========
/** 전체 주식 목록 */
const allStocks = ref([]);
/** 주식ID 입력값 */
const stockId = ref("");
/** 주식 수량 입력값 */
const stockQuantity = ref("");
/** 보유 주식 테이블 */
const table = reactive({
  headers: [
    { label: "주식ID", value: "stockId" },
    { label: "주식명", value: "stockName" },
    { label: "주식가격", value: "stockPrice" },
    { label: "보유수량", value: "quantity" },
  ],
  items: [],
});
/** 현재 로그인 플레이어 정보 */
const player = usePlayer();

/**
 * 함수: getStockList
 * - 이벤트: onMounted 최초 호출
 * - 동작: /api/stocks/list GET 호출 -> 전체 주식 목록 로딩
 */
const getStockList = async () => {
  const url = "/api/stocks/list";

  const response = await apiCall.get(url, null, null);

  if (response.result === apiCall.Response.SUCCESS) {
    allStocks.value = Array.isArray(response.body.list)
      ? response.body.list
      : [];
  } else {
    notifyInfo("주식 조회에 실패했습니다.");
  }
};

/**
 * 함수: getPlayereInfo
 * - 이벤트: onMounted 최초 호출, 갱신 버튼 (@click), 매수/매도 성공 후 호출
 * - 동작: /api/players/:id GET 호출 -> 플레이어 자산, 보유 주식 목록 로딩
 */
const getPlayerInfo = async () => {
  const url = `/api/players/${player.playerId}`;

  const response = await apiCall.get(url, null, null);

  if (response.result === apiCall.Response.SUCCESS) {
    const playerMoney = response.body.playerMoney;

    table.items = response.body.stocks;
  } else {
    notifyInfo("플레이어 조회에 실패했습니다.");
  }
};

/**
 * 함수: buyPlayerStock
 * - 이벤트: 주식 구매 버튼 (@click)
 * - 입력: stockId, stockQuantity
 * - 동작
 *   1) 입력값 유효성 검사
 *   2) /api/players/buy POST 호출
 *   3) 성공:
 *      - 구매한 주식 가격만큼 player.playerMoney 차감
 *      - 입력값 초기화
 *      - getPlayerInfo 재호출 -> 보유 목록 갱신
 *      실패:
 *      - 안내 팝업 노출
 */
const buyPlayerStock = async () => {
  if (stockId.value === "" || stockQuantity.value === "") {
    notifyInfo("주식ID 또는 주식가격을 입력하세요.");
    return;
  }

  const url = "/api/players/buy";

  const requestBody = {
    playerId: player.playerId,
    stockId: stockId.value,
    stockQuantity: stockQuantity.value,
  };

  const response = await apiCall.post(url, null, requestBody);

  if (response.result === apiCall.Response.SUCCESS) {
    const stockInfo = allStocks.value.find(
      (s) => String(s.id) === stockId.value
    );

    const totalPrice = stockInfo.stockPrice * stockQuantity.value;
    player.playerMoney = player.playerMoney - totalPrice;

    stockId.value = "";
    stockQuantity.value = "";

    await getPlayerInfo();
  } else {
    notifyInfo("주식 구매에 실패했습니다.");
  }
};

/**
 * 함수: sellPlayerStock
 * - 이벤트: 주식 판매 버튼 (@click)
 * - 입력: stockId, stockQuantity
 * - 동작
 *   1) 입력값 유효성 검사
 *   2) /api/players/sell POST 호출
 *   3) 성공:
 *      - 판매한 주식 가격만큼 player.playerMoney 증가
 *      - 입력값 초기화
 *      - getPlayerInfo 재호출 -> 보유 목록 갱신
 *      실패:
 *      - 안내 팝업 노출
 */
const sellPlayerStock = async () => {
  if (stockId.value === "" || stockQuantity.value === "") {
    notifyInfo("주식ID 또는 주식가격을 입력하세요.");
    return;
  }

  const url = "/api/players/sell";

  const requestBody = {
    playerId: player.playerId,
    stockId: stockId.value,
    stockQuantity: stockQuantity.value,
  };

  const response = await apiCall.post(url, null, requestBody);

  if (response.result === apiCall.Response.SUCCESS) {
    const stockInfo = allStocks.value.find(
      (s) => String(s.id) === stockId.value
    );

    const totalPrice = stockInfo.stockPrice * stockQuantity.value;
    player.playerMoney = player.playerMoney + totalPrice;

    stockId.value = "";
    stockQuantity.value = "";

    await getPlayerInfo();
  } else {
    notifyInfo("주식 판매에 실패했습니다.");
  }
};

// ===== 초기 로딩 ==========
onMounted(async () => {
  await getStockList();
  await getPlayerInfo();
});
</script>

<template>
  <div class="row mt-2">
    <span class="fs-4"
      ><i class="bi bi-person m-2"></i>{{ player.playerId }} 플레이어</span
    >
  </div>
  <div class="row border-bottom">
    <div class="col d-flex justify-content-end">
      <button class="btn btn-sm btn-primary m-1" @click="getPlayerInfo">
        <i class="bi bi-arrow-counterclockwise m-2"></i>갱신
      </button>
    </div>
  </div>
  <div class="row">
    <div class="col">
      <InlineInput
        class="m-2"
        label="플레이어ID"
        :disabled="true"
        v-model="player.playerId"
      />
      <InlineInput
        class="m-2"
        label="보유금액"
        :disabled="true"
        v-model="player.playerMoney"
      />
    </div>
  </div>
  <div class="row g-2 align-items-center m-2 mt-0">
    <div class="col-2 d-flex justify-content-end">
      <label class="col-form-label form-control-sm p-1">보유주식목록</label>
    </div>
    <div class="col">
      <ItemsTable
        :nosetting="true"
        :headers="table.headers"
        :items="table.items"
      />
    </div>
  </div>
  <div class="row g-2 align-items-center m-2 mt-0">
    <div class="col-2 d-flex justify-content-end">
      <label class="col-form-label form-control-sm p-1">주식선택</label>
    </div>
    <div class="col">
      <InlineInput placeholder="주식ID" v-model.lazy.trim="stockId" />
    </div>
    <div class="col">
      <InlineInput
        placeholder="주식수량"
        v-model.lazy.trim.number="stockQuantity"
      />
    </div>
    <div class="col d-flex justify-content-start">
      <button
        class="btn btn-sm btn-outline-primary m-1"
        @click="buyPlayerStock"
      >
        주식 구매
      </button>
      <button
        class="btn btn-sm btn-outline-primary m-1"
        @click="sellPlayerStock"
      >
        주식 판매
      </button>
    </div>
  </div>
</template>
