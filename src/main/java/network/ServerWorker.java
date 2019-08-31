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

	private DataInputStream is;

	private DataOutputStream os;

	private final byte playerID;

	private final boolean silent;

	ServerWorker(Server server, Socket client, byte id, boolean silent) {
		this.server = server;
		this.client = client;
		this.playerID = id;
		this.silent = silent;
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
		Tuple coord = getMoveReponse();

		// move
		if (!GameMap.getInstance().isMoveValid(coord.x, coord.y, (char) ('0' + playerID))) {
        	// TODO: Disqualify
			if (!silent) {
				System.err.println("doesnt work mate");
			}
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
		}
	}

	private Tuple getMoveReponse() throws IOException {
		try {
			if (is.readByte() != 4) { // code (5)
				// TODO: disqualify
			}
			if (is.readInt() != 4) {
				// TODO: Disqualify
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Tuple(is.readShort(), is.readShort());
	}

	private void requestMove() throws IOException {
		os.writeByte(3);  // code
		os.writeInt(5);  // length
		os.writeInt(1000);  // time limit
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
