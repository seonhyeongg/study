package com.sk.skala.stockapi.data.table;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Player 엔터티 클래스
 * 
 * 데이터베이스의 플레이어 정보를 매핑하기 위한 클래스
 */
@Entity
@Data
@NoArgsConstructor
public class Player {

	@Id
	private String playerId;

	private String playerPassword;
	private Double playerMoney;

	/**
	 * Player 객체를 생성한다.
	 * 
	 * @param id 플레이어 ID
	 * @param money 초기 보유 자산
	 */
	public Player(String id, Double money) {
		this.playerId = id;
		this.playerMoney = money;
	}

}
