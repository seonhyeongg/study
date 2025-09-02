from measure_time import measure_time
import time


# ===== 테스트용 함수 정의 ==========


@measure_time  # slow_function 실행 시간 자동 측정을 위해 데코레이터 적용
def slow_function():
    time.sleep(1.5)  # 1.5초 지연 (실행 시간 확인용)
    return "완료!"


@measure_time  # add 함수 실행 시간 자동 측정
def add(a, b):
    time.sleep(0.5)  # 0.5초 지연 (실행 시간 확인용)
    return a + b


# ===== 함수 실행 ==========

print(slow_function())
print(add(3, 5))
