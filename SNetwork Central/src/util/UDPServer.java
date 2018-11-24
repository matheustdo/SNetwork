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
	private DatagramSocket serverSocket;
	
	/**
	 * Constructs a new UDPServer
	 * @param serverPort Server port.
	 * @param serverIP Server ip.
	 * @throws UnknownHostException Indicate that the IP address of a host could not be determined.
	 * @throws SocketException Indicate that there is an error creating or accessing a Socket.
	 */
	public UDPServer(int serverPort, InetAddress serverIP) throws UnknownHostException, SocketException {
		this.serverSocket = new DatagramSocket(serverPort, serverIP);
	}
	
	/**
	 * Thread run method.
	 */
	@Override
	public void run() {
		byte[] receivedData = new byte[1024];
		while(!serverSocket.isClosed()) {
			DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);
			try {
				serverSocket.receive(packet);				
				ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
				ObjectInputStream ois = new ObjectInputStream(bais);
				Object data = ois.readObject();
				ois.close();
				setChanged();
				notifyObservers(data);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
