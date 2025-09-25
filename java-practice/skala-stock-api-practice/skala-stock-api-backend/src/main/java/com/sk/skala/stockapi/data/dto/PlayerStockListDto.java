package com.sk.skala.stockapi.data.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;


/**
 * PlayerStockListDto 클래스
 * 
 * 특정 플레이어의 자산 정보와 보유한 주식 목록를 전달하기 위한 DTO
 */
@Data
@Builder
public class PlayerStockListDto {

    private String playerId;
    private Double playerMoney;
    private List<PlayerStockDto> stocks;
}
