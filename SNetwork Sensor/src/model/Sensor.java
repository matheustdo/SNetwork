package model;

public class Sensor {	
	private int jumps;
	private int power;
	private int dataType;
	private String code;
	private String data;	
	
	public Sensor(String code, int dataType) {
		this.code = code;
		this.jumps = -1;
		this.power = 100;
		this.data = "26";
		this.dataType = dataType;
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
}
