package model;

import java.net.InetAddress;

/**
 * @author Matheus Teles
 */
public class Sensor {	
	private int jumps;
	private int power;
	private int dataType;
	private int port;
	private String code;
	private String data;
	private InetAddress ip;
	
	/**
	 * Construct a sensor.
	 * @param code Sensor code.
	 * @param dataType Data type.
	 * @param port Port number.
	 * @param ip Ip address.
	 */
	public Sensor(String code, int dataType, int port, InetAddress ip) {
		this.code = code;
		this.jumps = -1;
		this.power = 100;
		this.data = "26";
		this.dataType = dataType;
		this.port = port;
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

	/**
	 * Get data.
	 * @return Data.
	 */
	public String getData() {
		return data;
	}

	/**
	 * Set data.
	 * @param data Get data.
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * Get code.
	 * @return Code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Get data type.
	 * @return Data type.
	 */
	public int getDataType() {
		return dataType;
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
