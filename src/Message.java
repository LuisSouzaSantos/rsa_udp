import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class Message {
	
	public static final String PUBLIC_KEY_INDICATION = "PUBLIC_KEY===";
	public static final String ENCRYPTION_INDICATION = "ENCRYPTED_MESSAGE:";
	public static final String UNENCRYPTION_INDICATION = "UNENCRYPTED_MESSAGE:";
	
	private DatagramSocket socket;
	private InetAddress ipAddress = null;
	private int port;
	
	public Message(InetAddress ipAddress, int port, DatagramSocket socket) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.socket = socket;
	}
	
	public void sendPublicKey(KeyPair keyPair) throws IOException {
		if (keyPair == null) { return; }
		
		PublicKey publicKey = keyPair.getPublic();

		String publicKeyEncoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
		byte[] sendData = publicKeyEncoded.getBytes();
        
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
        this.socket.send(sendPacket);
	}
	
	public void sendMessage(String message, RSAPublicKey rsaPublicKey) throws Exception {
        byte[] sendData =  buildMessage(message, rsaPublicKey).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
        this.socket.send(sendPacket);
	}
	
	private String buildMessage(String message, RSAPublicKey rsaPublicKey) throws Exception {
		return rsaPublicKey == null ? Message.UNENCRYPTION_INDICATION + message
				: Message.ENCRYPTION_INDICATION + RSAGenerator.encryptMessage(message, rsaPublicKey);
	}
	
}
