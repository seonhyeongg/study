import streamlit as st
from dotenv import load_dotenv

from core.config import APP_TITLE, APP_ICON, APP_LAYOUT, sidebar_server_settings
from core.state import ensure_session_state


load_dotenv()

st.set_page_config(page_title=APP_TITLE, page_icon=APP_ICON, layout=APP_LAYOUT)
ensure_session_state()

st.title("📈 " + APP_TITLE)

with st.sidebar:
    st.header("Server Settings")
    sidebar_server_settings()

st.caption(
    "왼쪽 사이드바에서 서버 주소를 설정하고, 상단 탭(페이지)에서 기능을 사용하세요."
)
