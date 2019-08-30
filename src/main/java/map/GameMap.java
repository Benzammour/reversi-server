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

		// GameMap settings

		String[] s = mapLines[3].split(" ");
		mapHeight = Short.parseShort(s[0]);
		mapWidth = Short.parseShort(s[1]);

		numberOfStones = new int[3]; // 2 Players; one as a buffer for easier access

		// Game map
		map = new char[mapHeight][mapWidth];
		for (short i = 0; i < mapHeight; i++) {
			char[] line = mapLines[4 + i].replaceAll(" ", "").toCharArray();
			for (int j = 0; j < line.length; j++) {
				map[i][j] = line[j];

				/* NUMBER OF STONES OF PLAYER */
				if (MapUtil.isPlayerTile(map[i][j])) {
					numberOfStones[Character.getNumericValue(map[i][j])]++;
				}
			}
		}
	}

	/**
	 * Reads in valid playing fields and represents them using this class.
	 * The data structure main.java.map (2D-Array and Graph) is supposed to represent the actual playing tiles.
	 *
	 * @param mapName specifies the name of the map that has to be located in res/maps
	 */
	public void generateStringFromMapFile(String mapName) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(String.format("maps/%s", mapName))))) {
			generateMapFromString(br.lines().collect(Collectors.joining("\n")));
		} catch (IOException e) {
			System.err.println("Couldn't read file.");
			e.printStackTrace();
		}
	}

	/* ----------- Getter/Setter ----------- */

	public void setPlayer(int player) {
		this.player = (char) ('0' + player);
	}

	public char getPlayer() {
		return player;
	}
}