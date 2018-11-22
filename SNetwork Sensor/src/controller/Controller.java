package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import model.Sensor;

public class Controller {
	private int port;
	private InetAddress ip;
	private Sensor sensor;	
	
	public void start(String code, int dataType, String ip, int port) throws IOException {
		this.sensor = new Sensor(code, dataType);
		this.ip = InetAddress.getByName(ip);
		this.port = port;
		this.createServerConfigFile(new File("node.properties"));
	}
	
	public int getPort() {
		return port;
	}
	
	public String getIp() {
		return ip.getHostAddress();
	}
	
	public int getPowerLevel() {
		return sensor.getPower();
	}
	
	/**
	 * Sends a UDP message.
	 * @param message Message to send.
	 * @param ip Destination ip.
	 * @param port Destination port.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	private void sendUdpMessage(byte[] data, String ip, int port) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		DatagramPacket datagramPacket;
		
        //Send output object
        byte[] objData = data;
    	datagramPacket = new DatagramPacket(objData, objData.length, InetAddress.getByName(ip), port);
        socket.send(datagramPacket);
        
        //Close socket
        socket.close();
	}

	/**
	 * Verify if the properties file exists.
	 * @return If the properties file exists.
	 */
	public boolean hasPropertiesFile() {
		return (new File("node.properties")).exists();
	}
	
	/**
	 * Generate a node config file with code, data type, ip address and port.
	 * @param file Config file.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	private void createServerConfigFile(File file) throws IOException {		
		file.createNewFile();
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("#Sensor node properties%n" + "fab-code: " + sensor.getCode() + "%ndata-type: " +
        		 sensor.getDataType() + "%nip-address: " + ip.getHostAddress() + "%nport-number: " +
        		 Integer.toString(port));
        fw.close();
	}
	
	/**
	 * Reads the config server file.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void readServerConfigFile() throws IOException {
		File file = new File("node.properties");
		
		if(!file.exists()) {
			createServerConfigFile(file);
		} else {
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);  
            br.readLine();
            
            String codeLine = br.readLine().replaceAll(" ", "");
            String code = codeLine.replace("fab-code:", "").substring(0, 3);
            
            String dataTypeLine = br.readLine().replaceAll(" ", "");
            int dataType = Integer.parseInt(dataTypeLine.replace("data-type:", ""));
            
            String ipLine = br.readLine().replaceAll(" ", "");
            ip = InetAddress.getByName(ipLine.replace("ip-address:", ""));
            
            String portLine = br.readLine().replaceAll(" ", "");            
            port = Integer.parseInt(portLine.replace("port-number:", ""));
            
            this.sensor = new Sensor(code, dataType);
            
            br.close();
            fr.close();
		}
	}	
}
