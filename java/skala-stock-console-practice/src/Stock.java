/**
 * Stock 클래스
 * 
 * 주식의 이름, 가격, 수량을 관리하는 엔터티 역할을 한다.
 */
public class Stock {
    private String name;
    private int price;
    private int quantity;

    /**
     * Stock 객체를 생성한다.
     * 
     * @param name 주식 이름
     * @param price 주식 가격
     * @param quantity 보유 수량
     */
    public Stock(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // ----- Getters ----------

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // ----- Setters ----------

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Stock 객체의 정보를 문자열로 반환한다.
     * 
     * @return "종목: 이름, 현재가: 가격, 보유수량: 수량" 형식의 문자열
     */
    @Override
    public String toString() {
        return "종목: " + name + ", 현재가: " + price + ", 보유수량: " + quantity;
    }
}
