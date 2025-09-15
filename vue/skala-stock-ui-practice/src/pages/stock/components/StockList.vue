<!--
[Component] StockList.vue
- 목적: 주식 목록 조회, 주식명/가격 기반 신규 종목 추가 또는 기존 종목 수정
- 주요 기능
  1) onMounted 시점에 주식 목록 (getStockList) 조회
  2) 갱신 버튼 (@click): getStockList 재호출로 현재 페이지 데이터 새로고침
  3) 주식 종목 추가/변경 (addOrUpdateStock)
     - 입력값 검증
     - 현재 table.items에서 stockName 일치 항목 탐색
       1. 존재: /api/stocks PUT 호출 -> 해당 종목 업데이트
       2. 부재: /api/stocks PPST 호출 -> 신규 종목 등록
     - 성공 시 입력값 초기화 및 getStockList()로 목록 갱신
-->
<script setup>
import { ref, reactive, watch, onMounted } from "vue";
import apiCall from "@/scripts/api-call";
import { notifyInfo } from "@/scripts/store-popups";

// ===== 상태 ==========
/** 주식명 입력값 */
const stockName = ref("");
/** 주식 가격 입력값 */
const stockPrice = ref("");
/** 주식 테이블*/
const table = reactive({
  headers: [
    { label: "주식ID", value: "id" },
    { label: "주식명", value: "stockName" },
    { label: "주식가격", value: "stockPrice" },
  ],
  items: [],
});
/** 페이지네이션 (total: 전체 건수, current: 현재 페이지, count: 페이지당 건수) */
const page = reactive({
  total: 0,
  current: 1,
  count: 10,
});

/**
 * 함수: getStockList
 * - 이벤트: onMounted 최초 호출, 갱신 버튼 (@click), page.current/count watch
 * - 동작
 *   1) /api/stocks/list GET 호출
 *   2) 성공:
 *      - total/offset/list 수신
 *      - page.total/current/items 갱신
 *      실패:
 *      - 안내 팝업 노출
 */
const getStockList = async () => {
  const url = "/api/stocks/list";

  const queryParams = {
    total: page.total,
    count: page.count,
    offset: page.current - 1,
  };

  const response = await apiCall.get(url, null, queryParams);

  if (response.result === apiCall.Response.SUCCESS) {
    const pagedList = response.body;

    page.total = pagedList.total;
    page.current = pagedList.offset + 1;

    table.items = pagedList.list;
  } else {
    notifyInfo("주식 조회에 실패했습니다.");
  }
};

/** 함수: addOrUpdateStock
 * - 이벤트: 주식 추가/변경 버튼 (@click)
 * - 입력: stockName, stockPrice
 * - 동작
 *   1) 입력값 유효성 검사
 *   2) table.itmes에서 stockName 일치 항목 검색
 *      - 존재: /api/stocks PUT 호출 -> 수정
 *      - 부재: /api/stocks POST 호출 -> 신규 추가
 *   3) 성공:
 *      - 입력값 초기화
 *      - getStockList() 호출 -> 목록 갱신
 *      실패:
 *      - 안내 팝업 노출
 */
const addOrUpdateStock = async () => {
  if (stockName.value === "" || stockPrice.value === "") {
    notifyInfo("주식명 또는 주식가격을 입력하세요.");
    return;
  }

  const existing = table.items.find(
    (item) => item.stockName === stockName.value
  );

  const url = "/api/stocks";

  if (existing) {
    const requestBody = {
      id: existing.id,
      stockName: stockName.value,
      stockPrice: stockPrice.value,
    };

    const response = await apiCall.put(url, null, requestBody);

    if (response.result === apiCall.Response.SUCCESS) {
      stockName.value = "";
      stockPrice.value = "";

      await getStockList();
    } else {
      notifyInfo("주식 수정에 실패했습니다.");
    }
  } else {
    const requestBody = {
      stockName: stockName.value,
      stockPrice: stockPrice.value,
    };

    const response = await apiCall.post(url, null, requestBody);

    if (response.result === apiCall.Response.SUCCESS) {
      stockName.value = "";
      stockPrice.value = "";

      await getStockList();
    } else {
      notifyInfo("주식 추가에 실패했습니다.");
    }
  }
};

/** 현재 페이지 변경 시 목록 재조회 */
watch(
  () => page.current,
  () => {
    getStockList();
  }
);
/** 페이지당 건수 변경 시 첫 페이지로 이동 후 목록 재조회 */
watch(
  () => page.count,
  () => {
    page.current = 1;
    getStockList();
  }
);

// ===== 초기 로딩 ==========
onMounted(() => {
  getStockList();
});
</script>

<template>
  <div class="row mt-2">
    <span class="fs-4"><i class="bi bi-graph-up m-2"></i>주식목록</span>
  </div>
  <div class="row border-bottom">
    <div class="col d-flex justify-content-end">
      <button class="btn btn-sm btn-primary m-1" @click="getStockList">
        <i class="bi bi-arrow-counterclockwise m-2"></i>갱신
      </button>
    </div>
  </div>
  <div class="row g-2 align-items-center m-2 mt-0">
    <div class="col">
      <ItemsTable
        :nosetting="true"
        :headers="table.headers"
        :items="table.items"
      />
      <PageNavigator
        v-model:current="page.current"
        v-model:count="page.count"
        :totalCount="page.total"
      />
    </div>
  </div>
  <div class="row g-2 m-2 border-top">
    <div class="col-2 d-flex justify-content-end">
      <label class="col-form-label form-control-sm p-1">주식정보</label>
    </div>
    <div class="col">
      <InlineInput placeholder="주식명" v-model.lazy.trim="stockName" />
    </div>
    <div class="col">
      <InlineInput placeholder="주식가격" v-model.lazy.trim="stockPrice" />
    </div>
    <div class="col d-flex justify-content-start">
      <button
        class="btn btn-sm btn-outline-primary me-2"
        @click="addOrUpdateStock"
      >
        주식 추가/변경
      </button>
    </div>
  </div>
</template>
