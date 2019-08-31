package network;

import map.GameMap;
import util.Tuple;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ServerWorker extends Thread {
	private final Server server;

	private final Socket client;

	private final int timelimit;

	private DataInputStream is;

	private DataOutputStream os;

	private final byte playerID;

	private final boolean silent;

	private long startTime;

	ServerWorker(Server server, Socket client, byte id, boolean silent, int timelimit) {
		this.server = server;
		this.client = client;
		this.playerID = id;
		this.silent = silent;
		this.timelimit = timelimit;
	}

	@Override
	public void run() {
		try {
			os = new DataOutputStream(client.getOutputStream());
			is = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleMove() throws IOException {
		requestMove();
		startTime = System.nanoTime();
		Tuple coord = getMoveReponse();

		// apply move
		if (!GameMap.getInstance().isMoveValid(coord.x, coord.y, (char) ('0' + playerID))) {
			if (!silent) {
				System.err.println("Invalid Move");
			}
			disqualify(playerID);
		}
		if (!silent) {
			System.out.println("Received move: " + coord + " from Player " + playerID + ".");
		}
		GameMap.getInstance().executeMove(coord.x, coord.y,  (char) ('0' + playerID));

		// broadcast move to all other players
		for (ServerWorker sw : server.getWorker()) {
			sw.getOs().writeByte(5);  // code
			sw.getOs().writeInt(5);  // length
			sw.getOs().writeShort(coord.x);
			sw.getOs().writeShort(coord.y);
			sw.getOs().writeByte(playerID);
			sw.getOs().flush();
		}
	}

	private void disqualify(byte player) throws IOException {
		os.writeByte(6);  // code
		os.writeInt(1);  // length
		os.writeByte(player);  // max depth limit; 0 = unlimited
		os.flush();

		// announce winner & game end
		byte id = playerID == 1 ? (byte) 2 : (byte) 1; // if 1 gets disqualified, 2 wins and vice-versa
		server.endGame((char) ('0' + id));
		System.exit(0);
	}

	private Tuple getMoveReponse() throws IOException {
		try {
			if (is.readByte() != 4) { // code (4)
				if (!silent) {
					System.err.println("Sent wrong code");
				}
				disqualify(playerID);
			}

			// if response took more than `timelimit` time, disqualify player
			if ((System.nanoTime() - startTime) / 1000000 > timelimit) {
				disqualify(playerID);
			}

			if (is.readInt() != 4) {	// 4 Byte
				if (!silent) {
					System.err.println("Sent too much data");
				}
				disqualify(playerID);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Tuple(is.readShort(), is.readShort());
	}

	private void requestMove() throws IOException {
		os.writeByte(3);  // code
		os.writeInt(5);  // length
		os.writeInt(timelimit);  // time limit
		os.writeByte(0);  // max depth limit; 0 = unlimited
		os.flush();
	}

	public void sendPlayerNumber() throws IOException {
		// send map
		os.writeByte(2);
		os.writeInt(1);
		os.writeByte(playerID);
		os.flush();
	}

	public void announceEnd() throws IOException {
		os.writeByte(7);  // code
		os.writeInt(0);  // length
		os.flush();

	}

	public void sendString(byte code, int payloadLength, String payload) throws IOException {
		os.writeByte(code);
		os.writeInt(payloadLength);
		os.writeBytes(payload);
		os.flush();
	}

	public void close() {
		server.getWorker().remove(this);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getPlayerID() {
		return playerID;
	}


	private DataOutputStream getOs() {
		return os;
	}
}
