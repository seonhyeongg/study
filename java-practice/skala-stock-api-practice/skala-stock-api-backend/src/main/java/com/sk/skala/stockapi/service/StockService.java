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


/**
 * StockService
 * 
 * Stock 엔터티에 대한 CRUD를 담당하는 서비스 계층
 * 
 * Controller에서 넘어온 파라미터의 유효성을 검증하고, 도메인 정책을 적용한 뒤, Repository를 통해 영속성 계층에 접근함
 */
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    /**
     * 전체 주식 목록 페이지 단위 조회
     * 
     * @param offset 페이지 인덱스 (0-base)
     * @param count 페이지 크기
     * @return 페이징 메타데이터의 목록을 담은 Response
     * @throws ParameterException count <=0 또는 offset < 0인 경우
     */
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

    /**
     * ID로 주식 단건 조회
     * 
     * @param id 주식 ID
     * @return 조회된 주식 데이터를 담은 Response
     * @throws ParameterExeption id가 null인 경우
     * @throws ResponseException 데이터가 없을 경우
     */
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

    /**
     * 주식 생성
     * 
     * @param stock 생성할 주식 엔터티
     * @return 빈 본문을 가진 성공 Response
     * @throws ParameterException stockName이 공백이거나 stockPrice <=0인 경우
     * @throws ResponseException 동일 이름의 주식이 이미 존재하는 경우
     */
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

    /**
     * 주식 수정
     * 
     * @param stock 수정할 주식 엔터티
     * @return 빈 본문을 가진 성공 Response
     * @throws ParameterException stockName이 공백이거나 stockPrice <=0인 경우
     * @throws ResponseException 대상 주식이 존재하지 않는 경우
     */
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

    /**
     * 주식 삭제
     * 
     * @param stock 삭제할 주식
     * @return 빈 본문을 가진 성공 Response
     * @throws ParameterException id가 null인 경우
     * @throws ResponseException 대상 주식이 존재하지 않는 경우
     */
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
