package at.jojokobi.netutil.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ServerController {
	
	public void onConnect (long client, OutputStream out) throws IOException;
	
	public void listenTo (long client, InputStream in) throws IOException;

}
