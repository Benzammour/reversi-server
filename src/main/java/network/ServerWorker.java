package network;

import map.GameMap;
import util.Tuple;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by benzammour on August, 2019
 */
public class ServerWorker extends Thread {
	private final Server server;
	private Socket client;
	private DataInputStream is;
	private DataOutputStream os;
	private byte id;

	ServerWorker(Server server, Socket client, byte id) {
		this.server = server;
		this.client = client;
		this.id = id;
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
		if (!GameMap.getInstance().isMoveValid(coord.x, coord.y, (char) ('0' + id))) {
        	// TODO: Disqualify
			System.err.println("doesnt work mate");
		}
		GameMap.getInstance().move(coord.x, coord.y, (char) ('0' + id), 20);

		// broadcast move to all other players
		for (ServerWorker sw : server.getWorker()) {
			sw.getOs().writeByte(6);  // code
			sw.getOs().writeInt(5);  // length
			sw.getOs().writeShort(coord.x);
			sw.getOs().writeShort(coord.y);
			sw.getOs().writeByte(id);
		}
	}

	public Tuple getMoveReponse() throws IOException {
		try {
			if (is.readByte() != 5) { // code (5)
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

	public void requestMove() throws IOException {
		os.writeByte(4);  // code
		os.writeInt(5);  // length
		os.writeInt(1000);  // time limit
		os.writeByte(0);  // max depth limit; 0 = unlimited
	}

	public void sendPlayerNumber() throws IOException {
		// send map
		os.writeByte(3);
		os.writeInt(1);
		os.writeByte(id);
	}

	public void sendString(byte code, int payloadLength, String payload) throws IOException {
		os.writeByte(code);
		os.writeInt(payloadLength);
		os.writeBytes(payload);
	}

	public void close() {
		server.getWorker().remove(this);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getClient() {
		return client;
	}

	public DataInputStream getIs() {
		return is;
	}

	public DataOutputStream getOs() {
		return os;
	}

}
