import streamlit as st

from core.config import (
    APP_TITLE,
    APP_ICON,
    APP_LAYOUT,
    sidebar_server_settings,
    get_base_url,
)
from core.state import ensure_session_state, flags, set_flag
from core.http import api_call
from core.ui import show_status, debug_url
from core.table import render_table_exact, rows_from_body_list


# 페이지 기본 설정
st.set_page_config(page_title=APP_TITLE, page_icon=APP_ICON, layout=APP_LAYOUT)

# 세션 상태 초기화
ensure_session_state()

with st.sidebar:
    st.header("Server Settings")
    sidebar_server_settings()

BASE_URL = get_base_url()

st.subheader("📊 주식")

st.markdown("### 주식 목록")
s_off, s_cnt, s_btn1, s_btn2 = st.columns([1, 1, 1, 1])
with s_off:
    st_offset = st.number_input("offset", min_value=0, value=0, step=1, key="st_offset")
with s_cnt:
    st_count = st.number_input("count", min_value=1, value=10, step=1, key="st_count")
with s_btn1:
    st.button(
        "목록 불러오기",
        key="btn_st_load",
        on_click=lambda: set_flag("show_stocks_list", True),
    )
with s_btn2:
    st.button(
        "목록 숨기기",
        key="btn_st_hide",
        on_click=lambda: set_flag("show_stocks_list", False),
    )

if flags()["show_stocks_list"]:
    params = {"offset": int(st_offset), "count": int(st_count)}
    debug_url(BASE_URL, "/api/stocks/list", params)
    status, data, _ = api_call("get", BASE_URL, "/api/stocks/list", params=params)
    show_status("GET /api/stocks/list", status, data)
    rows = rows_from_body_list(data)
    render_table_exact(
        rows,
        wanted_cols=["id", "stockName", "stockPrice"],
        rename={"id": "ID", "stockName": "종목명", "stockPrice": "가격"},
        floats=["stockPrice"],
    )

st.divider()
st.markdown("### 개별 주식 조회")
stock_id_query = st.text_input("조회할 stockId", key="st_query_id")
if st.button("조회", key="btn_st_query"):
    debug_url(BASE_URL, f"/api/stocks/{stock_id_query}")
    status, data, _ = api_call("get", BASE_URL, f"/api/stocks/{stock_id_query}")
    show_status(f"GET /api/stocks/{stock_id_query}", status, data)
    body = data.get("body") if isinstance(data, dict) else None
    rows = [body] if isinstance(body, dict) else []
    render_table_exact(
        rows,
        wanted_cols=["id", "stockName", "stockPrice"],
        rename={"id": "ID", "stockName": "종목명", "stockPrice": "가격"},
        floats=["stockPrice"],
    )

st.divider()
st.markdown("### 주식 등록")
with st.form("form_st_add", clear_on_submit=True):
    sname = st.text_input("종목명", key="st_add_name")
    sprice = st.text_input("가격", key="st_add_price")
    s_add = st.form_submit_button("등록", key="btn_st_add")
if s_add:
    try:
        payload = {"stockName": sname, "stockPrice": float(sprice)}
    except ValueError:
        st.error("가격은 숫자여야 합니다.")
        payload = None
    if payload:
        status, data, _ = api_call("post", BASE_URL, "/api/stocks", json=payload)
        show_status("POST /api/stocks", status, data)

st.divider()
st.markdown("### 주식 수정")
with st.form("form_st_upd", clear_on_submit=True):
    sid = st.text_input("id", key="st_upd_id")
    sname_u = st.text_input("종목명(새)", key="st_upd_name")
    sprice_u = st.text_input("가격(새)", key="st_upd_price")
    s_upd = st.form_submit_button("수정", key="btn_st_upd")
if s_upd:
    try:
        payload = {"id": int(sid), "stockName": sname_u, "stockPrice": float(sprice_u)}
    except ValueError:
        st.error("id/가격은 숫자여야 합니다.")
        payload = None
    if payload:
        status, data, _ = api_call("put", BASE_URL, "/api/stocks", json=payload)
        show_status("PUT /api/stocks", status, data)

st.divider()
st.markdown("### 주식 삭제 (Body JSON)")
del_id = st.text_input("삭제할 id", key="st_del_id")
if st.button("삭제", key="btn_st_del"):
    try:
        payload = {"id": int(del_id)}
    except ValueError:
        st.error("id는 숫자여야 합니다.")
        payload = None
    if payload:
        status, data, _ = api_call("delete", BASE_URL, "/api/stocks", json=payload)
        show_status("DELETE /api/stocks", status, data)
