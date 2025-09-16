/**
 * Player 클래스
 * 
 * 플레이어의 ID, 보유 자산, 포트폴리오를 관리한다.
 * 
 * 한 명의 Player는 고유한 ID를 가지고 있으며, 주식 거래 시 Portfolio를 통해 종목을 관리한다.
 */
public class Player {
    private String id;
    private int money;
    private final Portfolio portfolio;

    /**
     * Player 객체를 생성한다.
     * 
     * @param id 플레이어 ID
     * @param initialMoney 초기 보유 자산
     */
    public Player(String id, int initialMoney) {
        this.id = id;
        this.money = initialMoney;
        this.portfolio = new Portfolio();
    }

    // ----- Getters ----------

    public String getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    // ----- Setters ----------

    public void setId(String id) {
        this.id = id;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
