package at.jojokobi.netutil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class BroadcastOutputStream extends OutputStream{
	
	private List<OutputStream> streams;

	public BroadcastOutputStream(List<OutputStream> streams) {
		super();
		this.streams = streams;
	}

	@Override
	public void write(int b) throws IOException {
		IOException exc = null;
		for (OutputStream outputStream : streams) {
			try {
				outputStream.write(b);
			}
			catch (IOException e) {
				e.printStackTrace();
				exc = e;
			}
		}
		if (exc != null) {
			throw exc;
		}
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
		IOException exc = null;
		for (OutputStream outputStream : streams) {
			try {
				outputStream.write(b, off, len);
			}
			catch (IOException e) {
				e.printStackTrace();
				exc = e;
			}
		}
		if (exc != null) {
			throw exc;
		}
	}
	
	@Override
	public void flush() throws IOException {
		super.flush();
		IOException exc = null;
		for (OutputStream outputStream : streams) {
			try {
				outputStream.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
				exc = e;
			}
		}
		if (exc != null) {
			throw exc;
		}
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		System.err.println("Closing");
		Thread.dumpStack();
		IOException exc = null;
		for (OutputStream outputStream : streams) {
			try {
				outputStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				exc = e;
			}
		}
		if (exc != null) {
			throw exc;
		}
	}

}
