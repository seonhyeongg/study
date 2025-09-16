import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PlayerRespository 클래스
 * 
 * 플레이어 데이터를 파일에 저장하고 불러온다.
 * 
 * 내부적으로 Map을 사용하여 Player 객체를 메모리에 캐싱한다.
 * 
 * 데이터 저장 위치: {@code data/players.txt}
 */
public class PlayerRepository {
    private static final String PLAYER_FILE = "data/players.txt";
    private final Map<String, Player> playerMap = new LinkedHashMap<>();
    private final PlayerMapper mapper = new PlayerMapper();

    /**
     * 파일에서 플레이어 목록을 로드하여 메모리에 적재한다.
     * 
     * 잘못된 형식의 데이터는 무시되며, 파일이 존재하지 않거나 읽기 실패 시 오류 메시지를 출력한다.
     */
    public void loadPlayerList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PLAYER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = mapper.fromLine(line);
                if (player != null) {
                    playerMap.put(player.getId(), player);
                }
            }
        } catch (IOException e) {
            System.out.println("플레이어 목록 로드 실패: " + e.getMessage());
        }
    }

    /**
     * 현재 메모리에 있는 플레이어 목록을 파일에 저장한다.
     * 
     * 기존 파일 내용은 덮어쓰며, 저장 실패 시 오류 메시지를 출력한다.
     */
    public void savePlayerList() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_FILE))) {
            for (Player player : playerMap.values()) {
                writer.write(mapper.toLine(player));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("플레이어 정보 저장 실패: " + e.getMessage());
        }

    }

    /**
     * 플레이어 ID로 Player를 조회한다.
     * 
     * @param id 조회할 플레이어 ID
     * @return Player 객체, 존재하지 않으면 null
     */
    public Player findPlayer(String id) {
        return playerMap.get(id);
    }

    /**
     * 새로운 플레이어르 저장소에 추가한다.
     * 
     * 동일 ID가 이미 존재하면 덮어쓴다.
     * 
     * @param player 추가할 Player 객체
     */
    public void addPlayer(Player player) {
        playerMap.put(player.getId(), player);
    }
}
