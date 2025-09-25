package com.sk.skala.stockapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.stockapi.data.table.Stock;


/**
 * StockRepository 인터페이스
 * 
 * Stock 엔터티 전용 Repository
 * 
 * JpaRepository<Stock, Long>을 상속받아 기본 CRUD 메서드를 제공함
 */
public interface StockRepository extends JpaRepository<Stock, Long> {
    /**
     * 주식 이름으로 Stock 검색
     * 
     * @param stockName 주식 이름
     * @return 검색된 주식 (없을 경우 empty 반환)
     */
    Optional<Stock> findByStockName(String stockName);
}
