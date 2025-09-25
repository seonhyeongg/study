from typing import Any, Dict, Tuple
import streamlit as st
from core.config import TIMEOUT


def safe_json(resp):
    """
    응답을 JSON으로 파싱, 실패 시 text 반환

    Args:
        resp (request.Response): HTTP 응답 객체

    Returns:
        Dict[str, Any]: 응답 JSON dict, 실패시 {"text": str}
    """
    try:
        return resp.json()
    except Exception:
        return {"text": resp.text}


def api_call(
    method: str, base: str, path: str, **kwargs
) -> Tuple[int | None, Dict[str, Any], Dict[str, str]]:
    """
    세션 내 http 클라이언트로 API 호출 수행

    Args:
        method (str): HTTP 메서드명
        base (str): BASE_URL
        path (str): EndPoint
        **kwargs: 추가 요청 옵션

    Returns:
        Tuple[Optional[int], Dict[str, Any], Dict[str, Any]]:
            - status_code (int | None): HTTP 상태 코드, 실패 시 None
            - body (Dict[str, Any]): 응답 JSON dict 또는 {"error": str}
            - headers (Dict[str, Any]): 응답 header dict
    """
    try:
        r = getattr(st.session_state.http, method)(
            f"{base}{path}", timeout=TIMEOUT, **kwargs
        )
        return r.status_code, safe_json(r), dict(r.headers)
    except Exception as e:
        return None, {"error": str(e)}, {}
