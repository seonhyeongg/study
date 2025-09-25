import os
import streamlit as st


# ===== 앱 기본 설정값 ===============

APP_TITLE = "SKALA STOCK API"
APP_ICON = "📈"
APP_LAYOUT = "wide"


# ===== 기본 서버 URL / 요청 타임아웃 ===============

DEFAULT_BASE_URL = os.environ.get("BASE_URL", "http://localhost:9080")
TIMEOUT = 8


def sidebar_server_settings():
    """
    사이드바에서 서버 주소 (BASE_URL)를 입력받아 세션 상태에 저장

    Args:
        None

    Returns:
        None
    """
    st.session_state.setdefault("BASE_URL", DEFAULT_BASE_URL)
    base = st.text_input(
        "BASE_URL", value=st.session_state["BASE_URL"], help="예: http://localhost:9080"
    )
    st.session_state["BASE_URL"] = base


def get_base_url() -> str:
    """
    현재 세션에서 BASE_URL을 가져옴

    Args:
        None

    Returns:
        str: 세션에 저장된 BASE_URL, 없는 경우 DEFAULT_BASE_URL
    """
    return st.session_state.get("BASE_URL", DEFAULT_BASE_URL)
