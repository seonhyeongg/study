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

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final StockRepository stockRepository;
    private final PlayerRepository playerRepository;
    private final PlayerStockRepository playerStockRepository;
    private final SessionHandler sessionHandler;

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
