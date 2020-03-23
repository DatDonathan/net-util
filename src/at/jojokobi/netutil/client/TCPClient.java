package at.jojokobi.netutil.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import at.jojokobi.netutil.NonClosingInputStream;
import at.jojokobi.netutil.NonClosingOutputStream;

public class TCPClient implements Client {

//	private static final int BUFFER_SIZE = 256;

	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private long clientId;
	
	private ClientController controller;

	public TCPClient(String host, int port) throws UnknownHostException, IOException {
		this.socket = new Socket(host, port);
		in = socket.getInputStream();
		out = socket.getOutputStream();
		try (DataInputStream dataIn = new DataInputStream(new NonClosingInputStream(in))) {
			clientId = dataIn.readLong();
		}
	}	

	@Override
	public void setController(ClientController controller) {
		this.controller = controller;
	}

	@Override
	public synchronized void close() throws IOException {
		socket.close();
	}

	@Override
	public synchronized boolean isClosed() {
		return socket.isClosed();
	}

	@Override
	public synchronized void start() {
		new Thread(() -> {
			try {
				if (controller != null) {
					controller.listenTo(new NonClosingInputStream(in));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, "Client-Listen-Thread").start();
//		new Thread(() -> {
//			try (InputStream in = socket.getInputStream()) {
//				while (socket.isConnected() && !isClosed()) {
//					byte[] message = packetWriterReader.readPacket(in);
//
//					synchronized (TCPClient.this) {
//						recieveBuffer.add(message);
//					}
//
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				socket.close();
//			} catch (SocketException e) {
// 
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}, "Client-Listen-Thread").start();
	}

	@Override
	public InputStream getInputStream() {
		return new NonClosingInputStream(in);
	}

	@Override
	public OutputStream getOutputStream() {
		return new NonClosingOutputStream(out);
	}

	@Override
	public long getClientId() {
		return clientId;
	}

	@Override
	public InetAddress getServerInetAddress() {
		return socket.getInetAddress();
	}
	
}
