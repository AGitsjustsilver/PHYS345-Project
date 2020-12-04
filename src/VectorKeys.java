import ij.ImagePlus;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;

public class VectorKeys {
    private int bitSize; // Default bit size is 8
    protected Vector<BigInteger> rVector; // true
    protected Vector<BigInteger> cVector; // false

    public VectorKeys(){
        this(8, 0,0);
    }

    public VectorKeys(ImagePlus imagePlus){
        this(imagePlus.getBitDepth(), imagePlus.getWidth(), imagePlus.getHeight());
    }

    public VectorKeys(int bSize, int width, int height){
        this.bitSize = bSize;
        this.rVector = generateVector(width);
        this.cVector = generateVector(height);
    }

    //public sector

        // retrievals
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

    public static void updateKeys(VectorKeys currentInstance, ImagePlus imp){
        currentInstance.rVector = VectorKeys.generateVector(imp, imp.getWidth());
        currentInstance.cVector = VectorKeys.generateVector(imp, imp.getHeight());
    }

    // private sector
    private Vector<BigInteger> generateVector(int size){
        if(size<1) return null;
        Vector<BigInteger> v = new Vector<>();
        for (int i = 0; i < size; i++) {
            v.add(new BigInteger(this.bitSize, new SecureRandom()));
        }
        return v;
    }
    private static Vector<BigInteger> generateVector(ImagePlus ip, int size){
        if(size<1) return null;
        Vector<BigInteger> v = new Vector<>();
        for (int i = 0; i < size; i++) {
            v.add(new BigInteger(ip.getBitDepth(), new SecureRandom()));
        }
        return v;
    }

    // object methods


    @Override
    public String toString() {
        return "VectorKeys{" +
                "\nbitSize=" + bitSize +
                "\n, rVector=" + rVector.toString() +
                "\n, cVector=" + cVector.toString() +
                "\n}\n";
    }

}
