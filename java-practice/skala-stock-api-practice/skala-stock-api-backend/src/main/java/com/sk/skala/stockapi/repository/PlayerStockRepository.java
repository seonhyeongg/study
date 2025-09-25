package com.sk.skala.stockapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.stockapi.data.table.PlayerStock;
import java.util.List;
import java.util.Optional;
import com.sk.skala.stockapi.data.table.Player;
import com.sk.skala.stockapi.data.table.Stock;


/**
 * PlayerStockRepository 인터페이스
 * 
 * PlayerStock 엔터티 전용 Repository
 * 
 * JpaRepository<PlayerStock, Long>을 상속받아 기본 CRUD 메서드를 제공함
 */
public interface PlayerStockRepository extends JpaRepository<PlayerStock, Long> {
    /**
     * 플레이어 ID로 보유 주식 목록 조회
     * 
     * @param playerId 플레이어 ID
     * @return 보유한 PlayerStock 리스트
     */
    List<PlayerStock> findByPlayer_PlayerId(String playerId);

    /**
     * 특정 플레이어의 특정 주식 보유 여부 조회
     * 
     * @param player 플레이어 엔터티
     * @param stock 주식 엔터티
     * @return 검색된 PlayerStock (없을 경우 empty 반환)
     */
    Optional<PlayerStock> findByPlayerAndStock(Player player, Stock stock);
}
