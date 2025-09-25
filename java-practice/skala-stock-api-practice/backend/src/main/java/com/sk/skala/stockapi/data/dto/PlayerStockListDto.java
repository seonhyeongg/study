package com.sk.skala.stockapi.data.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerStockListDto {

    private String playerId;
    private Double playerMoney;
    private List<PlayerStockDto> stocks;
}
