/**
 * StockMapper 클래스
 * 
 * Stock 객체를 문자열로 직렬화하거나, 문자열을 Stock 객체로 역직렬화한다.
 * 
 * 데이터 포맷 규칙: name, price
 * 
 * quantity는 TXT에 포함되지 않으므로, fromLine 시 기본값 (0)으로 초기화된다.
 */
public class StockMapper {
    /**
     * 문자열을 파싱하여 Stock 객체를 생성한다.
     * 
     * @param line "name, price" 형식의 문자열
     * @return Stock 객체 (형식 오류 시 null 반환)
     */
    public Stock fromLine(String line) {
        String[] fields = line.split(",");
        if (fields.length == 2) {
            String name = fields[0];
            int price = Integer.parseInt(fields[1]);

            return new Stock(name, price, 0);
        }
        return null;
    }

    /**
     * Stock 객체를 문자열로 직렬화한다.
     * 
     * @param stock 변환할 Stock 객체
     * @return "name, price" 형식의 문자열
     */
    public String toLine(Stock stock) {
        return stock.getName() + "," + stock.getPrice();
    }
}
