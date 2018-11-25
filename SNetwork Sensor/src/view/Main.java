package view;

import java.io.IOException;
import java.util.Scanner;

import controller.Controller;
import util.DataType;

public class Main {
	private static Controller controller = new Controller();
	
	public static void main(String[] args) throws IOException {
		//Navigation variable.
		char x = 'x'; 
		
		//Scanner to read input.
		Scanner scan = new Scanner(System.in); 
		
		System.out.println("####");
		System.out.println("## Welcome to SNetwork!");
		
		if(!controller.hasPropertiesFile()) {
			//Atributes for node.
			String code;
			String ip;
			int dataType;
			int port;
			
			System.out.println("## You are able to create a sensor node.");
			System.out.print("## Insert a code to identify the sensor (max 3 chars): ");
			code = scan.nextLine().substring(0, 3);
			
			do {
				System.out.println("## Select a data type:");
				System.out.println(" # 0. Temperature.");
				System.out.print("## Select an option: ");
				x = scan.nextLine().charAt(0);
			} while(x != '0');
			
			
			if(x == '0') {
				dataType = DataType.TEMPERATURE;
			} else {
				dataType = DataType.TEMPERATURE;
			}
			
			System.out.print("## Insert a ip address to the node: ");
			ip = scan.nextLine();
			
			System.out.print("## Insert a port to the node: ");
			port = scan.nextInt();
			scan.nextLine();
			
			controller.start(code, dataType, ip, port);
			
			System.out.println("## The module was created!");
			
			scan.close();
		} else {
			controller.start();
		}
		
		System.out.println("#### Sensor node");
		System.out.println("## Adress: " + controller.getIp() + ":" + controller.getPort());
		System.out.println("## Code: " + controller.getCode());
		System.out.println("## Power: " + controller.getPowerLevel());
		
		do {
			x = scan.nextLine().charAt(0);
		} while(x != 'q' || x == 'Q');
		
		System.exit(0);
	}
}
