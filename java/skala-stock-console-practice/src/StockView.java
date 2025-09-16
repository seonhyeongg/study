import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * StockView 클래스
 * 
 * 콘솔 기반 UI 역할을 한다.
 * 
 * 사용자 입력을 받고, 플레이어/주식 정보를 화면에 출력한다.
 */
public class StockView {
    private final Scanner scanner;

    /**
     * 기본 생성자
     * 
     * Scanner를 UTF-8로 초기화한다.
     */
    public StockView() {
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    /**
     * 플레이어 ID 입력을 요청한다.
     * 
     * @return 입력받은 플레이어 ID
     */
    public String promptForPlayerId() {
        System.out.print("\n플레이어 ID를 입력하세요: ");
        return scanner.nextLine();
    }

    /**
     * 초기 투자금 입력을 요청한다.
     * 
     * @return 입력받은 초기 투자금
     */
    public int promptForInitialMoney() {
        System.out.print("초기 투자금을 입력하세요: ");
        int money = scanner.nextInt();
        scanner.nextLine();
        return money;
    }

    /**
     * 메인 메뉴를 출력하고 사용자 선택을 입력받는다.
     * 
     * @return 선택 번호
     */
    public int showMenuAndGetSelection() {
        System.out.println("\n======= 스칼라 주식 시장 =======");
        System.out.println("  1. 나의 자산 확인");
        System.out.println("  2. 주식 구매");
        System.out.println("  3. 주식 판매");
        System.out.println("  0. 프로그램 종료");
        System.out.println("=============================");
        System.out.print("선택: ");
        int selection = scanner.nextInt();
        scanner.nextLine();
        return selection;
    }

    /**
     * 플레이어 정보를 화면에 출력한다.
     * 
     * @param player 플레이어 객체
     */
    public void displayPlayerInfo(Player player) {
        System.out.println("\n======= 플레이어 정보 =======");
        System.out.println("  ID: " + player.getId());
        System.out.println("  보유 현금: " + String.format("%,d", player.getMoney()));
        System.out.println("-----------------------------");
        System.out.println("  보유 주식 목록:");
        PortfolioFormatter formatter = new MenuPortfolioFormatter();
        String formattedStocks = formatter.format(player.getPortfolio());
        if (formattedStocks.isEmpty()) {
            System.out.println("    (보유 주식이 없습니다)");
        } else {
            System.out.print(formattedStocks);
        }
        System.out.println("=============================");
    }

    /**
     * 현재 시장 주식 목록을 화면에 출력한다.
     * 
     * @param stockList 시장의 모든 주식 리스트
     */
    public void displayStockList(List<Stock> stockList) {
        System.out.println("\n======= 현재 주식 시세 =======");
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            System.out.println("  " + (i + 1) + ". " + stock.getName() + " - "
                    + String.format("%,d", stock.getPrice()) + "원");
        }
        System.out.println("=============================");
    }

    /**
     * 사용자로부터 주식 번호를 입력받는다.
     * 
     * @return 선택한 주식 인덱스
     */
    public int getStockIndexFromUser() {
        System.out.print("주식 번호를 선택하세요: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();
        return index;
    }

    /**
     * 사용자로부터 거래 수량을 입력받는다.
     * 
     * @return 입력한 수량
     */
    public int getQuantityFromUser() {
        System.out.print("수량을 입력하세요: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        return quantity;
    }

    /**
     * 메시지를 화면에 출력한다.
     * 
     * @param message 출력할 메시지
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Scanner 자원을 해제한다.
     */
    public void close() {
        scanner.close();
    }
}
