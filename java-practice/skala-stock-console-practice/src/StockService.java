/**
 * StockService 클래스
 * 
 * 플레이어의 주식 거래 로직을 처리한다.
 * 
 * - 매수: 플레이어 잔액 확인 -> 주식 구매 -> 포트폴리오 업데이트
 * 
 * - 매도: 보유 수량 확인 -> 시장 주식 가격 확인 -> 잔액 증가 및 포트폴리오 갱신
 * 
 * 모든 결과는 사용자 메시지로 반환된다.
 */
public class StockService {
    private final StockRepository stockRepository;

    /**
     * StockService 생성자
     * 
     * @param stockRepository 주식 저장소
     */
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * 플레이어가 주식을 매수한다.
     * 
     * @param player 매수하는 플레이어
     * @param stockToBuy 구매 대상 주식
     * @param quantity 매수 수량
     * @return 거래 결과 메시지
     */
    public String buyStock(Player player, Stock stockToBuy, int quantity) {
        if (quantity <= 0) {
            return "ERROR: 구매 수량은 1 이상이어야 합니다.";
        }

        int totalCost = stockToBuy.getPrice() * quantity;
        if (player.getMoney() < totalCost) {
            return String.format("ERROR: 자금이 부족합니다.\n필요 금액: %,d원\n보유 금액: %,d원", totalCost,
                    player.getMoney());
        }

        player.setMoney(player.getMoney() - totalCost);
        Stock purchasedStock = new Stock(stockToBuy.getName(), stockToBuy.getPrice(), quantity);
        player.getPortfolio().addOrUpdateStock(purchasedStock);

        return String.format("매수 완료: %s %d주 @ %,d원 (총액: %,d원)\n잔액: %,d원", purchasedStock.getName(),
                purchasedStock.getQuantity(), purchasedStock.getPrice(), totalCost,
                player.getMoney());
    }

    /**
     * 플레이어가 주식을 매도한다.
     * 
     * @param player 매도하는 플레이어
     * @param stockToSell 플레이어가 보유한 주식
     * @param quantity 매도 수량
     * @return 거래 결과 메시지
     */
    public String sellStock(Player player, Stock stockToSell, int quantity) {
        if (quantity <= 0) {
            return "ERROR: 판매 수량은 1 이상이어야 합니다.";
        }
        if (quantity > stockToSell.getQuantity()) {
            return "ERROR: 보유 수량이 부족합니다.";
        }

        Stock marketStock = stockRepository.findStock(stockToSell.getName());
        if (marketStock == null) {
            return "ERROR: 판매하려는 주식이 시장에 존재하지 않습니다.";
        }

        int earnings = marketStock.getPrice() * quantity;
        player.setMoney(player.getMoney() + earnings);
        Stock stockUpdate = new Stock(stockToSell.getName(), marketStock.getPrice(),
                stockToSell.getQuantity() - quantity);
        player.getPortfolio().updateStock(stockUpdate);

        return String.format("매도 완료: %s %d주 @ %,d원 (총액: %,d원)\n잔액: %,d원", stockUpdate.getName(),
                stockUpdate.getQuantity(), stockUpdate.getPrice(), earnings, player.getMoney());
    }
}
