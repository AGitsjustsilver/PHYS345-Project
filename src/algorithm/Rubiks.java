package algorithm;

import ij.process.ImageProcessor;

import util.AccessHelpers;
import util.Direction;
import util.VectorKeys;

/*
 * The back end methods for the plugin interface
 * */

public abstract class Rubiks implements AccessHelpers {
    private VectorKeys vectKey;
    private final int ITERATOR_MAX = 1; // the higher the more secure

    protected ImageProcessor ip;
    protected final int WIDTH;
    protected final int HEIGHT;

    public Rubiks(ImageProcessor imageProcessor, VectorKeys keys){
        this.ip = imageProcessor;
        this.vectKey = keys;
        this.WIDTH = ip.getWidth();
        this.HEIGHT = ip.getHeight();

//        this.imgArr = this.ip.getIntArray();
    }

    //methods
    private boolean checkIteratorMax(int iter){
        return !(iter == ITERATOR_MAX);
    }


    public VectorKeys getVectKey(){
        return this.vectKey;
    }

    protected int position(boolean rowcol, int iterator, int staticField){
        return ((rowcol)? (iterator+(staticField*this.WIDTH)): (staticField+(iterator*this.WIDTH)));
    }

    public void encrypt(){
        int iter = 0;
        while (checkIteratorMax(iter)){
            iter++;
            rowShift();
            colShift();
            rowXOR();
            colXOR();
        }
    }

    public void decrypt(){
        int iter = 0;

        while(checkIteratorMax(iter)){
            iter++;
            colXOR();
            rowXOR();
            colShift();
            rowShift();
        }
    }

    /**
     * Rotates the row/column of the image pixels based on the key
     * @param d - The direction of the pixels being shifted
     * @param index - The row or column index being shifted
     * @param key - The vector key (True rVector, False cVector)
     */
    protected void shiftElements(byte[] byteArray, Direction d, int index, boolean key) {
        // col = UP/DOWN = true
        // row = LEFT/RIGHT = false;
        boolean direction = (d == Direction.UP)||(d == Direction.DOWN);
        int bound = ((direction)? this.WIDTH: this.HEIGHT)-1;
        int a;
        for (int keyShifts = 0; keyShifts < getIntKey(key, index); keyShifts++) {
            if((d == Direction.DOWN) || (d == Direction.RIGHT)) {
                for (int i = 0; i < bound; i++) {
                    // shifts -> for RIGHT and DOWN
                    a = this.position(direction, i, index);
                    swap(byteArray, a, a + 1);
                }
                swap(byteArray,bound - 1, bound);
            }else {
                for (int i = bound; i > 0; i--) {
                    // shifts <- for LEFT and UP
                    a = this.position(direction, i, index);
                    swap(byteArray,a - 1, a);
                }
                swap(byteArray,1, 0);
            }
        }
    }

    protected void rowXOR(){
        //        * 6. Using Vector Key C apply XOR to rows
        for (int i = 0; i < this.WIDTH; i++) {
            if (i%2!=0) {
                // a. Image[2i-1][j] (odd rows) get regular XOR of C[j]
                for (int j = 0; j < this.HEIGHT; j++) {
                    xorShift(i,j,false);
                }
            }else {
                // b. Image[2i][j] (even rows) get left bit shifted C[j]
                for (int j = 0; j < this.HEIGHT; j++) {
                    rotXorShift(i,j,false);
                }
            }
        }
    }

    protected void colXOR(){
        //        * 7. Using Vector Key R apply XOR to columns
        for (int i = 0; i < this.WIDTH; i++) {
            for (int j = 0; j < this.HEIGHT; j++) {
                if(j%2!=0) {
                    // a. Image[i][2j-1] (odd columns) get regular XOR of R[j]
                    xorShift(i,j,true);
                }else {
                    // b. Image[i][2j] (even columns) get left bit shifted R[j]
                    rotXorShift(i,j,true);
                }
            }
        }
    }

    public byte getByteKey(boolean key, int index) {
        return this.vectKey.getByteKey(key, index);
    }

    public int getIntKey(boolean key, int index) {
        return this.vectKey.getIntKey(key,index);
    }

    // actions that each individual rubiks type has to do with

    protected abstract void rowShift();
    protected abstract void colShift();

    protected abstract void xorShift(int i, int j, boolean key);
    protected abstract void rotXorShift(int i, int j, boolean key);

    // object
    @Override
    public String toString() {
        return "algorithm.Rubiks{" +
                "\nvectKey=" + vectKey.toString() +
                "\n, ITERMAX=" + ITERATOR_MAX +
                "\n, ip=" + ip +
                "\n}\n";
    }
}
