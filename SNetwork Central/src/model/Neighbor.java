package model;

import java.net.InetAddress;

public class Neighbor {
	private int port;
	private InetAddress ip;
	
	public Neighbor(int port, InetAddress ip) {
		this.port = port;
		this.ip = ip;
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
