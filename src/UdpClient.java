import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class UdpClient {

    private DatagramSocket clientSocket;
    private DatagramPacket receivePacket = null;
    private InetAddress ipAddress;
    private int serverPort;
    private byte[] receiveData;
    private RSAPublicKey rsaPublicKey = null;
    private KeyPair keyPair = null;
    private Message message;
	
    public UdpClient(String serverAddress, int serverPort) throws Exception {
        this.clientSocket = new DatagramSocket();
        this.ipAddress = InetAddress.getByName(serverAddress);
        this.serverPort = serverPort;
        this.receiveData = new byte[4096];
        this.keyPair = RSAGenerator.generateKeyPairs();
        this.message = new Message(ipAddress, this.serverPort, clientSocket);
        
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        System.out.println("PublicKey: "+new String(Base64.getEncoder().encode(rsaPublicKey.getEncoded())));
    }
    
    public void sendData(String message, boolean wouldLikeToSendPublicKey) throws Exception {
    	if (wouldLikeToSendPublicKey) {
    		this.message.sendPublicKey(keyPair);
    		return;
    	}
    		
    	this.message.sendMessage(message, rsaPublicKey);
    }
    
	private String handleReceiveMessage() throws IOException {
		this.receiveData = new byte[4096];
        this.receivePacket = new DatagramPacket(this.receiveData, this.receiveData.length);
        this.clientSocket.receive(receivePacket);
        
        return new String(this.receivePacket.getData(), 0 ,this.receivePacket.getLength());
	}
   
    public String receiveData() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String receivedMessage = handleReceiveMessage();
                
        if (Utils.isPublicKeyMessage(receivedMessage)) {
        	storagePublicKey(receivedMessage);
        	receivedMessage = "MESSAGE:CHAVE PUBLICA RECEBIDA";
        }
        
        boolean isEncryptedMessage = Utils.isEncrypedMessage(receivedMessage);
        
        String message = Utils.extractMessage(receivedMessage);

        String realMessage = handleRealMessage(message, isEncryptedMessage);

        return "Message received: "+ realMessage;
    }
    
    public void close() {
        this.clientSocket.close();
    }
    
    
    public void setStoragedClientPublicKey(RSAPublicKey rsaPublicKey) { 
    	this.rsaPublicKey = rsaPublicKey;
    }
    
    public void storagePublicKey(String message) throws NoSuchAlgorithmException, InvalidKeySpecException {    	
    	byte[] encodedKey = Base64.getDecoder().decode(message);
    	
		setStoragedClientPublicKey(new RsaPublicKeyImpl(encodedKey));
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
    
}
