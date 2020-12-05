package algorithm;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Direction;
import util.VectorKeys;

import ij.process.ImageProcessor;

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
        int w = ip.getWidth(), h = ip.getHeight();
        this.rArray = new byte[w*h];
        this.gArray = new byte[w*h];
        this.bArray = new byte[w*h];
        for(int row = 0; row < w; row++){
            for (int col = 0; col < h; col++) {
                int val = this.intArray[col+(row*w)];
                this.rArray[col+(row*w)] = (byte) (val & 0xff);
                this.gArray[col+(row*w)] = (byte) ((val >> 8) & 0xff);
                this.bArray[col+(row*w)] = (byte) ((val >> 16) & 0xff);
            }
        }
    }

    private void mergeChannels(){
        for (int row = 0; row < this.WIDTH; row++) {
            for (int col = 0; col < this.HEIGHT; col++) {
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
    }

    @Override
    public void decrypt(){
        super.decrypt();
        this.mergeChannels();
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

    @Override
    protected void rowXOR() {

    }

    @Override
    protected void colXOR() {

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

    }

    @Override
    protected void rotXorShift(int i, int j, boolean key) {// will probably change the value of RGB within the int

    }


    // object
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void swap(byte[] arr, int a, int b) {
        byte t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
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
        int sum = 0;
        for (int i : arrSumRGB(rowcol, index)) sum += i;
        return sum;
    }

    @Override
    public int arrIntAccess(boolean rowcol, int iterator, int staticField) {
        return 0;
    }

    @Override
    public int getIntKey(boolean key, int index) {
        return this.getVectKey().getIntKey(key,index);
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
            int val = arrIntAccess(rowcol,i,pos);
            sum[0] +=  val       & 0xff;
            sum[1] += (val >> 8) & 0xff;
            sum[2] += (val >>16) & 0xff;
        }
        return sum;
    }
}
