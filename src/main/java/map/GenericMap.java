package map;

import util.MapUtil;
import util.Triplet;

import java.util.*;

public class GenericMap implements Cloneable {
    char[][] map;

    static int mapHeight;

    static int mapWidth;

    int[] numberOfStones; // used to determine winner

    private final Set<Triplet> paths = new HashSet<>();

    // x = x-coord; y = x-coord; d = direction;
    private static final int[][] CORNERS = {{0, -1, 0}, {1, -1, 1}, {1, 0, 2},
            {1, 1, 3}, {0, 1, 4}, {-1, 1, 5}, {-1, 0, 6}, {-1, -1, 7}};

    public boolean isMoveValid(int x, int y, char player) {
        paths.clear();

        if (!MapUtil.isCoordinateInMap(x, y) || MapUtil.isTileHole(map[y][x]) || !MapUtil.isTileFree(GameMap.getInstance().getMap()[y][x])) {
            return false;
        }

        // Check whether the coordinate encloses a path
        // Therefore check whether the coordinate neighbours a different stone
        int currentX;
        int currentY;
        int direction;
        Set<Triplet> path;
        boolean result = false;

        for (int i = 0; i < 8; i++) {
            paths.add(new Triplet(x, y, i));
        }

        for (int i = 0; i < 8; i++) {
            path = new HashSet<>();

            currentX = x + CORNERS[i][0];
            currentY = y + CORNERS[i][1];
            direction = CORNERS[i][2];


            while (MapUtil.isCoordinateInMap(currentX, currentY) && MapUtil.isDifferentPlayerStone(currentX, currentY, player)
                    && !path.contains(new Triplet(currentX, currentY, direction))
                    && !paths.contains(new Triplet(currentX, currentY, direction))) {
                path.add(new Triplet(currentX, currentY, direction));

                // Move in the specified direction, while the next stone still is another player's stone
                currentX += CORNERS[direction][0];
                currentY += CORNERS[direction][1];
            }

            // If stone that comes now is of player, we have a valid path
            if (path.size() > 0 && MapUtil.isCoordinateInMap(currentX, currentY) && map[currentY][currentX] == player &&
                    (x != currentX || y != currentY)) {
                result = true;

                // path is valid, add path
                paths.addAll(path);
            } else {
                // Clear for next iteration of loop
                path.clear();
            }
        }

        return result;
    }

    public void executeMove(int x, int y, char player) {
        int playerId = Character.getNumericValue(player);

        for (int i = 1; i < 8; i++) {
            paths.remove(new Triplet(x, y, i));
        }

        paths.forEach(tuple -> {
            char tile = map[tuple.getY()][tuple.getX()];

            // If a special tile is encountered, ignore, as it's dealt with separately
            if (MapUtil.isPlayerTile(tile)) {
                numberOfStones[Character.getNumericValue(tile)]--;
            }

            numberOfStones[playerId]++;
            map[tuple.getY()][tuple.getX()] = player;
        });

        paths.clear();
    }

    // -----------------------------------------------  Getter/Setter
    public char[][] getMap() {
        return map;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int[] getNumberOfStones() {
        return numberOfStones;
    }

    // -----------------------------------------------  Object Overrides
    @Override
    public String toString() {
        return MapUtil.renderMap(map);
    }
}