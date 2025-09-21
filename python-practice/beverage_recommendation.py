"""
음료 추천 시스템
--------------------
사용자 주문 내역을 관리하고 최근 태그 기반으로 음료를 추천한다.
"""

from dataclasses import dataclass
from typing import List

# ===== 데이터 클래스 정의 ===============


@dataclass
class Beverage:
    """
    메뉴에 등록되는 음료

    Attributes:
        name (str): 음료 이름
        price (int): 음료 가격
        tags (List[str]): 음료 특징 태그 목록
    """

    name: str
    price: int
    tags: List[str]


class Order:
    """
    특정 음료와 수량으로 구성된 주문 1건

    Attributes:
        beverage (Beverage): 주문한 음료
        quantity (int): 주문 수량
    """

    def __init__(self, beverage: Beverage, quantity: int):
        self.beverage = beverage
        self.quantity = quantity

    @property
    def total_price(self) -> int:
        """
        주문 1건의 총 금액 계산

        Returns:
            int: 음료 가격 * 주문 수량
        """
        return self.beverage.price * self.quantity


class User:
    """
    사용자 정보 및 주문 내역 관리

    Attributes:
        name (str): 사용자 이름
        order_list (List[Order]): 사용자 주문 내역
    """

    def __init__(self, name: str):
        self.name = name
        self.order_list = []  # 사용자가 현재까지 주문한 기록

    def order_append(self, order: Order):
        """
        새로운 주문을 사용자 주문 내역에 추가

        Args:
            order (Order): 추가할 주문
        """
        self.order_list.append(order)

    def get_total_price(self) -> int:
        """
        현재까지 주문한 모든 주문의 총 금액 계산

        Returns:
            int: 모든 주문의 총합 금액
        """
        return sum(order.total_price for order in self.order_list)

    def get_recent_tags(self, n: int = 2) -> List[str]:
        """
        최근 주문 내역에서 태그 추출

        Args:
            n (int, optional): 최근 n개의 주문 기준 (기본값 2)

        Returns:
            List[str]: 최근 주문에서 추출된 고유 태그 목록
        """
        tags = []
        for order in self.order_list[-n:]:  # 최근 n개 주문 확인
            tags.extend(order.beverage.tags)  # 해당 음료 태그 추가

        return list(set(tags))  # 중복 제거 후 리스트로 변환


class Recommendation:
    """
    추천 시스템: 사용자 주문 태그 기반 메뉴 추천

    Attributes:
        menu (List[Beverage]): 전체 메뉴 리스트
    """

    def __init__(self, menu: List[Beverage]):
        self.menu = menu

    def recommend(self, user: User) -> List[str]:
        """
        사용자 최근 주문 태그와 메뉴 태그를 비교해 추천 음료 제공

        Args:
            user (User): 추천 대상 사용자

        Returns:
            List[str]: 추천 음료 이름 리스트
        """
        recent_tags = user.get_recent_tags()
        recommendations = []

        for beverage in self.menu:
            if any(tag in beverage.tags for tag in recent_tags):
                recommendations.append(beverage.name)

        return recommendations


# ===== 테스트 시나리오 ===============

menu = [
    Beverage("아이스 아메리카노", 3000, ["커피", "콜드"]),
    Beverage("카페라떼", 3500, ["커피", "밀크"]),
    Beverage("녹차", 2800, ["차", "뜨거운"]),
    Beverage("허브티", 3000, ["차", "차가운"]),
]

# 사용자 생성 및 주문 추가
user = User("A")
user.order_append(Order(menu[2], 1))
user.order_append(Order(menu[3], 1))

# 추천 시스템 실행
recommend_sys = Recommendation(menu)
print("추천 음료:", recommend_sys.recommend(user))
print("총 주문 금액:", user.get_total_price())
