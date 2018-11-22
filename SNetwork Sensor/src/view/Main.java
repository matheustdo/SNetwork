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
		
		System.out.println("## Welcome to SNetwork!");
		
		if(!controller.hasPropertiesFile()) {
			//Atributes for nodes.
			String code;
			String ip;
			int dataType;
			int port;
			
			System.out.println("## You are able to create a sensor node.");
			System.out.print("## Insert a code to identify the sensor (max 3 chars): ");
			code = scan.nextLine();
			
			do {
				System.out.println("## Select a data type:");
				System.out.println(" # 0. Temperature.");
				System.out.print("## Select an option: ");
				x = scan.nextLine().charAt(0);
			} while(x != '0');
			
			
			if(x == '1') {
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
			
			scan.close();
		} else {
			controller.readServerConfigFile();
		}
	}
}
