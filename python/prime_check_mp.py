"""
소수 판별
--------------------
1~10,000,000 범위의 난수를 1000만개 생성하여
단일 프로세스와 멀티 프로세스로 소수 판별 성능 비교
"""

import time
import random
from os import cpu_count
from multiprocessing import Pool


def is_prime(n: int) -> bool:
    """
    소수 판별

    규칙:
        - 2 미만: 소수 아님
        - 2: 소수
        - 짝수: 소수 아님 (2 제외)
        - 홀수: sqrt(n) 이하의 홀수로만 나누어 소수 여부 확인

    Args:
        n (int): 확인할 수

    Returns:
        bool: True if prime, False otherwise
    """
    if n < 2:
        return False
    if n == 2:
        return True
    if n % 2 == 0:
        return False
    for i in range(3, int(n**0.5) + 1, 2):
        if n % i == 0:
            return False
    return True


if __name__ == "__main__":
    # 1. 난수 정수 생성
    # 2. single process에서 소수 판별 실행
    # 3. multi process에서 소수 판별 실행
    # 4. 소수 개수 및 실행 시간 출력

    # 데이터 생성
    num_list = [random.randint(1, 10_000_000) for _ in range(10_000_000)]

    # 단일 프로세스 실행
    start_time_single = time.time()
    result_single = list(map(is_prime, num_list))
    end_time_single = time.time()

    print("single processing")
    print("result:", result_single.count(True))
    print("time:", end_time_single - start_time_single)

    # 멀티 프로세스 실행
    start_time_multi = time.time()
    with Pool(processes=cpu_count()) as pool:
        result_multi = pool.map(is_prime, num_list)
    end_time_multi = time.time()

    print("multi processing")
    print("result:", result_multi.count(True))
    print("time:", end_time_multi - start_time_multi)
