/**
 * MenuPortfolioFormatter 클래스
 * 
 * 포트폴리오를 메뉴 형식 (Ordered List)으로 출력하는 구현체이다.
 */
public class MenuPortfolioFormatter implements PortfolioFormatter {

    /**
     * 포트폴리오를 메뉴 형식으로 변환한다.
     * 
     * @param portfolio 변환할 포트폴리오
     * @return 번호가 붙은 문자열 목록
     */
    @Override
    public String format(Portfolio portfolio) {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Stock stock : portfolio.getAllStocks()) {
            sb.append(index++).append(". ").append(stock.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
