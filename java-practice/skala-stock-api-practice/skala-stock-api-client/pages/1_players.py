import streamlit as st

from core.config import (
    APP_TITLE,
    APP_ICON,
    APP_LAYOUT,
    sidebar_server_settings,
    get_base_url,
)
from core.state import (
    ensure_session_state,
    auth,
    set_logged_in,
    logout,
    flags,
    set_flag,
)
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


# ===== 플레이어 탭 구성 ===============

st.subheader("👤 플레이어")
tabs = st.tabs(["📋 CRUD", "🔐 로그인·거래"])


# ===== CRUD ===============

with tabs[0]:
    st.markdown("### 플레이어 목록")
    c_off, c_cnt, c_btn1, c_btn2 = st.columns([1, 1, 1, 1])
    with c_off:
        pl_offset = st.number_input(
            "offset", min_value=0, value=0, step=1, key="pl_offset"
        )
    with c_cnt:
        pl_count = st.number_input(
            "count", min_value=1, value=10, step=1, key="pl_count"
        )
    with c_btn1:
        st.button(
            "목록 불러오기",
            key="btn_pl_load",
            on_click=lambda: set_flag("show_players_list", True),
        )
    with c_btn2:
        st.button(
            "목록 숨기기",
            key="btn_pl_hide",
            on_click=lambda: set_flag("show_players_list", False),
        )

    if flags()["show_players_list"]:
        params = {"offset": int(pl_offset), "count": int(pl_count)}
        debug_url(BASE_URL, "/api/players/list", params)
        status, data, _ = api_call("get", BASE_URL, "/api/players/list", params=params)
        show_status("GET /api/players/list", status, data)
        rows = rows_from_body_list(data)
        render_table_exact(
            rows,
            wanted_cols=["playerId", "playerPassword", "playerMoney"],
            rename={"playerId": "ID", "playerPassword": "PW", "playerMoney": "자산"},
            floats=["playerMoney"],
        )

    st.divider()
    st.markdown("### 플레이어 등록")
    with st.form("form_pl_add", clear_on_submit=True):
        pid = st.text_input("ID", key="pl_add_id")
        pw = st.text_input("비밀번호", type="password", key="pl_add_pw")
        money = st.text_input("자산", key="pl_add_money")
        sub_add = st.form_submit_button("등록", key="btn_pl_add")
    if sub_add:
        try:
            payload = {
                "playerId": pid,
                "playerPassword": pw,
                "playerMoney": float(money),
            }
        except ValueError:
            st.error("자산은 숫자여야 합니다.")
            payload = None
        if payload:
            status, data, _ = api_call("post", BASE_URL, "/api/players", json=payload)
            show_status("POST /api/players", status, data)

    st.divider()
    st.markdown("### 플레이어 수정")
    with st.form("form_pl_upd", clear_on_submit=True):
        up_id = st.text_input("ID", key="pl_upd_id")
        up_money = st.text_input("새 자산", key="pl_upd_money")
        sub_upd = st.form_submit_button("수정", key="btn_pl_upd")
    if sub_upd:
        try:
            payload = {"playerId": up_id, "playerMoney": float(up_money)}
        except ValueError:
            st.error("새 자산은 숫자여야 합니다.")
            payload = None
        if payload:
            status, data, _ = api_call("put", BASE_URL, "/api/players", json=payload)
            show_status("PUT /api/players", status, data)

    st.divider()
    st.markdown("### 플레이어 삭제")
    del_id = st.text_input("삭제할 ID", key="pl_del_id")
    if st.button("삭제", key="btn_pl_del"):
        payload = {"playerId": del_id}
        status, data, _ = api_call("delete", BASE_URL, "/api/players", json=payload)
        show_status("DELETE /api/players", status, data)

# ===== 로그인 / 거래 ===============

with tabs[1]:
    st.markdown("### 로그인")
    with st.form("form_pl_login", clear_on_submit=True):
        lid = st.text_input("ID", key="pl_login_id")
        lpw = st.text_input("PW", type="password", key="pl_login_pw")
        sub_login = st.form_submit_button("로그인", key="btn_pl_login")

    if sub_login:
        payload = {"playerId": lid, "playerPassword": lpw}
        status, data, _ = api_call("post", BASE_URL, "/api/players/login", json=payload)
        show_status("POST /api/players/login", status, data)
        if status and 200 <= status < 300:
            set_logged_in(lid)

    a = auth()
    if a["is_logged_in"]:
        pid = a["playerId"]
        st.success(f"로그인 성공: ({pid})")

        st.markdown("### 내 정보 조회")
        if st.button("조회", key="btn_user_detail"):
            debug_url(BASE_URL, f"/api/players/{pid}")
            status, data, _ = api_call("get", BASE_URL, f"/api/players/{pid}")
            show_status(f"GET /api/players/{pid}", status, data)

            body = data.get("body") if isinstance(data, dict) else None
            if isinstance(body, dict):
                player_info = [
                    {
                        "playerId": body.get("playerId"),
                        "playerMoney": body.get("playerMoney"),
                    }
                ]
                st.markdown("#### 👤 플레이어 정보")
                render_table_exact(
                    player_info,
                    wanted_cols=["playerId", "playerMoney"],
                    rename={"playerId": "ID", "playerMoney": "자산"},
                    floats=["playerMoney"],
                )

                stocks = body.get("stocks") or []
                st.markdown("#### 📊 보유 주식")
                render_table_exact(
                    stocks,
                    wanted_cols=["stockId", "stockName", "stockPrice", "quantity"],
                    rename={
                        "stockId": "ID",
                        "stockName": "종목명",
                        "stockPrice": "가격",
                        "quantity": "보유수량",
                    },
                    floats=["stockPrice"],
                )

        st.divider()
        st.markdown("### 주식 매수")
        with st.form("form_buy", clear_on_submit=True):
            sid = st.text_input("stockId", key="buy_sid")
            qty = st.text_input("quantity", key="buy_qty")
            buyb = st.form_submit_button("매수", key="btn_buy")
        if buyb:
            try:
                payload = {
                    "playerId": pid,
                    "stockId": int(sid),
                    "stockQuantity": int(qty),
                }
            except ValueError:
                st.error("stockId/quantity는 숫자여야 합니다.")
                payload = None
            if payload:
                status, data, _ = api_call(
                    "post", BASE_URL, "/api/players/buy", json=payload
                )
                show_status("POST /api/players/buy", status, data)

        st.markdown("### 주식 매도")
        with st.form("form_sell", clear_on_submit=True):
            sid2 = st.text_input("stockId", key="sell_sid")
            qty2 = st.text_input("quantity", key="sell_qty")
            sellb = st.form_submit_button("매도", key="btn_sell")
        if sellb:
            try:
                payload = {
                    "playerId": pid,
                    "stockId": int(sid2),
                    "stockQuantity": int(qty2),
                }
            except ValueError:
                st.error("stockId/quantity는 숫자여야 합니다.")
                payload = None
            if payload:
                status, data, _ = api_call(
                    "post", BASE_URL, "/api/players/sell", json=payload
                )
                show_status("POST /api/players/sell", status, data)

        if st.button("로그아웃", key="btn_logout"):
            logout()
            st.info("로그아웃됨")
