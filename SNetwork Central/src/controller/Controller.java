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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import model.Neighbor;
import model.Central;
import util.UDPServer;

/**
 * @author Matheus Teles
 */
public class Controller implements Observer {
	private ArrayList<Neighbor> neighbors;
	private Central central;
	private UDPServer server;
	
	public Controller() {
		this.neighbors = new ArrayList<Neighbor>();
	}
	
	public void start() throws IOException {
		this.readServerConfigFile();
		this.readNeighborsFile();
		this.startServer();
	}
	
	public void start( String ip, int port) throws IOException {
		this.central = new Central(port, InetAddress.getByName(ip));
		this.createServerConfigFile(new File("central.properties"));
		this.readNeighborsFile();
		this.startServer();
	}
	
	public void startServer() throws UnknownHostException, SocketException {
		server = new UDPServer(central.getPort(), central.getIp());
		Thread threadServer =  new Thread(server);
		threadServer.start();
		server.addObserver(this);
	}
	
	public int getPort() {
		return central.getPort();
	}
	
	public String getIp() {
		return central.getIp().getHostAddress();
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
		return (new File("central.properties")).exists();
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
        pw.printf("#Sensor node properties%nip-address: " + central.getIp().getHostAddress() + "%nport-number: " +
        		 Integer.toString(central.getPort()));
        fw.close();
	}
	
	/**
	 * Reads the config server file.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void readServerConfigFile() throws IOException {
		File file = new File("central.properties");
		
		if(!file.exists()) {
			createServerConfigFile(file);
		} else {
			FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);  
            br.readLine();
            String ipLine = br.readLine().replaceAll(" ", "");
            InetAddress ip = InetAddress.getByName(ipLine.replace("ip-address:", ""));
            
            String portLine = br.readLine().replaceAll(" ", "");            
            int port = Integer.parseInt(portLine.replace("port-number:", ""));
            
            this.central = new Central(port, ip);
            
            br.close();
            fr.close();
		}
	}	
	
	/**
	 * Reads the neighbors file.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void readNeighborsFile() throws IOException {
		File file = new File("central.neighbors");
		
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

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}	
}
