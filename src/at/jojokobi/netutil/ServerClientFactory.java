package at.jojokobi.netutil;

import java.io.IOException;

import at.jojokobi.netutil.client.Client;
import at.jojokobi.netutil.server.Server;

public interface ServerClientFactory {
	
	public Server createServer (int port) throws IOException;
	
	public Client createClient (String host, int port) throws IOException;

}
