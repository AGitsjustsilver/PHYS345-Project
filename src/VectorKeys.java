import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;

public class VectorKeys {
    protected Vector<BigInteger> cVector;
    protected Vector<BigInteger> rVector;

    public VectorKeys(){
        this.cVector = null;
        this.rVector = null;
    }

    public VectorKeys(int cSize, int rSize){
        this.cVector = generateVector(cSize);
        this.rVector = generateVector(rSize);
    }

    private Vector<BigInteger> generateVector(int size){
        Vector<BigInteger> v = new Vector<>();
        for (int i = 0; i < size; i++) {
            v.add(new BigInteger(8, new SecureRandom()));
        }
        return v;
    }

    public BigInteger getKey(boolean key, int index){
        return ((key) ? this.rVector : this.cVector).get(index);
    }

    public int getIntKey(boolean key, int index){
        return getKey(key, index).intValue();
    }

    public short getShortKey(boolean key, int index){
        return getKey(key,index).shortValue();
    }

    public long getLongKey(boolean key, int index){
        return getKey(key, index).longValue();
    }



}
