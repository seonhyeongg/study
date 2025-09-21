import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * StockRepository 클래스
 * 
 * 주식 데이터를 파일에서 불러오거나, 기본 주식 목록을 초기화하여 관리한다.
 * 
 * 내부적으로 ArrayList를 사용하며, 주식 목록 조회와 단일 주식 검색 기능을 제공한다.
 * 
 * 데이터 저장 위치: {@code data/stocks.txt}
 */
public class StockRepository {
    private static final String STOCK_FILE = "data/stocks.txt";
    private final List<Stock> stockList = new ArrayList<>();
    private final StockMapper mapper = new StockMapper();

    /**
     * 파일에서 주식 목록을 불러온다.
     * 
     * 잘못된 형식의 데이터는 무시되며, 파일이 존재하지 않거나 읽기 실패 시 기본 주식 목록을 생성한다.
     */
    public void loadStockList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Stock stock = mapper.fromLine(line);
                if (stock != null) {
                    stockList.add(stock);
                }
            }
        } catch (IOException e) {
            System.out.println("주식 목록 로드 실패: " + e.getMessage());
            System.out.println("기본 주식 목록 생성");
            initializeDefaultStocks();
        }
    }

    /**
     * 기본 주식 목록을 초기화한다.
     * 
     * 파일이 존재하지 않거나 읽기 실패 시 fallback 데이터로 사용한다.
     */
    private void initializeDefaultStocks() {
        stockList.add(new Stock("TechCorp", 152, 0));
        stockList.add(new Stock("GreenEnergy", 88, 0));
        stockList.add(new Stock("HealthPlus", 210, 0));
        stockList.add(new Stock("BioGen", 75, 0));
    }

    /**
     * 현재 보유한 모든 주식 목록을 반환한다.
     * 
     * @return 새로운 ArrayList로 감싼 주식 목록 (외부에서 변경 불가)
     */
    public List<Stock> getAllStocks() {
        return new ArrayList<>(stockList);
    }

    /**
     * 인덱스로 주식을 검색한다.
     * 
     * @param index 주식 리스트 인덱스
     * @return 주식 객체, 범위를 벗어나면 null
     */
    public Stock findStock(int index) {
        if (index >= 0 && index < stockList.size())
            return stockList.get(index);
        return null;
    }

    /**
     * 이름으로 주식을 검색한다.
     * 
     * @param name 주식 이름
     * @return 해당 이름의 주식, 존재하지 않으면 null
     */
    public Stock findStock(String name) {
        return stockList.stream().filter(stock -> stock.getName().equals(name)).findFirst()
                .orElse(null);
    }
}
