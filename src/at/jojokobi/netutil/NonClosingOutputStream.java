package at.jojokobi.netutil;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NonClosingOutputStream extends FilterOutputStream{

	public NonClosingOutputStream(OutputStream in) {
		super(in);
	}
	
	@Override
	public void close() throws IOException {
		
	}

}
