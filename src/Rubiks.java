import ij.*;
import ij.process.ImageProcessor;
import java.math.BigInteger;
import java.util.Vector;
import java.security.SecureRandom;
/*
 * The back end methods for the plugin interface
 * */

abstract class Rubiks {
    private Vector<BigInteger> vectR; // true
    private Vector<BigInteger> vectC; // false
    private int bitSize;
    private final int ITERMAX = 1;


    public ImagePlus imp;
    public ImageProcessor ip;
    public int[][] imgArr;

    public Rubiks(){
        this.imp = new ImagePlus();
        this.ip = null;
        this.generateVectors();
    }

    public Rubiks(String path){
        this.imp = new ImagePlus(path);
        this.ip = imp.getProcessor();
        this.generateVectors();
    }

    public Rubiks(ImagePlus imagePlus, ImageProcessor imageProcessor){
        this.imp = imagePlus;
        this.ip = imageProcessor;
        this.generateVectors();
    }

    //methods
    public void generateVectors(){
        this.bitSize = ip.getBitDepth();
        this.vectR = this.generateVector(ip.getWidth());
        this.vectC = this.generateVector(ip.getHeight());
    }

    public boolean checkIter(int iter){
        return (iter == ITERMAX);
    }

    public int arrSum(int[] arr){
        int sum = 0;
        for (int i: arr) sum += i;
        return sum;
    }

    public void shift(Dir d, int index){
        // col = UP/DOWN = true
        // row = LEFT/RIGHT = false;
        boolean direction = (d == Dir.UP)||(d == Dir.DOWN);
        if(direction) {
            this.colSwitch();
            d = (d == Dir.UP)? Dir.LEFT: Dir.RIGHT;
        }
        int bound = this.imgArr[index].length-1;
        if (d == Dir.RIGHT){
            // shift ->
            for (int row = 0; row < bound; row++){
                swap(this.imgArr[index], row, row+1);
            }
            swap(this.imgArr[index], bound-1, bound);
        }else {
            // shift <-
            for (int row = bound; row > 0; row--){
                swap(this.imgArr[index], row-1, row);
            }
            swap(this.imgArr[index], 1, 0);
        }
        if(direction)
            this.colSwitch();
    }

    public void xorShift(int i, int j, boolean key){
        this.imgArr[i][j] = this.imgArr[i][j] ^ getKey(key, j);
    }

    public void xorRotShift(int i, int j, boolean key){
        this.imgArr[i][j] = this.imgArr[i][j] ^ ( getKey(key, j) << 1);
    }

    private int getKey(boolean key, int pos){
        return ((key)? this.vectR.get(pos): this.vectC.get(pos)).intValue();
    }

    private void swap(int[] arr, int indexA, int indexB){
        int temp = arr[indexA];
        arr[indexA] = arr[indexB];
        arr[indexB] = temp;
    }

    private void colSwitch(){
        int rowLen = ip.getWidth(),
                colLen = ip.getHeight();
        int[][] temp = new int[rowLen][colLen];
        for (int row = 0; row < rowLen; row++){
            for (int col = 0; col < colLen; col++){
                temp[col][row] = this.imgArr[row][col];
            }
        }
        this.imgArr = temp;
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

enum Dir {
    UP,
    DOWN,
    LEFT,
    RIGHT
}