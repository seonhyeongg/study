package com.sk.skala.stockapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.stockapi.data.table.Player;


/**
 * PlayerRepository 인터페이스
 * 
 * Player 엔터티 전용 Repository
 * 
 * JpaRepository<Player, String>을 상속받아 기본 CRUD 메서드를 제공함
 */
public interface PlayerRepository extends JpaRepository<Player, String> {
}
