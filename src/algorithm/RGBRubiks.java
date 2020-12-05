package algorithm;

import util.Direction;
import util.VectorKeys;

import ij.process.ImageProcessor;

public class RGBRubiks extends Rubiks {

    public RGBRubiks(ImageProcessor ip, VectorKeys keys){
        super(ip, keys);
    }

    @Override
    public void encrypt() {
        int iter = 0;
    }

    @Override
    public void decrypt() {

    }

    @Override
    protected void rowShift() {

    }

    @Override
    protected void colShift() {

    }

    @Override
    protected void rowXOR() {

    }

    @Override
    protected void colXOR() {

    }


    @Override
    public void shiftElements(Direction direction, int index, boolean key){

    }

    @Override
    protected void xorShift(int i, int j, boolean key) {

    }

    @Override
    protected void rotXorShift(int i, int j, boolean key) {

    }


    // object
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void swap(int posA, int posB) {

    }

    @Override
    public byte arrByteAccess(boolean rowcol, int iterator, int staticField) {
        return 0;
    }

    @Override
    public byte getByteKey(boolean key, int index) {
        return 0;
    }

    @Override
    public int arrSum(boolean rowcol, int index) {
        return 0;
    }

    @Override
    public int arrIntAccess(boolean rowcol, int iterator, int staticField) {
        return 0;
    }

    @Override
    public int getIntKey(boolean key, int index) {
        return 0;
    }

    /**
     * Sums the RGB pixels of the RGB images
     * @param rowcol - true = row, false = col
     * @param pos - the row/col to sum
     * @return returns a 3 wide array of summed values [0-Red, 1-Green, 2-Blue]
     */
    public int[] arrSumRGB(boolean rowcol, int pos){
        int[] sum = {0, 0, 0};
        for (int i = 0; i < ((rowcol)? this.WIDTH :this.HEIGHT); i++) {
            sum[0] +=  arrIntAccess(rowcol,i,pos)       & 0xff;
            sum[1] += (arrIntAccess(rowcol,i,pos) >>8)  & 0xff;
            sum[2] += (arrIntAccess(rowcol,i,pos) >>16) & 0xff;
        }
        return sum;
    }
}
