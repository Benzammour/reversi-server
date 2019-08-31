package map;

import util.MapUtil;
import util.Triplet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by benzammour on April, 2019
 */
public class GameMap extends GenericMap {

	private static GameMap instance;

	private char player;

	private GameMap() {}

	public static GameMap getInstance() {
		return (instance == null) ? instance = new GameMap() : instance;
	}

	public void generateMapFromString(String mapString) {
		String[] mapLines = mapString.lines().toArray(String[]::new);

		System.out.println(mapString);

		// GameMap settings
		String[] s = mapLines[0].split(" ");
		mapHeight = Short.parseShort(s[0]);
		mapWidth = Short.parseShort(s[1]);

		numberOfStones = new int[3]; // 2 Players; one as a buffer for easier access

		// Game map
		map = new char[mapHeight][mapWidth];
		for (short i = 0; i < mapHeight; i++) {
			char[] line = mapLines[1 + i].replaceAll(" ", "").toCharArray();
			for (int j = 0; j < line.length; j++) {
				map[i][j] = line[j];

				/* NUMBER OF STONES OF PLAYER */
				if (MapUtil.isPlayerTile(map[i][j])) {
					numberOfStones[Character.getNumericValue(map[i][j])]++;
				}
			}
		}
	}
}