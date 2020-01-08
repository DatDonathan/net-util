package at.jojokobi.netutil.server;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.List;

public interface Server extends Closeable {
	
	public static final long BROADCAST_CLIENT_ID = 0;
	public static final long MIN_ID = 1;
	
	public void start ();
	
	public List<Long> getClients ();
	
	public boolean isClosed ();
	
	public InetAddress getHostAddress();
	
	public OutputStream getOutputStream (long clientClient);
	
	public OutputStream getBroadcastOutputStream ();
	
	public InputStream getInputStream (long clientClient);
	
	public List<Long> fetchNewClients ();
	
	public void setController (ServerController controller);

}
