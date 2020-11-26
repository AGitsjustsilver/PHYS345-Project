import ij.*;
import ij.process.ImageProcessor;
import java.math.BigInteger;
import java.util.Vector;
import java.security.SecureRandom;
/*
 * The back end methods for the plugin interface
 * */

abstract class Rubiks {
    private Vector<BigInteger> vectR;
    private Vector<BigInteger> vectC;
    private int bitSize;
    private final int ITERMAX = 1;

    public ImagePlus imp;
    public ImageProcessor ip;

    //methods
    public void generateVectors(){
        this.bitSize = ip.getBitDepth();
        this.vectR = this.generateVector(ip.getWidth());
        this.vectC = this.generateVector(ip.getHeight());
    }

    public boolean checkIter(int iter){
        return (iter == ITERMAX);
    }

    public Integer rowSum(){return null;}

    public Integer colSum(){return null;}

    private boolean shiftLeft(){
        return false;
    }

    private boolean shiftRight(){
        return false;
    }

    private Vector<BigInteger> generateVector(int size){
        /*Each vector is to take a random */
        Vector<BigInteger> v = new Vector<>();
        for (int i = 0; i < size; i++) {
            v.add(new BigInteger(this.bitSize, new SecureRandom()));
        }
        return v;
    }

    // actions that each individual rubiks type has to do with
    public abstract void encrypt();
    public abstract void decrypt();

    public static void main(String[] args) {
        int size = 80;
        Vector<BigInteger> v = new Vector<>(size);
        for (int i = 0; i < size; i++) {
            v.add(new BigInteger(8, new SecureRandom()));
        }
        System.out.println(v.toString());
    }
}