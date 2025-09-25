package com.sk.skala.stockapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.table.Player;
import com.sk.skala.stockapi.service.PlayerService;
import com.sk.skala.stockapi.data.dto.PlayerSession;
import com.sk.skala.stockapi.data.dto.StockOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * PlayerController
 * 
 * 플레이어 관련 API EndPoint를 제공하는 REST Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    /**
     * 전체 플레이어 목록 조회
     * 
     * @param offset 페이지 인덱스
     * @param count 페이지 크기
     * @return 플레이어 목록 + 페이징 정보
     */
    @GetMapping("/list")
    public Response getAllPlayers(@RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer count) {
        return playerService.getAllPlayers(offset, count);
    }

    /**
     * 플레이어 단건 조회
     * 
     * @param playerId 플레이어 ID
     * @return 플레이어 정보 + 보유 주식 현황
     */
    @GetMapping("/{playerId}")
    public Response getPlayerById(@PathVariable String playerId) {
        return playerService.getPlayerById(playerId);
    }

    /**
     * 플레이어 생성
     * 
     * @param player 생성할 플레이어 정보
     * @return 처리 결과
     */
    @PostMapping
    public Response createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    /**
     * 플레이어 로그인
     * 
     * @param playerSession 로그인 요청 정보
     * @return 로그인 성공 시 세션 토큰 포함
     */
    @PostMapping("login")
    public Response loginPlayer(@RequestBody PlayerSession playerSession) {
        return playerService.loginPlayer(playerSession);
    }

    /**
     * 플레이어 수정
     * 
     * @param player 수정할 플레이어 정보
     * @return 처리 결과
     */
    @PutMapping
    public Response updatePlayer(@RequestBody Player player) {
        return playerService.updatePlayer(player);
    }

    /**
     * 플레이어 삭제
     * 
     * @param player 삭제할 플레이어 정보
     * @return 처리 결과
     */
    @DeleteMapping
    public Response deletedPlayer(@RequestBody Player player) {
        return playerService.deletePlayer(player);
    }

    /**
     * 주식 매수
     * 
     * @param order 매수 주문
     * @return 처리 결과
     */
    @PostMapping("/buy")
    public Response buyPlayerStock(@RequestBody StockOrder order) {
        return playerService.buyPlayerStock(order);
    }

    /**
     * 주식 매도
     * 
     * @param order 매도 주문
     * @return 처리 결과
     */
    @PostMapping("/sell")
    public Response sellPlayerStock(@RequestBody StockOrder order) {
        return playerService.sellPlayerStock(order);
    }

}
