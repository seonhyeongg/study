import java.util.List;

/**
 * 프로그램의 시작점. 각 컴포넌트를 생성하고 전체 흐름을 제어합니다.
 */
public class SkalaStockMarket {
    private final PlayerRepository playerRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final StockView stockView;
    private Player player;

    public SkalaStockMarket() {
        playerRepository = new PlayerRepository();
        stockRepository = new StockRepository();
        stockService = new StockService(stockRepository);
        stockView = new StockView();
    }

    public void start() {
        stockRepository.loadStockList();
        playerRepository.loadPlayerList();

        initializePlayer();
        stockView.displayPlayerInfo(player);

        mainLoop();

        stockView.showMessage("프로그램을 종료합니다...Bye");
        stockView.close();
    }

    private void initializePlayer() {
        String playerId = stockView.promptForPlayerId();
        player = playerRepository.findPlayer(playerId);
        if (player == null) {
            int money = stockView.promptForInitialMoney();
            player = new Player(playerId, money);
            playerRepository.addPlayer(player);
        }
    }

    private void mainLoop() {
        boolean running = true;
        while (running) {
            int code = stockView.showMenuAndGetSelection();
            switch (code) {
                case 1:
                    stockView.displayPlayerInfo(player);
                    break;
                case 2:
                    buyStock();
                    break;
                case 3:
                    sellStock();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    stockView.showMessage("올바른 번호를 선택하세요.");
            }
        }
    }

    private void buyStock() {
        List<Stock> marketStocks = stockRepository.getAllStocks();
        stockView.displayStockList(marketStocks);

        int index = stockView.getStockIndexFromUser();
        if (index >= 0 && index < marketStocks.size()) {
            Stock selectedStock = marketStocks.get(index);
            int quantity = stockView.getQuantityFromUser();
            String result = stockService.buyStock(player, selectedStock, quantity);
            stockView.showMessage(result);
        } else {
            stockView.showMessage("ERROR: 잘못된 선택입니다.");
        }
    }

    private void sellStock() {
        stockView.showMessage("판매할 주식 번호를 선택하세요.");
        stockView.displayPlayerInfo(player);

        List<Stock> playerStocks = player.getPortfolio().getStocksAsList();
        if (playerStocks.isEmpty()) {
            stockView.showMessage("보유한 주식이 없습니다.");
            return;
        }

        int index = stockView.getStockIndexFromUser();
        if (index >= 0 && index < playerStocks.size()) {
            Stock selectedStock = playerStocks.get(index);
            int quantity = stockView.getQuantityFromUser();
            String result = stockService.sellStock(player, selectedStock, quantity);
            stockView.showMessage(result);
        } else {
            stockView.showMessage("ERROR: 잘못된 선택입니다.");
        }
    }
}
