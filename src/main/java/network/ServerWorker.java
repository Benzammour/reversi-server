package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by benzammour on August, 2019
 */
public class ServerWorker extends Thread {
	private final Server server;
	private Socket client;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMap(String map) throws IOException {
		// send map
		os.writeByte(2);
		os.writeInt(map.getBytes().length);
		os.writeBytes(map);
	}

	public void sendPlayerNumber() throws IOException {
		os.writeByte(3);
		os.writeInt(1);
		os.writeByte(id);
	}

	public void close() {
		server.getWorker().remove(this);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
