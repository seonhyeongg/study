package com.sk.skala.stockapi.data.table;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * PlayerStock 엔터티 클래스
 * 
 * 특정 플레이어가 어떤 주식을 얼마나 보유하고 있는지를 매핑하기 위한 테이블 포트폴리오의 기본 단위로 사용됨
 * 
 * - @ManyToOne: 플레이어와 주식에 대한 다대일 관계 설정 (여러 PlayerStock 레코드가 하나의 Player 또는 Stock과 연결될 수 있음)
 * - @JoinColumn: 외래키 이름 지정
 * 
 */
@Entity
@Data
@NoArgsConstructor
public class PlayerStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id")
	private Player player;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_id")
	private Stock stock;

	private Integer quantity;

	/**
	 * PlayerStock 객체를 생성한다.
	 * 
	 * @param player 플레이어
	 * @param stock 보유 주식
	 * @param quantity 보유 수량
	 */
	public PlayerStock(Player player, Stock stock, Integer quantity) {
		this.player = player;
		this.stock = stock;
		this.quantity = quantity;
	}
}
