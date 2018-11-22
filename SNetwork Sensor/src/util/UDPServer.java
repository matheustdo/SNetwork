package util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * @author Matheus Teles
 */
public class UDPServer extends Observable implements Runnable {
	private Object obj;
	private DatagramSocket serverSocket;
	
	/**
	 * Constructs a new UDPServer
	 * @param serverPort Server port.
	 * @param serverIP Server ip.
	 * @throws UnknownHostException Indicate that the IP address of a host could not be determined.
	 * @throws SocketException Indicate that there is an error creating or accessing a Socket.
	 */
	public UDPServer(int serverPort, String serverIP) throws UnknownHostException, SocketException {
		this.obj = new Object();
		this.serverSocket = new DatagramSocket(serverPort, InetAddress.getByName(serverIP));
	}
	
	/**
	 * Thread run method.
	 */
	@Override
	public void run() {
		byte[] receivedData = new byte[1024];
		while(!serverSocket.isClosed()) {
			DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			try {
				serverSocket.receive(receivedPacket);				
				ByteArrayInputStream bais = new ByteArrayInputStream(receivedPacket.getData());
				ObjectInputStream ois = new ObjectInputStream(bais);
				obj = ois.readObject();
				ois.close();
				setChanged();
				notifyObservers();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get datagram socket.
	 * @return Datagram socket.
	 */
	public DatagramSocket getDatagramSocket() {
		return serverSocket;
	}

	/**
	 * Get input object.
	 * @return Input object.
	 */
	public Object getObj() {
		return obj;
	}	
	
	/**
	 * Get server address.
	 * @return Server address.
	 */
	public String getServerAdress() {
		return serverSocket.getLocalAddress().getHostAddress();
	}
	
	/**
	 * Get server host name.
	 * @return Server host name.
	 */
	public String getServerHostName() {
		return serverSocket.getLocalAddress().getHostName();
	}
	
	/**
	 * Get server port.
	 * @return Server port.
	 */
	public int getServerPort() {
		return serverSocket.getLocalPort();
	}
}
