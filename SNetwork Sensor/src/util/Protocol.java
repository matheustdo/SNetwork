package util;

/**
 * Network protocol.
 * @author Matheus Teles
 */
public final class Protocol {
	/* 
	 * Send an update message with following format:
	 * hi#jumpsNumber#powerLevel#ip#port 
	 */
	public static final int UPDATE = 0;
	/* 
	 * Send a data message with following format:
	 * data#nodeCode#dataType#data 
	 */
	public static final int DATA = 1;
	/*
	 * Send a hi message with following format:
	 * hi#ip#port
	 */
	public static final int HI = 2;
	/*
	 * Send a bye message with following format:
	 * bye#ip#port
	 */
	public static final int BYE = 3;
	/**
	 * Send a message that the sensor is not connected to the central with following format:
	 * lost#ip#port
	 */
	public static final int LOST = 4;
}
