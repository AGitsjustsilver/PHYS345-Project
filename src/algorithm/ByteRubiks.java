package algorithm;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Direction;
import util.VectorKeys;

import ij.process.*;

import java.util.Arrays;

public class ByteRubiks extends Rubiks {

    private byte[] byteArray;

    public ByteRubiks(ImageProcessor ip, VectorKeys keys){
        super(ip, keys);
        this.byteArray = (byte[])ip.getPixels();
    }

    @Override
    protected void rowShift(){
        //        * 4. For each row of image:
        for(int row=0; row < this.HEIGHT; row++) {
//        *   a. calculate sum all values in row
//        *   b. take modulo 2
            // shift multiple times by the vector value at the index
            if(this.arrSum(true, row) % 2 == 0) {
//        *   c. if 0 -> right circular shift
                shiftElements(this.byteArray, Direction.RIGHT, row, true);
            }else {
//        *      else -> left circular shift
                shiftElements(this.byteArray, Direction.LEFT, row, true);
            }
        }
    }

    @Override
    protected void colShift(){
        //        * 5. For each column of image:
        for(int col = 0; col < this.WIDTH; col++) {
//        *   a. calculate the sum of all column values
//        *   b. compute modulo 2
            // shift multiple times by the vector value at the index
            if(this.arrSum(false, col) % 2 == 0) {
//        *   c. if 0 -> up circular shift
                shiftElements(this.byteArray, Direction.UP,col, false);
            }else {
//        *      else ->down circular shift
                shiftElements(this.byteArray, Direction.DOWN,col, false);
            }
        }
    }

    @Override
    protected void xorShift(int row, int col, boolean key){
        this.byteArray[col+(row*this.WIDTH)] = (byte) (this.byteArray[col+(row*this.WIDTH)] ^ this.getByteKey(key, col));
    }

    @Override
    protected void rotXorShift(int row, int col, boolean key){
        this.byteArray[col+(row*this.WIDTH)] = (byte) (this.byteArray[col+(row*this.WIDTH)] ^ (this.getByteKey(key, col) >> 1));
    }

    // Inteface methods
    @Override
    public void swap(byte[] array,int posA, int posB){
        byte temp = array[posA];
        array[posA] = array[posB];
        array[posB] = temp;
    }

    /**
     * Sums the upto 32-bit grey images
     * @param rowcol - true for row, false for column
     * @param pos - the index of which row/col to sum
     * @return the sum of row/col
     */
    public int arrSum(boolean rowcol, int pos){
        int sum =0;
        for(int i = 0; i < ((rowcol)? this.WIDTH : this.HEIGHT); i++){
            sum += arrByteAccess(rowcol,i,pos);
        }
        return sum;
    }

    /**
     * Differentiates the type of access whether it be across a row or across a column
     * @param rowcol - true = row, false = col
     * @param iterator - The changing bit
     * @param staticField - the exact col or row
     * @return - the value at that position based on traversal access
     */
    @Override
    public byte arrByteAccess(boolean rowcol, int iterator, int staticField) {
        return this.byteArray[this.position(rowcol,iterator,staticField)];
    }

    public int[] arrSumRGB(boolean rowcol, int index) throws NotImplementedException {
        return new int[0];
    }

    // object

    @Override
    public String toString() {
        return super.toString() + Arrays.toString(this.byteArray);
    }
}
