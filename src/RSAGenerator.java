import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAGenerator {

	public static KeyPair generateKeyPairs() {
        SecureRandom random = new SecureRandom();
        
        //Generating first prime number
        BigInteger firstPrime = BigInteger.probablePrime(4096, random);
        
        //Generating second prime number
        BigInteger secondPrime = BigInteger.probablePrime(4096, random);
        
        BigInteger modulus = firstPrime.multiply(secondPrime);
        
        //Calculating totiente funcution
        BigInteger totiente = firstPrime.subtract(BigInteger.ONE).multiply(secondPrime.subtract(BigInteger.ONE));
        
        BigInteger publicExponent = new BigInteger("3");
        
        while(totiente.gcd(publicExponent).intValue() > 1) publicExponent = publicExponent.add(new BigInteger("2"));
        
        BigInteger privateExponent = publicExponent.modInverse(totiente);
        
        RSAPublicKey publicKey = new RsaPublicKeyImpl(modulus, publicExponent);
        RSAPrivateKey privateKey = new RsaPrivateKeyImpl(modulus, privateExponent);
        
        return new KeyPair(publicKey, privateKey);
	}
	
	public static String encryptMessage(String message, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedMessage);
	}
	
	public static String decryptMessage(String message, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedMessageByte = Base64.getDecoder().decode(message);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessageByte);
        return new String(decryptedMessage, "UTF-8");
	}
	
	public static void main(String[] args) throws Exception {
		KeyPair keyPair = generateKeyPairs();
		
		String message  = "The information security is of significant importance to ensure the privacy of communications";
//		String encryptedMessage = encryptMessage(message, keyPair.getPublic());
//		String decryptedMessage = decryptMessage(encryptedMessage, keyPair.getPrivate());
		
		System.out.println("Original Message: "+ message);
		System.out.println("Public Key: "+ keyPair.getPublic());
		System.out.println("Private Key: "+ keyPair.getPrivate());
	}
	
}
