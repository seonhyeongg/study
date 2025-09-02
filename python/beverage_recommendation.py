from dataclasses import dataclass
from typing import List

# ===== 데이터 클래스 정의 ===============


@dataclass
class Beverage:
    """메뉴에 등록되는 음료: 이름, 가격, 태그"""

    name: str
    price: int
    tags: List[str]


class Order:
    """특정 음료와 수량으로 구성된 주문 1건"""

    def __init__(self, beverage: Beverage, quantity: int):
        self.beverage = beverage
        self.quantity = quantity

    @property
    def total_price(self) -> int:
        """
        주문 1건의 총 금액 계산: 음료 가격 * 주문 수량
        """
        return self.beverage.price * self.quantity


class User:
    """사용자: 이름, 주문 내역"""

    def __init__(self, name: str):
        self.name = name
        self.order_list = []  # 사용자가 현재까지 주문한 기록

    def order_append(self, order: Order):
        """새로운 주문을 사용자 주문 내역에 추가"""
        self.order_list.append(order)

    def get_total_price(self) -> int:
        """현재까지 주문한 모든 주문의 총 금액 계산"""
        return sum(order.total_price for order in self.order_list)

    def get_recent_tags(self, n: int = 2) -> List[str]:
        """
        최근 n건 주문 내역에서 등장한 태그 반환
        - set으로 변환하여 중복 태그 제거
        - 기본값 n=2 -> 최근 2개 주문 기준
        """
        tags = []
        for order in self.order_list[-n:]:  # 최근 n개 주문 확인
            tags.extend(order.beverage.tags)  # 해당 음료 태그 추가

        return list(set(tags))  # 중복 제거 후 리스트로 변환


class Recommendation:
    """추천: 전체 메뉴 중에서 사용자 추천 음료 검색"""

    def __init__(self, menu: List[Beverage]):
        self.menu = menu

    def recommend(self, user: User) -> List[str]:
        """
        사용자의 최근 주문 태그를 기반으로 메뉴에서 추천 음료 선택
        - 사용자 최근 태그와 메뉴의 태그가 겹치면 추천 리스트에 추가
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
