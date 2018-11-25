package model;

import java.net.InetAddress;

/**
 * @author Matheus Teles
 */
public class Central {	
	private int port;
	private InetAddress ip;
	
	/**
	 * Construct a central.
 	 * @param port Port number.
	 * @param ip Ip address.
	 */
	public Central(int port, InetAddress ip) {
		this.port = port;
		this.ip = ip;
	}

	/**
	 * Get port number.
	 * @return Port number.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set port number.
	 * @param port Port number.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Get ip address.
	 * @return Ip address.
	 */
	public InetAddress getIp() {
		return ip;
	}

	/**
	 * Set ip address.
	 * @param ip Ip address.
	 */
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
}
