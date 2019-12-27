package at.jojokobi.netutil;

import java.io.IOException;
import java.net.UnknownHostException;

import at.jojokobi.netutil.client.Client;
import at.jojokobi.netutil.client.TCPClient;
import at.jojokobi.netutil.server.Server;
import at.jojokobi.netutil.server.TCPServer;

public class TCPServerClientFactory implements ServerClientFactory {
	
	private static TCPServerClientFactory instance = null;
	
	public static TCPServerClientFactory getInstance () {
		if (instance == null) {
			instance = new TCPServerClientFactory();
		}
		return instance;
	}
	
	private TCPServerClientFactory() {
		
	}

	@Override
	public Server createServer(int port) throws IOException {
		return new TCPServer(port);
	}

	@Override
	public Client createClient(String host, int port) throws UnknownHostException, IOException {
		return new TCPClient(host, port);
	}

}
