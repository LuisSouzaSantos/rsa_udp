public class Utils {
	
	public static final String SEPARATOR = ":";

	public static boolean isPublicKeyMessage(String message) {
		if (message == null) { return false; }
		
		return !(message.startsWith(Message.ENCRYPTION_INDICATION) || message.startsWith(Message.UNENCRYPTION_INDICATION));
	}
	
	public static String extractKey(String message) {
		if (message == null) { return ""; }
		
		return message.split(SEPARATOR)[1];
	}
	
	public static boolean isEncrypedMessage(String message) {
		if (message == null) { return false; }
		
		return message.startsWith(Message.ENCRYPTION_INDICATION);
	}
	
    public static String extractMessage(String receivedMessage) {
    	if (receivedMessage == null) { return ""; }
    	
    	String[] splitedMessage = receivedMessage.split(SEPARATOR);
    	
    	if (splitedMessage.length <= 1) { return splitedMessage[0]; }
    	
    	return splitedMessage[1];
    }
   
}
