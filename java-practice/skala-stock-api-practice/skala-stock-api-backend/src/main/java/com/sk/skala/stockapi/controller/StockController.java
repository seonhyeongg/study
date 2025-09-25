package com.sk.skala.stockapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sk.skala.stockapi.data.common.Response;
import com.sk.skala.stockapi.data.table.Stock;
import com.sk.skala.stockapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


/**
 * StockController
 * 
 * 주식 관련 API EndPoint를 제공하는 REST Controller
 */
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    /**
     * 전체 주식 목록 조회
     * 
     * @param offset 페이지 인덱스
     * @param count 페이지 크기
     * @return 주식 목록과 페이징 정보
     */
    @GetMapping("/list")
    public Response getAllStocks(@RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer count) {
        return stockService.getAllStocks(offset, count);
    }

    /**
     * 주식 단건 조회
     * 
     * @param id 주식 ID
     * @return 해당 주식 정보
     */
    @GetMapping("/{id}")
    public Response getStockById(@PathVariable Long id) {
        return stockService.getStockById(id);
    }

    /**
     * 주식 생성
     * 
     * @param stock 생성할 주식 정보
     * @return 처리 결과
     */
    @PostMapping
    public Response createStock(@RequestBody Stock stock) {
        return stockService.createStock(stock);
    }

    /**
     * 주식 수정
     * 
     * @param stock 수정할 주식 정보
     * @return 처리 결과
     */
    @PutMapping
    public Response updateStock(@RequestBody Stock stock) {
        return stockService.updateStock(stock);
    }

    /**
     * 주식 삭제
     * 
     * @param stock 삭제할 주식 정보
     * @return 처리 결과
     */
    @DeleteMapping
    public Response deletedStock(@RequestBody Stock stock) {
        return stockService.deleteStock(stock);
    }
}
