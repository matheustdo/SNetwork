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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import model.Neighbor;
import model.Sensor;

public class Controller {
	private ArrayList<Neighbor> neighbors;
	private Sensor sensor;	
	
	public Controller() {
		neighbors = new ArrayList<Neighbor>();
	}
	
	public void start() throws IOException {
		this.readServerConfigFile();
		this.readNeighborsFile();
	}
	
	public void start(String code, int dataType, String ip, int port) throws IOException {
		this.sensor = new Sensor(code, dataType, port, InetAddress.getByName(ip));
		this.createServerConfigFile(new File("node.properties"));
		this.readNeighborsFile();
	}
	
	public int getPort() {
		return sensor.getPort();
	}
	
	public String getIp() {
		return sensor.getIp().getHostAddress();
	}
	
	public int getPowerLevel() {
		return sensor.getPower();
	}
	
	public String getCode() {
		return sensor.getCode();
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
        		 sensor.getDataType() + "%nip-address: " + sensor.getIp().getHostAddress() + "%nport-number: " +
        		 Integer.toString(sensor.getPort()));
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
            InetAddress ip = InetAddress.getByName(ipLine.replace("ip-address:", ""));
            
            String portLine = br.readLine().replaceAll(" ", "");            
            int port = Integer.parseInt(portLine.replace("port-number:", ""));
            
            this.sensor = new Sensor(code, dataType, port, ip);
            
            br.close();
            fr.close();
		}
	}	
	
	/**
	 * Reads the neighbors file.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void readNeighborsFile() throws IOException {
		File file = new File("node.neighbors");
		
		if(file.exists()) {
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);  
            br.readLine();
            
            while(br.ready()) {
            	StringTokenizer st = new StringTokenizer(br.readLine());
            	InetAddress ip = InetAddress.getByName(st.nextToken());
            	int port = Integer.parseInt(st.nextToken());
            	
            	Neighbor neighbor = new Neighbor(port, ip);
            	
            	neighbors.add(neighbor);
            }
            
            br.close();
            fr.close();
		}
	}	
}
