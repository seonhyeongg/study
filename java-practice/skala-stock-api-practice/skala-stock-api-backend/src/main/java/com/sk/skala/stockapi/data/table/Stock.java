package com.sk.skala.stockapi.data.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Stock 엔터티 클래스
 * 
 * 데이터베이스의 주식 정보를 매핑하기 위한 클래스
 */
@Entity
@Data
@NoArgsConstructor
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String stockName;
	private Double stockPrice;

	/**
	 * Stock 객체를 생성한다.
	 * 
	 * @param name 주식 이름
	 * @param price 주식 가격
	 */
	public Stock(String name, Double price) {
		this.stockName = name;
		this.stockPrice = price;
	}
}
