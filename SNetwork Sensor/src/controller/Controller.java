package controller;

import model.Sensor;

public class Controller {
	private int port;
	private Sensor sensor;
	
	public void createSensor(String code, int dataType) {
		sensor = new Sensor(code, dataType);
	}

	public void setPort(int port) {
		this.port = port;
	}
}
