import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

public class RsaPrivateKeyImpl implements RSAPrivateKey {

	private static final long serialVersionUID = 1L;
	
	private BigInteger modulus;
    private BigInteger privateExponent;
	
    public RsaPrivateKeyImpl(BigInteger modulus, BigInteger privateExponent) {
        this.modulus = modulus;
        this.privateExponent = privateExponent;
    }
    
	@Override
	public String getAlgorithm() {
		return "RSA";
	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getEncoded() {
		return null;
	}

	@Override
	public BigInteger getModulus() {
		return this.modulus;
	}

	@Override
	public BigInteger getPrivateExponent() {
		return this.privateExponent;
	}

}
