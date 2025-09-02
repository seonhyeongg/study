import time
import functools
from typing import Callable, Any


def measure_time(func: Callable) -> Callable:
    """
    함수 실행 시간 측정 데코레이터

    지정된 함수를 실행하기 전/후 시간을 기록하여
    실행 시간을 초 단위로 출력한다.

    Args:
        func (Callable): 실행 시간을 측정할 대상 함수

    Returns:
        Callable: 실행 시간을 측정하는 wrapper 함수
    """

    @functools.wraps(func)  # 원래 함수 이름 및 docstring 유지
    def wrapper(*args: Any, **kwargs: Any) -> Any:
        start_time = time.time()  # 함수 실행 시작 시각 기록
        result = func(*args, **kwargs)  # 전달받은 함수 실행 후 결과 저장
        end_time = time.time()  # 함수 실행 종료 시각 기록

        print(
            f"{func.__name__} took {end_time-start_time:.4f} seconds"
        )  # 함수 실행 시간 출력
        return result  # 원래 함수 반환값 그대로 반환

    return wrapper
