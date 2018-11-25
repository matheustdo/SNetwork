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
	public static final int HI = 0;
	/* 
	 * Send a data message with following format:
	 * data#nodeCode#dataType#data 
	 */
	public static final int DATA = 1;
	/*
	 * Send a bye message with following format:
	 * bye#ip#port
	 */
	public static final int BYE = 2;
}
