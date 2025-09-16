import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Portfolio 클래스
 * 
 * 여러 종목 (Stock)을 관리하는 주식 포트폴리오 역할을 한다.
 * 
 * 내부적으로 LinkedHashMap을 사용하여 종목명을 키로 관리한다. 순서를 유지하면서도 빠른 조회/업데이트가 가능하다.
 */
public class Portfolio {

    // 종목명 (String) -> Stock 객체 매핑
    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    /**
     * 포트폴리오에 새로운 종목을 추가하거나, 이미 존재하는 경우 가격과 수량을 갱신한다.
     * 
     * @param stockToAdd 추가 또는 갱신할 종목
     */
    public void addOrUpdateStock(Stock stockToAdd) {
        stocks.compute(stockToAdd.getName(), (name, existingStock) -> {
            if (existingStock == null) {
                return stockToAdd;
            } else {
                existingStock.setPrice(stockToAdd.getPrice());
                existingStock.setQuantity(existingStock.getQuantity() + stockToAdd.getQuantity());
                return existingStock;
            }
        });
    }

    /**
     * 기존 종목의 가격과 수량을 새로운 값으로 갱신한다.
     * 
     * @param stockToUpdate 갱신할 종목
     */
    public void updateStock(Stock stockToUpdate) {

        Stock existingStock = stocks.get(stockToUpdate.getName());
        if (existingStock != null) {
            existingStock.setPrice(stockToUpdate.getPrice());
            existingStock.setQuantity(stockToUpdate.getQuantity());

            // 보유량이 0 이하인 경우 포트폴리오에서 제거
            if (existingStock.getQuantity() <= 0) {
                stocks.remove(existingStock.getName());
            }
        }
    }

    /**
     * 종목명을 기준으로 종목을 조회한다.
     * 
     * @param name 종목명
     * @return 종목이 존재하면 Stock, 없으면 Optional.empty()
     */
    public Optional<Stock> findStockByName(String name) {
        return Optional.ofNullable(stocks.get(name));
    }

    /**
     * 포트폴리오에 포함된 모든 종목을 반환한다.
     * 
     * @return 전체 종목 Collection
     */
    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }

    /**
     * 포트폴리오에 포함된 모든 종목을 List로 반환한다.
     * 
     * @return 전체 종목 List
     */
    public List<Stock> getStocksAsList() {
        return new ArrayList<>(stocks.values());
    }
}
