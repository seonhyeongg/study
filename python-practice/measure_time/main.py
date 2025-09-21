"""
실행 시간 측정 데코레이터 사용 예제
--------------------
- slow_function: 1.5초 지연 후 메시지 반환
- add: 두 수의 합 계산 (0.5초 지연)
"""

from measure_time import measure_time
import time


# ===== 테스트용 함수 정의 ==========


@measure_time
def slow_function() -> str:
    """
    지연 실행 함수 (실행 시간 측정용)

    Returns:
        str: 완료 메시지
    """
    time.sleep(1.5)  # 1.5초 지연 (실행 시간 확인용)
    return "완료!"


@measure_time
def add(a: int, b: int) -> int:
    """
    두 수의 합 계산 (실행 시간 측정용)

    Args:
        a (int): 첫 번째 숫자
        b (int): 두 번째 숫자

    Returns:
        int: 두 수의 합
    """
    time.sleep(0.5)  # 0.5초 지연 (실행 시간 확인용)
    return a + b


# ===== 함수 실행 ==========

print(slow_function())
print(add(3, 5))
