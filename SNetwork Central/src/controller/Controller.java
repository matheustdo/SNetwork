package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import model.Central;
import model.Neighbor;
import util.Protocol;
import util.UDPServer;

/**
 * @author Matheus Teles
 */
public class Controller implements Observer {
	private ArrayList<Neighbor> neighbors;
	private Central central;
	private UDPServer server;
	private int updateTime;
	private long updateCode;
	
	/**
	 * Construct a controller.
	 */
	public Controller() {
		this.neighbors = new ArrayList<Neighbor>();
		this.updateTime = 60000;
		this.updateCode = 0;
	}
	
	/**
	 * Start from file.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void start() throws IOException {
		this.readServerConfigFile();
		this.readNeighborsFile();
		this.startServer();
		this.startNetworkUpdater();
	}

	/**
	 * Start manually.
	 * @param ip Ip address.
	 * @param port port number.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void start(String ip, int port) throws IOException {
		this.central = new Central(port, InetAddress.getByName(ip));
		this.createServerConfigFile(new File("central.properties"));
		this.readNeighborsFile();
		this.startServer();
		this.startNetworkUpdater();
	}
	
	/**
	 * Start udp server.
	 * @throws UnknownHostException Thrown to indicate that the IP address of a host could not be determined.
	 * @throws SocketException Thrown to indicate that there is an error creating or accessing a Socket.
	 */
	public void startServer() throws UnknownHostException, SocketException {
		server = new UDPServer(central.getPort(), central.getIp());
		Thread threadServer =  new Thread(server);
		threadServer.start();
		server.addObserver(this);
	}
	
	/**
	 * Start network updater.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	private void startNetworkUpdater() throws IOException {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	String message = Protocol.UPDATE +"#" + updateCode++ + "#0#100#" + central.getIp().getHostAddress() + "#" + central.getPort();
            	try {
            		for(Neighbor neighbor: neighbors) {
            			sendUdpMessage(message, neighbor.getIp(), neighbor.getPort());
            		}
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }, 5000, updateTime);
	}
	
	/**
	 * Get port number.
	 * @return Port number.
	 */
	public int getPort() {
		return central.getPort();
	}
	
	/**
	 * Get ip address.
	 * @return Ip address.
	 */
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
	private void sendUdpMessage(String message, InetAddress ip, int port) throws IOException {
		//Convert string do a byte array.
		DatagramSocket socket = new DatagramSocket();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
		DatagramPacket datagramPacket;		
		oos.writeObject(message);
        oos.close();
        byte[] data = baos.toByteArray();
        
        //Send output object.     
    	datagramPacket = new DatagramPacket(data, data.length, ip, port);
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
        		 Integer.toString(central.getPort()) + "%nupdate-time: " + updateTime);
        fw.close();
	}
	
	/**
	 * Read the config server file.
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
            
            String updateLine = br.readLine().replaceAll(" ", "");            
            updateTime = Integer.parseInt(updateLine.replace("update-time:", ""));
            
            this.central = new Central(port, ip);
            
            br.close();
            fr.close();
		}
	}	
	
	/**
	 * Read the neighbors file.
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
	
	/**
	 * Add neighbor to neighbors file.
	 * @param ip Neighbor ip.
	 * @param port Neighbor port number.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	private void addNeighbor(InetAddress ip, int port) throws IOException {
		File file = new File("central.neighbors");
		
		if(file.exists()) {
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.newLine();
			bw.write(ip.getHostAddress() + " " + port);
			bw.close();
			fw.close();
		}
	}

	/**
	 * Notification for observer.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof UDPServer) {
			StringTokenizer st = new StringTokenizer((String) arg, "#");
			int protocol = Integer.parseInt(st.nextToken());	
			
			System.out.println(">> " + arg);
			
			if (protocol == Protocol.HI) {
				try {
					InetAddress ip = InetAddress.getByName(st.nextToken());
					int port = Integer.parseInt(st.nextToken());
					boolean hasNeighbor = false;
					
					for(Neighbor neighbor: neighbors) {
						if(neighbor.getIp().equals(ip) && neighbor.getPort() == port) {
							hasNeighbor = true;
						}
					}
					
					if(!hasNeighbor) {		            	
		            	neighbors.add(new Neighbor(port, ip));
		            	addNeighbor(ip, port);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Close the application.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void die() throws IOException {		
		for(Neighbor neighbor: neighbors) {
			sendUdpMessage(Protocol.BYE + "#" + central.getIp().getHostAddress() + "#" + 
						   central.getPort(), neighbor.getIp(), neighbor.getPort());			
		}
		System.exit(0);
	}
}
