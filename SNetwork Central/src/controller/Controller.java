package controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import java.util.Timer;
import java.util.TimerTask;

import model.Neighbor;
import model.Central;
import util.Protocol;
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
		this.startNetworkUpdater();
	}
	
	private void startNetworkUpdater() throws IOException {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	try {
					sendUdpMessage(Protocol.HI + "#0#", "127.0.0.1", 4001);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }, 5000, 60000);
		/**
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if() {
						
					}
				}
			}
		}).start();*/		
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
	private void sendUdpMessage(String message, String ip, int port) throws IOException {
		//Convert string do a byte array.
		DatagramSocket socket = new DatagramSocket();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
		DatagramPacket datagramPacket;		
		oos.writeObject(message);
        oos.close();
        byte[] data = baos.toByteArray();
        
        //Send output object.     
    	datagramPacket = new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
        socket.send(datagramPacket);
        
        //Close socket.
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
		if(o instanceof UDPServer) {
			System.out.println(">> " + arg);
		}
	}	
}
