package util;

import map.GameMap;
import map.GenericMap;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marc Luque on 24.04.2019.
 */
public class MapUtil {

    private static GameMap gameMap = GameMap.getInstance();

    public static boolean isCoordinateInMap(int x, int y) {
        return x >= 0 && x < gameMap.getMapWidth() && y >= 0 && y < gameMap.getMapHeight();
    }

    public static boolean isDifferentPlayerStone(int x, int y, int player) {
        return MapUtil.isOccupied(gameMap.getMap()[y][x]) && gameMap.getMap()[y][x] != player;
    }

    public static boolean isPlayerTile(char type) {
        return '1' <= type && type <= '8';
    }

    public static boolean isTileExpansion(char type) {
        return type == 'x';
    }

    /**
     * Considers tiles with expansion stones as occupied according to the specification.
     *
     * @return whether the current tile is occupied
     */
    public static boolean isOccupied(char type) {
        // Check whether type is expansion stone OR character from 1 to 8, using ascii values
        return isTileExpansion(type) || isPlayerTile(type);
    }

    public static boolean isTileHole(char type) {
        return type == '-';
    }

    public static boolean isTileFree(char type) { return type == '0'; }

    public static boolean isTileSpecial(char type) {
        return type == 'c' || type == 'b' || type == 'i';
    }

    public static boolean isTileCaptureableAndInMap(int x, int y) {
        // Captureable means whether it is theoretically possible to place a stone onto
        // that tile. Doesn't consider all possible cases due to performance issues
        return isCoordinateInMap(x, y) && !isTileHole(gameMap.getMap()[y][x]);
    }

    public static Triplet[] isTileCorner(int x, int y) {
        if(!isTileCaptureableAndInMap(x,y)) {
            return null;
        }

        // Check every pair of neighbours around current tile (i,j)
        Triplet transition;
        Tuple neighbour;
        Tuple oppositeNeighbour;
        // Maximum amount of neighbours is 4
        Triplet[] neighbours = new Triplet[4];
        int neighbourCounter = 0;

        // k in [0,3] because opposite neighbours are also checked
        for (int k = 0; k < 4; k++) {
            // Checks whether a path via a transition can be enclosed
            // Check the potential neighbour in direction k
            neighbour = new Tuple(x + GameMap.getCORNERS()[k][0], y + GameMap.getCORNERS()[k][1]);

            // Check the potential opposite neighbour in direction k + 4
            oppositeNeighbour = new Tuple(x + GameMap.getCORNERS()[k + 4][0], y + GameMap.getCORNERS()[k + 4][1]);

            // Returns null if there is a pair of neighbours around the tile (x,y) in opposite directions
            if (MapUtil.isTileCaptureableAndInMap(neighbour.getX(), neighbour.getY())
                    && MapUtil.isTileCaptureableAndInMap(oppositeNeighbour.getX(), oppositeNeighbour.getY())) {
                return null;
            }

            if (MapUtil.isTileCaptureableAndInMap(neighbour.getX(), neighbour.getY())) {
                neighbours[neighbourCounter++] = new Triplet(neighbour.getX(), neighbour.getY(), k);
            } else if (MapUtil.isTileCaptureableAndInMap(oppositeNeighbour.getX(), oppositeNeighbour.getY())) {
                neighbours[neighbourCounter++] = new Triplet(oppositeNeighbour.getX(), oppositeNeighbour.getY(), k + 4);
            }
        }

        // Returns the neighbours when no such pair is found
        return neighbours;
    }

    /**
     *
     * @param map the map of the specific game state
     * @param player
     * @return the number of the player with the maximum amount of stones
     */
    public static char maxNumberOfStones(GenericMap map, char player) {
        int max = 0;
        int result = Character.getNumericValue(player);
        int[] nStones = map.getNumberOfStones();

        for (int i = 1; i <= 2; i++) { // 2 = number of players
            if (nStones[i] > max) {
                max = nStones[i];
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

    public static Set<Triplet> getMovesForPlayer(boolean[] inTime, GenericMap map, char player, boolean allowOverrideStones) {
        Set<Triplet> moves = new HashSet<>();
        for (int i = 0; i < map.getMap().length; i++) {
            for (int j = 0; j < map.getMap()[0].length; j++) {
                if (!inTime[0]) {
                    return moves;
                } else if (map.isMoveValidExtended(j, i, player, true)) {
                    moves.add(new Triplet(j, i, 0));
                }
            }
        }

        return moves;
    }
}