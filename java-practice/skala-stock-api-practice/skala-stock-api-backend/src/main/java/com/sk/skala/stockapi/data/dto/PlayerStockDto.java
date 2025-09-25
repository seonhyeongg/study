package com.sk.skala.stockapi.data.dto;

import lombok.Builder;
import lombok.Data;


/**
 * PlayerStockDto 클래스
 * 
 * 플레이어가 보유한 특정 주식 정보를 전달하기 위한 DTO
 */
@Data
@Builder
public class PlayerStockDto {

    private Long stockId;
    private String stockName;
    private Double stockPrice;
    private Integer quantity;
}
