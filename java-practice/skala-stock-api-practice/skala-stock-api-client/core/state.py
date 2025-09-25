import requests
import streamlit as st


def ensure_session_state():
    """
    세션 상태 기본값 초기화

    Args:
        None

    Returns:
        None
    """
    st.session_state.setdefault("auth", {"is_logged_in": False, "playerId": None})
    st.session_state.setdefault(
        "flags",
        {
            "show_players_list": False,
            "show_stocks_list": False,
        },
    )
    if "http" not in st.session_state:
        st.session_state.http = requests.Session()


def auth():
    """
    현재 인증 상태 반환

    Returns:
        dict: {"is_logged_in": bool, "playerId": str | None}
    """
    return st.session_state["auth"]


def set_logged_in(player_id: str):
    """
    로그인 상태로 전환

    Args:
        player_id (str): 로그인한 플레이어 ID

    Returns:
        None
    """
    st.session_state["auth"].update(is_logged_in=True, playerId=player_id)


def logout():
    """
    로그아웃 처리

    Args:
        None

    Returns:
        None
    """
    st.session_state["auth"].update(is_logged_in=False, playerId=None)


def flags():
    """
    UI 표시 플래그 상태 반환

    Returns:
        dict: {"show_players_list": bool, "show_stocks_list": bool}
    """
    return st.session_state["flags"]


def set_flag(key: str, value: bool):
    """
    UI 표시 플래그 값 설정

    Args:
        key (str): 플래그 이름
        value (bool): 플래그 값

    Returns:
        None
    """
    st.session_state["flags"][key] = value
