/**
 * PortfolioFormatter 인터페이스
 * 
 * 포트폴리오를 특정 형식 (String)으로 변환하는 규약을 정의한다.
 */
public interface PortfolioFormatter {
    /**
     * 포트폴리오를 지정된 형식으로 변환한다.
     * 
     * @param portfolio 변환할 포트폴리오
     * @return 변환된 문자열
     */
    String format(Portfolio portfolio);
}
