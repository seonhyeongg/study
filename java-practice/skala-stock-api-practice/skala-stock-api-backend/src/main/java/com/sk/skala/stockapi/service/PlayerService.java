package com.sk.skala.stockapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sk.skala.stockapi.config.Error;
import com.sk.skala.stockapi.data.common.PagedList;
import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.dto.PlayerSession;
import com.sk.skala.stockapi.data.dto.PlayerStockDto;
import com.sk.skala.stockapi.data.dto.PlayerStockListDto;
import com.sk.skala.stockapi.data.dto.StockOrder;
import com.sk.skala.stockapi.data.table.Player;
import com.sk.skala.stockapi.data.table.PlayerStock;
import com.sk.skala.stockapi.data.table.Stock;
import com.sk.skala.stockapi.exception.ParameterException;
import com.sk.skala.stockapi.exception.ResponseException;
import com.sk.skala.stockapi.repository.PlayerRepository;
import com.sk.skala.stockapi.repository.PlayerStockRepository;
import com.sk.skala.stockapi.repository.StockRepository;
import com.sk.skala.stockapi.tools.StringTool;
import lombok.RequiredArgsConstructor;


/**
 * PlayerService
 * 
 * Player 엔터티에 대한 CRUD와 매수/매도 도메인 로직을 담당하는 서비스 계층
 * 
 * Controller에서 넘어온 파라미터의 유효성을 검증하고, 도메인 정책을 적용한 뒤, Repository를 통해 영속성 계층에 접근함
 */
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final StockRepository stockRepository;
    private final PlayerRepository playerRepository;
    private final PlayerStockRepository playerStockRepository;
    private final SessionHandler sessionHandler;

    /**
     * 전체 플레이어 목록 페이지 단위 조회
     * 
     * @param offset 페이지 인덱스 (0-base)
     * @param count 페이지 크기
     * @return 페이징 메타데이터의 목록을 담은 Response
     * @throws ParameterException count <=0 또는 offset < 0인 경우
     */
    public Response getAllPlayers(int offset, int count) {
        if (count <= 0 || offset < 0) {
            throw new ParameterException("offset", "count");
        }
        Pageable pageable = PageRequest.of(offset, count, Sort.by("playerId"));
        Page<Player> paged = playerRepository.findAll(pageable);

        PagedList pagedList = new PagedList();
        pagedList.setTotal(paged.getTotalElements());
        pagedList.setOffset(offset);
        pagedList.setCount(paged.getNumberOfElements());
        pagedList.setList(paged.getContent());

        Response response = new Response();
        response.setBody(pagedList);

        return response;
    }

    /**
     * ID로 플레이어 단건 조회
     * 
     * @param playerId 플레이어 ID
     * @return PlayerStockListDto를 담은 Response
     * @throws ParameterExeption playerId가 null인 경우
     * @throws ResponseException 데이터가 없을 경우
     */
    @Transactional(readOnly = true)
    public Response getPlayerById(String playerId) {
        if (playerId == null) {
            throw new ParameterException("id");
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND, "Player not found"));

        List<PlayerStock> playerStocks = playerStockRepository.findByPlayer_PlayerId(playerId);

        List<PlayerStockDto> stockDtos = playerStocks.stream()
                .map(playerStock -> PlayerStockDto.builder().stockId(playerStock.getStock().getId())
                        .stockName(playerStock.getStock().getStockName())
                        .stockPrice(playerStock.getStock().getStockPrice())
                        .quantity(playerStock.getQuantity()).build())
                .collect(Collectors.toList());

        PlayerStockListDto playerStockListDto =
                PlayerStockListDto.builder().playerId(player.getPlayerId())
                        .playerMoney(player.getPlayerMoney()).stocks(stockDtos).build();

        Response response = new Response();
        response.setBody(playerStockListDto);

        return response;
    }

    /**
     * 플레이어 생성
     * 
     * @param playerSession 플레이어 정보
     * @return 빈 본문을 가진 성공 Response
     * @throws ParameterException playerID 또는 playerPassword가 비어있는 경우
     * @throws ResponseException 동일 ID의 플레이어가 이미 존재하는 경우
     */
    public Response createPlayer(Player playerSession) {
        if (StringTool.isAnyEmpty(playerSession.getPlayerId())
                || StringTool.isAnyEmpty(playerSession.getPlayerPassword())) {
            throw new ParameterException("playerId", "playerPassword");
        }

        Optional<Player> option = playerRepository.findById(playerSession.getPlayerId());
        if (option.isPresent()) {
            throw new ResponseException(Error.DATA_DUPLICATED);
        }

        Player player = new Player();
        player.setPlayerId(playerSession.getPlayerId());
        player.setPlayerPassword(playerSession.getPlayerPassword());
        player.setPlayerMoney(playerSession.getPlayerMoney());

        playerRepository.save(player);

        return new Response();
    }

    /**
     * 플레이어 로그인
     * 
     * @param playerSession 로그인 요청 DTO
     * @return 로그인한 플레이어 (비밀번호 null 처리)
     * @throws ParameterException 필수 파라미터가 누락된 경우
     * @throws ResponseException 사용자가 없거나 인증에 실패한 경우
     */
    public Response loginPlayer(PlayerSession playerSession) {
        if (StringTool.isAnyEmpty(playerSession.getPlayerId())
                || StringTool.isAnyEmpty(playerSession.getPlayerPassword())) {
            throw new ParameterException("playerId", "playerPassword");
        }

        Optional<Player> option = playerRepository.findById(playerSession.getPlayerId());
        if (option.isEmpty()) {
            throw new ResponseException(Error.DATA_NOT_FOUND);
        }

        Player player = option.get();
        if (player.getPlayerPassword().equals(playerSession.getPlayerPassword())) {
            sessionHandler.storeAccessToken(playerSession);
        } else {
            throw new ResponseException(Error.NOT_AUTHENTICATED);
        }

        player.setPlayerPassword(null);
        Response response = new Response();
        response.setBody(player);

        return response;
    }

    /**
     * 플레이어 자산 업데이트
     * 
     * @param player playerID, playerMoney 포함
     * @return 빈 본문을 가진 성공 Response
     * @throws ParameterException playerID가 비어있거나 playerMoney <=0인 경우
     * @throws ResponseException 대상 플레이어가 존재하지 않는 경우
     */
    public Response updatePlayer(Player player) {
        if (StringTool.isAnyEmpty(player.getPlayerId()) || player.getPlayerMoney() <= 0) {
            throw new ParameterException("playerId", "playerMoney");
        }

        Optional<Player> option = playerRepository.findById(player.getPlayerId());
        if (option.isEmpty()) {
            throw new ResponseException(Error.DATA_NOT_FOUND);
        }

        Player storedPlayer = option.get();
        storedPlayer.setPlayerMoney(player.getPlayerMoney());

        playerRepository.save(storedPlayer);

        return new Response();
    }

    /**
     * 플레이어 삭제
     * 
     * @param player 삭제할 플레이어
     * @return 빈 본문을 가진 성공 Response
     * @throws ParameterException playerId가 null인 경우
     * @throws ResponseException 대상 플레이어가 존재하지 않는 경우
     */
    public Response deletePlayer(Player player) {
        if (player.getPlayerId() == null) {
            throw new ParameterException("playerId");
        }

        Optional<Player> option = playerRepository.findById(player.getPlayerId());
        if (option.isEmpty()) {
            throw new ResponseException(Error.DATA_NOT_FOUND);
        }

        playerRepository.deleteById(player.getPlayerId());

        return new Response();
    }

    /**
     * 주식 매수
     * 
     * @param order stockId, stockQuantity 포함
     * @return 빈 본문을 가진 성공 Response
     * @throws ResponseException 대상 플레이어/주식이 존재하지 않거나 잔액이 부족한 경우
     */
    @Transactional
    public Response buyPlayerStock(StockOrder order) {
        String playerId = sessionHandler.getPlayerId();

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND, "Player not found"));

        Stock stock = stockRepository.findById(order.getStockId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND, "Stock not found"));

        double totalCost = stock.getStockPrice() * order.getStockQuantity();
        if (totalCost > player.getPlayerMoney()) {
            throw new ResponseException(Error.INSUFFICIENT_FUNDS);
        }

        player.setPlayerMoney(player.getPlayerMoney() - totalCost);
        playerRepository.save(player);

        Optional<PlayerStock> option = playerStockRepository.findByPlayerAndStock(player, stock);

        if (option.isPresent()) {
            PlayerStock existingPlayerStock = option.get();
            existingPlayerStock
                    .setQuantity(existingPlayerStock.getQuantity() + order.getStockQuantity());
            playerStockRepository.save(existingPlayerStock);
        } else {
            PlayerStock newPlayerStock = new PlayerStock(player, stock, order.getStockQuantity());
            playerStockRepository.save(newPlayerStock);
        }

        return new Response();
    }

    /**
     * 주식 매도
     * 
     * @param order stockId, stockQuantity 포함
     * @return 빈 본문을 가진 성공 Response
     * @throws ResponseException 대상 플레이어/주식이 존재하지 않거나, 플레이어가 대상 주식을 보유하지 않았거나, 대상 주식 보유 수량이 부족한 경우
     */
    @Transactional
    public Response sellPlayerStock(StockOrder order) {
        String playerId = sessionHandler.getPlayerId();

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND, "Player not found"));

        Stock stock = stockRepository.findById(order.getStockId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND, "Stock not found"));

        PlayerStock playerStock = playerStockRepository.findByPlayerAndStock(player, stock)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND,
                        "Player does not own this stock"));

        if (order.getStockQuantity() > playerStock.getQuantity()) {
            throw new ResponseException(Error.INSUFFICIENT_FUNDS);
        }

        Integer newQuantity = playerStock.getQuantity() - order.getStockQuantity();
        if (newQuantity == 0) {
            playerStockRepository.delete(playerStock);
        } else {
            playerStock.setQuantity(newQuantity);
            playerStockRepository.save(playerStock);
        }

        double totalEarnings = stock.getStockPrice() * order.getStockQuantity();
        player.setPlayerMoney(player.getPlayerMoney() + totalEarnings);
        playerRepository.save(player);

        return new Response();
    }
}
