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
			String ip;
			int port;
			
			System.out.println("## You are able to create a central node.");
			
			System.out.print("## Insert a ip address to the node: ");
			ip = scan.nextLine();
			
			System.out.print("## Insert a port to the node: ");
			port = scan.nextInt();
			scan.nextLine();
			
			controller.start(ip, port);
			
			System.out.println("## The central was created!");
			
			scan.close();
		} else {
			controller.start();
		}
		
		System.out.println("#### Central node");
		System.out.println("## Adress: " + controller.getIp() + ":" + controller.getPort());
		
		do {
			x = scan.nextLine().charAt(0);
		} while(x != 'q' || x == 'Q');
		
		System.exit(0);
	}
}
