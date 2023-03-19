import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class UdpServer {
	
    private DatagramSocket serverSocket;
    private byte[] buffer = null;
    private RSAPublicKey rsaPublicKey = null;
    private KeyPair keyPair = null;
    private DatagramPacket receivePacket = null;
    
	public UdpServer(int port) throws SocketException {
        this.serverSocket = new DatagramSocket(port);
        this.buffer = new byte[4096];
        keyPair = RSAGenerator.generateKeyPairs();
	}
	
	private String handleReceiveMessage() throws IOException {
		this.buffer = new byte[4096];
        this.receivePacket = new DatagramPacket(this.buffer, this.buffer.length);
        this.serverSocket.receive(receivePacket);
        
        return new String(receivePacket.getData(), 0, receivePacket.getLength());
	}
	
	private String handleRealMessage(String message, boolean isEncrypted) {
        String realMessage = "";
        
        try {
			realMessage = isEncrypted ? RSAGenerator.decryptMessage(message, keyPair.getPrivate()) : message;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return realMessage;
	}
	
	public void startServer() throws Exception {
        System.out.println("Server started");
        
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        System.out.println("PublicKey: "+new String(Base64.getEncoder().encode(rsaPublicKey.getEncoded())));

		while (true) {
            String receivedMessage = handleReceiveMessage();
            
            boolean isEncryptedMessage = Utils.isEncrypedMessage(receivedMessage);
            
            if (Utils.isPublicKeyMessage(receivedMessage)) { 
            	storagePublicKey(receivedMessage);
            	receivedMessage = "MESSAGE:CHAVE PUBLICA RECEBIDA";
            }
            
            String message = Utils.extractMessage(receivedMessage);
            String realMessage = handleRealMessage(message, isEncryptedMessage);

            System.out.println("Message received: "+realMessage);
       
            Message messenger = new Message(receivePacket.getAddress(), receivePacket.getPort(), serverSocket);
            
            if (!isEncryptedMessage) { 
            	messenger.sendPublicKey(keyPair); 
            }else  {
                messenger.sendMessage(realMessage, this.rsaPublicKey);
            }
        }
	}
	
    public void stop() {
        serverSocket.close();
    }
    
    public void setStoragedClientPublicKey(RSAPublicKey rsaPublicKey) { 
    	this.rsaPublicKey = rsaPublicKey;
    }
    
    public void storagePublicKey(String message) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ClassNotFoundException {    	
    	byte[] encodedKey = Base64.getDecoder().decode(message);
        
		setStoragedClientPublicKey(new RsaPublicKeyImpl(encodedKey));
    }
        	
}
