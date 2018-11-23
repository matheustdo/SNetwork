package model;

import java.net.InetAddress;

public class Sensor {	
	private int jumps;
	private int power;
	private int dataType;
	private int port;
	private String code;
	private String data;
	private InetAddress ip;
	
	public Sensor(String code, int dataType, int port, InetAddress ip) {
		this.code = code;
		this.jumps = -1;
		this.power = 100;
		this.data = "26";
		this.dataType = dataType;
		this.port = port;
		this.ip = ip;
	}

	public int getJumps() {
		return jumps;
	}

	public void setJumps(int jumps) {
		this.jumps = jumps;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public int getDataType() {
		return dataType;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
}
