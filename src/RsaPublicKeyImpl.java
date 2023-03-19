import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaPublicKeyImpl implements RSAPublicKey {

	private static final long serialVersionUID = 1L;
	
	private final BigInteger modulus;
    private final BigInteger publicExponent;
	
	public RsaPublicKeyImpl(BigInteger modules, BigInteger publicExponent) {
		this.modulus = modules;
		this.publicExponent = publicExponent;
	}
	
    public RsaPublicKeyImpl(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Create an RSAPublicKey object from the RSAPublicKeySpec object
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        
        this.modulus = publicKey.getModulus();
        this.publicExponent = publicKey.getPublicExponent();
    }
	
	@Override
	public String getAlgorithm() {
		return "RSA";
	}

	@Override
	public String getFormat() {
		return "X.509";
	}

	@Override
	public byte[] getEncoded() {
		try {
			RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
	        KeyFactory factory = KeyFactory.getInstance(getAlgorithm());
	        return factory.generatePublic(spec).getEncoded();
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	        throw new RuntimeException("RSA not supported", e);
	    }
	}

	@Override
	public BigInteger getModulus() {
		return this.modulus;
	}

	@Override
	public BigInteger getPublicExponent() {
		return this.publicExponent;
	}

}
