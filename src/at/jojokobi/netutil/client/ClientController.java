package at.jojokobi.netutil.client;

import java.io.IOException;
import java.io.InputStream;

public interface ClientController {

	public void listenTo (InputStream in) throws IOException;
	
}
