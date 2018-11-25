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
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import model.Neighbor;
import model.Sensor;
import util.Protocol;
import util.UDPServer;

/**
 * @author Matheus Teles
 */
public class Controller implements Observer {
	private ArrayList<Neighbor> neighbors;
	private Sensor sensor;
	private UDPServer server;
	private Neighbor clusterHead;
	private final int criticalLevel = 20;
	private final int minimalUpdateTime = 5000;
	private String dataFusion;
	private Calendar lastUpdate = Calendar.getInstance();
	
	/**
	 * Construct a controller.
	 */
	public Controller() {
		this.neighbors = new ArrayList<Neighbor>();
	}
	
	/**
	 * Start from file.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void start() throws IOException {
		this.readServerConfigFile();
		this.readNeighborsFile();
		this.live();
		this.startServer();
		this.startDataSender();
	}
	
	/**
	 * Start manually.
	 * @param code Code.
	 * @param dataType Data type.
	 * @param ip Ip address.
	 * @param port Port number.
	 * @throws IOException IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void start(String code, int dataType, String ip, int port) throws IOException {
		this.sensor = new Sensor(code, dataType, port, InetAddress.getByName(ip));
		this.createServerConfigFile(new File("node.properties"));
		this.readNeighborsFile();
		this.live();
		this.startServer();
		this.startDataSender();
	}
	
	/**
	 * Start udp server.
	 * @throws UnknownHostException Thrown to indicate that the IP address of a host could not be determined.
	 * @throws SocketException Thrown to indicate that there is an error creating or accessing a Socket.
	 */
	public void startServer() throws UnknownHostException, SocketException {
		server = new UDPServer(sensor.getPort(), sensor.getIp());
		Thread threadServer =  new Thread(server);
		threadServer.start();
		server.addObserver(this);
	}
	
	/**
	 * Get port number.
	 * @return Port number.
	 */
	public int getPort() {
		return sensor.getPort();
	}
	
	/**
	 * Get ip address.
	 * @return Ip address.
	 */
	public String getIp() {
		return sensor.getIp().getHostAddress();
	}
	
	/**
	 * Get power lever.
	 * @return Power level.
	 */
	public int getPowerLevel() {
		return sensor.getPower();
	}
	
	/**
	 * Get code.
	 * @return Code.
	 */
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
        sensor.setPower(sensor.getPower() - 1);
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
	 * Read the config server file.
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
	 * Read the neighbors file.
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
            	
            	Neighbor neighbor = new Neighbor(port, ip, -1, -1);
            	
            	neighbors.add(neighbor);
            }
            
            br.close();
            fr.close();
		}
	}
	
	private void startDataSender() throws IOException {
		Timer timer = new Timer();
		dataFusion = Protocol.DATA + "#" + sensor.getCode() + "#" + sensor.getDataType() + "#" + sensor.getData();

		timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	if(clusterHead != null) {            		
	            	try {
	            		sendUdpMessage(dataFusion, clusterHead.getIp(), clusterHead.getPort());
					} catch (IOException e) {
						e.printStackTrace();
					}
	        		dataFusion = Protocol.DATA + "#" + sensor.getCode() + "#" + sensor.getDataType() + "#" + sensor.getData();
            	}
            }
        }, 5000, 5000);
	}

	/**
	 * Notification for observer.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof UDPServer) {
			sensor.setPower(sensor.getPower() - 1);
			StringTokenizer st = new StringTokenizer((String) arg, "#");
			int protocol = Integer.parseInt(st.nextToken());			
			
			System.out.println(">> " + arg);
			
			if(protocol == Protocol.HI) {
				try {
					int jumps = Integer.parseInt(st.nextToken());
					int power = Integer.parseInt(st.nextToken());
					InetAddress ip = InetAddress.getByName(st.nextToken());
					int port = Integer.parseInt(st.nextToken());
					
					if(jumps != -1) {
						if(jumps == 0 || clusterHead == null) {
							clusterHead = new Neighbor(port, ip, jumps, power);
							sensor.setJumps(jumps + 1);
						} else if (jumps + 1 < sensor.getJumps() || (clusterHead.getPower() <= criticalLevel && power >= criticalLevel)){
							clusterHead = new Neighbor(port, ip, jumps, power);
							sensor.setJumps(jumps + 1);
						}
						
						if((Calendar.getInstance().getTimeInMillis() - lastUpdate.getTimeInMillis()) >= minimalUpdateTime) {
							for(Neighbor neighbor: neighbors) {
								if(!neighbor.getIp().equals(clusterHead.getIp()) || neighbor.getPort() != clusterHead.getPort()) {
									sendUdpMessage(Protocol.HI + "#" + sensor.getJumps() + 
		         						   "#" + sensor.getPower() +"#" + sensor.getIp().getHostAddress() + 
		         						   "#" + sensor.getPort(), neighbor.getIp(), neighbor.getPort());								
								}            			
		            		}
							
							lastUpdate = Calendar.getInstance();
						}	
					}				
				} catch (IOException e) {
					e.printStackTrace();
				}				
			} else if (protocol == Protocol.DATA) {
				String message = ((String) arg).replaceFirst(protocol + "", "");
				dataFusion  += message;
			} else if (protocol == Protocol.BYE) {
				try {
					InetAddress ip = InetAddress.getByName(st.nextToken());
					int port = Integer.parseInt(st.nextToken());
					
					if(clusterHead.getIp().equals(ip) && clusterHead.getPort() == port) {
						clusterHead = null;
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}			
			}
		}
	}
	
	/**
	 * Control the node live.
	 */
	private void live() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(sensor.getPower() <= 0) {
						try {
							System.out.println("## Node died");
							die();
						} catch (IOException e) {
							e.printStackTrace();
						}						
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * Close the application.
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	public void die() throws IOException {		
		for(Neighbor neighbor: neighbors) {
			sendUdpMessage(Protocol.BYE + "#" + sensor.getIp().getHostAddress() + "#" + 
						   sensor.getPort(), neighbor.getIp(), neighbor.getPort());			
		}
		System.exit(0);
	}

	/**
	 * Get jumps number.
	 * @return Jumps number.
	 */
	public int getJumps() {
		return sensor.getJumps();
	}	
}
