package algorithm;

import jdk.nashorn.internal.ir.annotations.Ignore;
import util.Direction;
import util.VectorKeys;

import ij.process.*;

public class ByteRubiks extends Rubiks {

    private byte[] byteArray;

    public ByteRubiks(ImageProcessor ip, VectorKeys keys){
        super(ip, keys);
        this.byteArray = (byte[])ip.getPixels();
    }

    @Override
    public void encrypt() {
//         Algorithm Steps
//        * 1. Generate 2 different size arrays R and C
//        * 2. Determine the max number of Iterations //
        int iter = 0;

        while (checkIteratorMax(iter)) {
//        * 3. Increment iterator by 1
            iter++;
            this.rowShift();
            this.colShift();
            this.rowXOR();
            this.colXOR();

//        * 8. if iter and iterMax are equal then its done else go again
        }

        // update ImageProcessor
//        this.ip.setIntArray(this.imgArr);
    }

    @Override
    public void decrypt() {
        int iter = 0;

        while(checkIteratorMax(iter)){
            iter++;
            this.colXOR();
            this.rowXOR();
            this.colShift();
            this.rowShift();
        }

//        this.ip.setIntArray(this.imgArr);
    }

    /**
     * Rotates the row/column of the image pixels based on the key
     * @param d - The direction of the pixels being shifted
     * @param index - The row or column index being shifted
     * @param key - The vector key (True rVector, False cVector)
     */
    @Override
    protected void shiftElements(Direction d, int index, boolean key) {
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
                    swap(a, a + 1);
                }
                swap(bound - 1, bound);
            }else {
                for (int i = bound; i > 0; i--) {
                    // shifts <- for LEFT and UP
                    a = this.position(direction, i, index);
                    swap(a - 1, a);
                }
                swap(1, 0);
            }
        }
    }

    @Override
    protected void rowShift(){
        //        * 4. For each row of image:
        for(int row=0; row < this.WIDTH; row++) {
//        *   a. calculate sum all values in row
//        *   b. take modulo 2
            // shift multiple times by the vector value at the index
            if(this.arrSum(true, row) % 2 == 0) {
//        *   c. if 0 -> right circular shift
                shiftElements(Direction.RIGHT, row, true);
            }else {
//        *      else -> left circular shift
                shiftElements(Direction.LEFT, row, true);
            }
        }
    }

    @Override
    protected void colShift(){
        //        * 5. For each column of image:
        for(int col = 0; col < this.HEIGHT; col++) {
//        *   a. calculate the sum of all column values
//        *   b. compute modulo 2
            // shift multiple times by the vector value at the index
            if(this.arrSum(false, col) % 2 == 0) {
//        *   c. if 0 -> up circular shift
                shiftElements(Direction.UP,col, false);
            }else {
//        *      else ->down circular shift
                shiftElements(Direction.DOWN,col, false);
            }
        }
    }

    @Override
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

    @Override
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
    public void swap(int posA, int posB){
        byte temp = this.byteArray[posA];
        this.byteArray[posA] = this.byteArray[posB];
        this.byteArray[posB] = temp;
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

    @Override
    public byte getByteKey(boolean key, int index) {
        return this.getVectKey().getByteKey(key,index);
    }

    @Ignore
    public int arrIntAccess(boolean rowcol, int iterator, int staticField) {
        return 0;
    }

    @Override
    public int getIntKey(boolean key, int index) {
        return this.getVectKey().getIntKey(key, index);
    }

    @Ignore
    public int[] arrSumRGB(boolean rowcol, int index) {
        return new int[0];
    }

    // object

    @Override
    public String toString() {
        return super.toString();
    }
}
