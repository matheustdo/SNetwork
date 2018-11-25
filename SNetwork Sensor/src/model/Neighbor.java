package model;

import java.net.InetAddress;

/**
 * @author Matheus Teles
 */
public class Neighbor {
	private int port;
	private InetAddress ip;
	private int jumps;
	private int power;
	
	/**
	 * Construct a neighbor
	 * @param port port number.
	 * @param ip Ip address.
	 * @param jumps Jumps number.
	 * @param power Power level.
	 */
	public Neighbor(int port, InetAddress ip, int jumps, int power) {
		this.port = port;
		this.ip = ip;
		this.jumps = jumps;
		this.power = power;
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

	/**
	 * Get jumps.
	 * @return Jumps number.
	 */
	public int getJumps() {
		return jumps;
	}

	/**
	 * Set jumps.
	 * @param jumps Jumps number.
	 */
	public void setJumps(int jumps) {
		this.jumps = jumps;
	}

	/**
	 * Get power level.
	 * @return Power level.
	 */
	public int getPower() {
		return power;
	}

	/**
	 * Set power level.
	 * @param power Power level.
	 */
	public void setPower(int power) {
		this.power = power;
	}
}
