package algorithm;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Direction;
import util.VectorKeys;

import ij.process.ImageProcessor;

import java.util.Arrays;

public class RGBRubiks extends Rubiks {

    private int[] intArray;
    private byte[] rArray;
    private byte[] gArray;
    private byte[] bArray;

    public RGBRubiks(ImageProcessor ip, VectorKeys keys){
        super(ip, keys);
        this.arrayInit(ip);
    }

    private void arrayInit(ImageProcessor ip){
        this.intArray = (int[])ip.getPixels();
        int len = this.intArray.length, w = ip.getWidth(), h = ip.getHeight(), pos = 0;
        this.rArray = new byte[len];
        this.gArray = new byte[len];
        this.bArray = new byte[len];
        for(int row = 0; row < h; row++){
            for (int col = 0; col < w; col++) {
                pos = (col + (row * w));
                int val = this.intArray[pos];
                this.rArray[pos] = (byte) (val & 0xff);
                this.gArray[pos] = (byte) ((val >> 8) & 0xff);
                this.bArray[pos] = (byte) ((val >> 16) & 0xff);
            }
        }
        System.out.println("LEN: " + len + " POS: " + pos);
    }

    private void mergeChannels(){
        for (int row = 0; row < this.HEIGHT; row++) {
            for (int col = 0; col < this.WIDTH; col++) {
                this.intArray[col+(row*this.WIDTH)] = (this.rArray[col+(row*this.WIDTH)]) |
                                                      (this.gArray[col+(row*this.WIDTH)] << 8) |
                                                      (this.bArray[col+(row*this.WIDTH)] << 16);
            }
        }
    }

    // super class

    @Override
    public void encrypt(){
        super.encrypt();
        this.mergeChannels();
        ip.setPixels(this.intArray);
    }

    @Override
    public void decrypt(){
        super.decrypt();
        this.mergeChannels();
        ip.setPixels(this.intArray);
    }

    @Override
    protected void rowShift() {
        for (int row = 0; row < this.WIDTH; row++) {
            int[] rgb = this.arrSumRGB(true, row);
            for (int j= 0; j < rgb.length; j++) {
                if (rgb[j] % 2 == 0) {
                    this.shiftElements(j, Direction.RIGHT, row, true);
                } else {
                    this.shiftElements(j, Direction.LEFT, row, true);
                }
            }
        }
    }

    @Override
    protected void colShift() {
        for (int col = 0; col < this.HEIGHT; col++) {
            int[] rgb = this.arrSumRGB(true, col);
            for (int j= 0; j < rgb.length; j++) {
                if (rgb[j] % 2 == 0) {
                    this.shiftElements(j,Direction.UP, col, false);
                }else{
                    this.shiftElements(j, Direction.DOWN, col, false);
                }
            }
        }
    }

    private void shiftElements(int channel, Direction direction, int index, boolean key){
        byte[] temp = channelChoice(channel);
        assert temp!=null;
        super.shiftElements(temp, direction, index, key);
        switch (channel){
            case 0:
                this.rArray = temp;
                break;
            case 1:
                this.gArray = temp;
                break;
            case 2:
                this.bArray = temp;
                break;
        }
    }

    private byte[] channelChoice(int channel){
        switch (channel){
            case 0:
                return this.rArray;
            case 1:
                return this.gArray;
            case 2:
                return this.bArray;
        }
        return null;
    }

    @Override
    protected void xorShift(int i, int j, boolean key) {// will probably change the value of RGB within the int
        int pos = (j + (i * this.WIDTH));
        this.rArray[pos] = (byte) (this.rArray[pos] ^ this.getByteKey(key, j));
        this.gArray[pos] = (byte) (this.gArray[pos] ^ this.getByteKey(key, j));
        this.bArray[pos] = (byte) (this.bArray[pos] ^ this.getByteKey(key, j));
    }

    @Override
    protected void rotXorShift(int i, int j, boolean key) {// will probably change the value of RGB within the int
        int pos = (j + (i * this.WIDTH));
        this.rArray[pos] = (byte) (this.rArray[pos] ^ (this.getByteKey(key, j) >> 1));
        this.gArray[pos] = (byte) (this.gArray[pos] ^ (this.getByteKey(key, j) >> 1));
        this.bArray[pos] = (byte) (this.bArray[pos] ^ (this.getByteKey(key, j) >> 1));
    }

    @Override
    public void swap(byte[] arr, int a, int b) {
        byte t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
    }

    @Override
    public byte arrByteAccess(boolean rowcol, int iterator, int staticField) throws NotImplementedException{
        return 0;
    }


    @Override
    public int arrSum(boolean rowcol, int index) throws NotImplementedException{
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
            int p = this.position(rowcol, i, pos);
            sum[0] += this.rArray[p] & 0xff;
            sum[1] += this.gArray[p] & 0xff;
            sum[2] += this.bArray[p] & 0xff;
        }
        return sum;
    }

    // object
    @Override
    public String toString() {
        return super.toString() + Arrays.toString(this.intArray);
    }

}
