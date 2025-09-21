import java.util.Collection;
import java.util.stream.Collectors;

/**
 * PlayerMapper 클래스
 * 
 * Player 객체를 문자열로 직렬화하거나, 문자열을 Player 객체로 역직렬화한다.
 * 
 * 데이터 포맷 규칙: id, money, stockName:price:quantity|stockName:price:quantity ...
 */
public class PlayerMapper {

    /**
     * 문자열을 파싱하여 Player 객체를 생성한다.
     * 
     * @param line "id, money, stocks..." 형식의 문자열
     * @return Player 객체 (형식 오류 시 null 반환)
     */
    public Player fromLine(String line) {
        String[] fields = line.split(",", 3);
        if (fields.length < 1)
            return null;

        String id = fields[0];
        int money = Integer.parseInt(fields[1]);
        Player player = new Player(id, money);

        if (fields.length > 2 && !fields[2].isEmpty()) {
            String[] stockData = fields[2].split("\\|");

            for (String s : stockData) {
                String[] stockProps = s.split(":");

                if (stockProps.length == 3) {
                    String name = stockProps[0];
                    int price = Integer.parseInt(stockProps[1]);
                    int quantity = Integer.parseInt(stockProps[2]);

                    player.getPortfolio().addOrUpdateStock(new Stock(name, price, quantity));
                }
            }
        }
        return player;
    }

    /**
     * Player 객체를 문자열로 직렬화한다.
     * 
     * @param player 변환할 Player 객체
     * @return "id, money, stocks..." 형식의 문자열
     */
    public String toLine(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append(player.getId()).append(",").append(player.getMoney());

        Collection<Stock> stocks = player.getPortfolio().getAllStocks();

        if (!stocks.isEmpty()) {
            sb.append(",");
            String stockData = stocks.stream()
                    .map(stock -> String.join(":", stock.getName(),
                            String.valueOf(stock.getPrice()), String.valueOf(stock.getQuantity())))
                    .collect(Collectors.joining("|"));
            sb.append(stockData);
        }

        return sb.toString();
    }
}
