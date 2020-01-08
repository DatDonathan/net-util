package at.jojokobi.netutil.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jojokobi.netutil.BroadcastOutputStream;
import at.jojokobi.netutil.NonClosingInputStream;
import at.jojokobi.netutil.NonClosingOutputStream;

public class TCPServer implements Server {

//	private static final int BUFFER_SIZE = 256;

	private ServerSocket serverSocket;
	private Map<Long, SocketInstance> clientSockets = Collections.synchronizedMap(new HashMap<>());
	private List<Long> newClients = new ArrayList<>();
//	private List<Long> removedClients = new ArrayList<>();

	private ServerController controller;
	
	private volatile long nextId = MIN_ID;

	public TCPServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public synchronized void close() throws IOException {
		serverSocket.close();
	}

	@Override
	public synchronized List<Long> getClients() {
		return new ArrayList<>(clientSockets.keySet());
	}

	@Override
	public synchronized boolean isClosed() {
		return serverSocket.isClosed();
	}

	@Override
	public void start() {
		new Thread(() -> {
			while (!isClosed()) {
				try {
					Socket socket = serverSocket.accept();
					synchronized (TCPServer.this) {
						long id = nextId++;
						SocketInstance instance = new SocketInstance(socket, socket.getInputStream(), socket.getOutputStream());
						//Send id
						try (DataOutputStream out = new DataOutputStream(new NonClosingOutputStream(instance.getOutputStream()))) {
							out.writeLong(id);
						}
						clientSockets.put(id, instance);
						newClients.add(id);
						controller.onConnect(id, new NonClosingOutputStream(instance.getOutputStream()));
						//Listen Thread
						new Thread (() -> {
							try {
								controller.listenTo(id, new NonClosingInputStream(instance.getInputStream()));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}, "Client-Listen-Thread " + id).start();
					}
				} catch (SocketException e) {
					
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "Server-Accept-Thread").start();
	}

	@Override
	public OutputStream getOutputStream(long clientId) {
		OutputStream out = null;
		if (BROADCAST_CLIENT_ID == clientId) {
			out = getBroadcastOutputStream();
		}
		else {
			out = new NonClosingOutputStream(clientSockets.get(clientId).getOutputStream());
		}
		return out;
	}

	@Override
	public OutputStream getBroadcastOutputStream() {
		List<OutputStream> streams = new ArrayList<>();
		for (var socket : clientSockets.entrySet()) {
			streams.add(socket.getValue().getOutputStream());
		}
		return new NonClosingOutputStream(new BroadcastOutputStream(streams));
	}

	@Override
	public InputStream getInputStream(long clientId) {
		return new NonClosingInputStream(clientSockets.get(clientId).getInputStream());
	}

	@Override
	public void setController(ServerController controller) {
		this.controller = controller;
	}

	@Override
	public List<Long> fetchNewClients() {
		List<Long> newClients = new ArrayList<>(this.newClients);
		this.newClients.clear();
		return newClients;
	}

	@Override
	public InetAddress getHostAddress() {
		InetAddress address = null;
		try (Socket socket = new Socket();) {
			socket.connect(new InetSocketAddress("google.com", 80));
			address = socket.getLocalAddress();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return address;
	}

//	@Override
//	public List<Long> fetchRemovedClients() {
//		List<Long> removedClients = new ArrayList<>(this.removedClients);
//		this.removedClients.clear();
//		return removedClients;
//	}
//
//	@Override
//	public void update() {
//		for (long l : clientSockets.keySet()) {
//			try {
//				clientSockets.get(l).getInputStream().available();
//			}
//			catch (IOException e) {
//				System.out.println("Removed " + l);
//				clientSockets.remove(l);
//				removedClients.add(l);
//			}
//		}
//	}
}

class SocketInstance {
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public SocketInstance(Socket socket, InputStream inputStream, OutputStream outputStream) {
		super();
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	public Socket getSocket() {
		return socket;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
}
