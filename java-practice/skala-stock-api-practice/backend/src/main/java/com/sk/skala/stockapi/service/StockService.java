package com.sk.skala.stockapi.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sk.skala.stockapi.config.Error;
import com.sk.skala.stockapi.data.common.PagedList;
import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.table.Stock;
import com.sk.skala.stockapi.exception.ParameterException;
import com.sk.skala.stockapi.exception.ResponseException;
import com.sk.skala.stockapi.repository.StockRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Response getAllStocks(Integer offset, Integer count) {
        if (count <= 0 || offset < 0) {
            throw new ParameterException("offset", "count");
        }
        Pageable pageable = PageRequest.of(offset, count, Sort.by("id"));
        Page<Stock> paged = stockRepository.findAll(pageable);

        PagedList pagedList = new PagedList();
        pagedList.setTotal(paged.getTotalElements());
        pagedList.setOffset(offset);
        pagedList.setCount(paged.getNumberOfElements());
        pagedList.setList(paged.getContent());

        Response response = new Response();
        response.setBody(pagedList);

        return response;
    }

    public Response getStockById(Long id) {
        if (id == null) {
            throw new ParameterException("id");
        }

        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        Response response = new Response();
        response.setBody(stock);

        return response;
    }

    public Response createStock(Stock stock) {
        if (stock.getStockName().isBlank() || stock.getStockPrice() <= 0) {
            throw new ParameterException("stockName", "stockPrice");
        }

        Optional<Stock> option = stockRepository.findByStockName(stock.getStockName());
        if (option.isPresent()) {
            throw new ResponseException(Error.DATA_DUPLICATED);
        }

        stock.setId(0L);
        stockRepository.save(stock);

        return new Response();
    }

    public Response updateStock(Stock stock) {
        if (stock.getStockName().isBlank() || stock.getStockPrice() <= 0) {
            throw new ParameterException("stockName", "stockPrice");
        }

        Optional<Stock> option = stockRepository.findById(stock.getId());
        if (option.isEmpty()) {
            throw new ResponseException(Error.DATA_NOT_FOUND);
        }

        stockRepository.save(stock);

        return new Response();
    }

    public Response deleteStock(Stock stock) {
        if (stock.getId() == null) {
            throw new ParameterException("id");
        }

        Optional<Stock> option = stockRepository.findById(stock.getId());
        if (option.isEmpty()) {
            throw new ResponseException(Error.DATA_NOT_FOUND);
        }

        stockRepository.deleteById(stock.getId());

        return new Response();
    }
}
