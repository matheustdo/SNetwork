package view;

import java.io.IOException;
import java.util.Scanner;

import controller.Controller;
import util.DataType;

public class Main {
	private static Controller controller = new Controller();
	
	public static void main(String[] args) throws IOException {
		char x = 'x'; //Navigation variable.
		Scanner scan = new Scanner(System.in); //Scanner to read input.
		
		//Atributes for application.
		String code;
		int dataType;
		int port;
		
		System.out.println("## Welcome to SNetwork!");
		System.out.println("## You are able to create a sensor node.");
		System.out.print("## Insert a code to identify the sensor: ");
		code = scan.nextLine();
		
		do {
			System.out.println("## Select a data type:");
			System.out.println(" # 1. Temperature;");
			System.out.print("## Select an option: ");
			x = scan.nextLine().charAt(0);
		} while(x != '1');
		
		
		if(x == '1') {
			dataType = DataType.TEMPERATURE;
		} else {
			dataType = DataType.TEMPERATURE;
		}
		
		System.out.print("## Insert a port to the node: ");
		port = scan.nextInt();
		scan.nextLine(); //Clears the buffer.
		
		controller.createSensor(code, dataType); //Creates the sensor.
		controller.setPort(port); //Sets the sensor port.
		
		
		
		scan.close(); //Closes the scanner.
	}
}
