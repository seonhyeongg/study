from urllib.parse import urlencode
import streamlit as st


def show_status(label: str, status: int | None, data):
    """
    API 호출 결과 상태를 Streamlit UI에 표시

    Args:
        label (str): 상태 표시 레이블
        status (Optional[int]): HTTP 상태 코드, None이면 요청 실패
        data (Any): 응답 데이터

    Returns:
        None
    """
    if status is None:
        st.error(f"{label}: 요청 실패")
    elif 200 <= status < 300:
        st.success(f"{label}: {status}")
    else:
        st.warning(f"{label}: {status}")
    with st.expander("응답 내용 보기", expanded=False):
        st.write(data)


def debug_url(base: str, path: str, params: dict | None = None):
    """
    요청 URL을 Streamlit 캡션으로 출력 (디버깅용)

    Args:
        base (str): BASE_URL
        path (str): EndPoint 경로
        params (Optional[Dict[str, Any]]): 쿼리 파라미터 dict

    Returns:
        None
    """
    q = f"?{urlencode(params)}" if params else ""
    st.caption(f"➡️ {base}{path}{q}")
