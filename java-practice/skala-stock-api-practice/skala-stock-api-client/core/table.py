import pandas as pd
import streamlit as st


def rows_from_body_list(data):
    """
    응답 데이터에서 body.list 추출

    Args:
        data (Dict[str, Any]): API 응답 dict

    Returns:
        List[Dict[str, Any]]: list가 존재하면 리스트 반환, 없으면 빈 리스트
    """
    if not isinstance(data, dict):
        return []
    body = data.get("body")
    if not isinstance(body, dict):
        return []
    lst = body.get("list")
    return lst if isinstance(lst, list) else []


def render_table_exact(rows, wanted_cols, rename=None, floats=None, round_nd=2):
    """
    지정한 컬럼만 포함한 DataFrame 생성 후 Streamlit 테이블로 출력

    Args:
        rows (List[Dict[str, Any]]): 테이블로 변환할 데이터 행
        wanted_cols (List[str]): 표시할 컬럼 이름 목록
        rename (Optional[Dict[str, str]]): 컬럼명 변경 매핑 dict
        floats (Optional[List[str]]): 소수점 반올림 적용할 컬럼명 목록
        round_nd (int): 소수점 반올림 자리수 (기본값: 2)

    Returns:
        None
    """
    if not rows:
        st.info("데이터가 없습니다.")
        return
    df = pd.DataFrame(rows)
    cols = [c for c in wanted_cols if c in df.columns]
    if cols:
        df = df[cols]
    if rename:
        df = df.rename(columns=rename)
    if floats:
        for c in floats:
            if c in df.columns:
                df[c] = pd.to_numeric(df[c], errors="coerce").round(round_nd)
    st.dataframe(df, use_container_width=True)
