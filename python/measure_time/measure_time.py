import time
import functools


def measure_time(func):
    """
    함수 실행 시간 측정 데코레이터
    - 함수 실행 전/후 시간을 기록하여 실행 시간 출력
    """

    @functools.wraps(func)  # 원래 함수 이름/docstring 유지
    def wrapper(*args, **kwargs):
        start_time = time.time()  # 함수 실행 시작 시각 기록
        result = func(*args, **kwargs)  # 전달받은 함수 실행 후 결과 저장
        end_time = time.time()  # 함수 실행 종료 시각 기록

        print(
            f"{func.__name__} took {end_time-start_time:.4f} seconds"
        )  # 함수 실행 시간 출력
        return result  # 원래 함수 반환값 그대로 반환

    return wrapper  # 데코레이터로 사용할 수 있도록 wrapper 함수 반환
