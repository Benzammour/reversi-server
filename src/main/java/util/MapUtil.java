package util;

import map.GameMap;
import map.GenericMap;

import java.util.HashSet;
import java.util.Set;

public class MapUtil {

    private static final GameMap gameMap = GameMap.getInstance();

    public static boolean isCoordinateInMap(int x, int y) {
        return x >= 0 && x < gameMap.getMapWidth() && y >= 0 && y < gameMap.getMapHeight();
    }

    public static boolean isDifferentPlayerStone(int x, int y, int player) {
        return MapUtil.isOccupied(gameMap.getMap()[y][x]) && gameMap.getMap()[y][x] != player;
    }

    public static boolean isPlayerTile(char type) {
        return '1' <= type && type <= '2';
    }

    private static boolean isOccupied(char type) {
        return isPlayerTile(type);
    }

    public static boolean isTileHole(char type) {
        return type == '-';
    }

    public static boolean isTileFree(char type) { return type == '0'; }

    public static char maxNumberOfStones(GenericMap map) {
        int max = 0;
        int result = 0;
        int[] stones = map.getNumberOfStones();

        for (int i = 1; i <= 2; i++) {  // 2 = number of players
            if (stones[i] > max) {
                max = stones[i];
                result = i;
            }
        }

        return (char) ('0' + result);
    }

    public static String renderMap(char[][] map) {
        StringBuilder sb = new StringBuilder();

        for (char[] tA : map) {
            for (char t : tA) {
                sb.append(t).append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    public static Set<Triplet> getMovesForPlayer(GenericMap map, char player) {
        Set<Triplet> moves = new HashSet<>();
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[0].length; j++) {
                if (map.isMoveValid(j, i, player)) {
                    moves.add(new Triplet(j, i, 0));
                }
            }
        }

        return moves;
    }
}