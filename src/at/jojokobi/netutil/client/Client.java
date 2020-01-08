package at.jojokobi.netutil.client;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public interface Client extends Closeable {
	
	/**
	 * 
	 * @return	The input stream to recieve packets from a server. The close method wont close the input stream. To close the clients InputStream call {@link Client#close()}
	 */
	public InputStream getInputStream ();
	
	/**
	 * 
	 * @return	The output stream to send packets to a server. The close method wont close the output stream. To close the clients OutputStream call {@link Client#close()}
	 */
	public OutputStream getOutputStream ();
	
	public boolean isClosed ();
	
	public long getClientId ();
	
	public void start ();
	
	public InetAddress getServerInetAddress();
	
	public void setController (ClientController controller);

}
