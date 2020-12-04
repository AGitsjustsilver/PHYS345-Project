import ij.process.ImageProcessor;

import java.util.Arrays;
/*
 * The back end methods for the plugin interface
 * */

abstract class Rubiks {
    private VectorKeys vectKey;
    private final int ITERATOR_MAX = 1; // the higher the more secure

    public ImageProcessor ip;
    public int width;
    public int height;
    public int[][] imgArr;

    public Rubiks(ImageProcessor imageProcessor, VectorKeys keys){
        this.ip = imageProcessor;
        this.vectKey = keys;
        this.width = ip.getWidth();
        this.height = ip.getHeight();
        this.imgArr = this.ip.getIntArray();
    }

    //methods
    public boolean checkIteratorMax(int iter){
        return !(iter == ITERATOR_MAX);
    }

    public int arrSum(int[] arr){
        int sum = 0;
        for (int i: arr) sum += i;
        return sum;
    }

    public int getKey(boolean key, int pos){
        return this.vectKey.getIntKey(key, pos);
    }

    public void swap(int[] arr, int indexA, int indexB){
        int temp = arr[indexA];
        arr[indexA] = arr[indexB];
        arr[indexB] = temp;
    }

    public void colSwitch(){
        int rowLen = this.imgArr.length,
            colLen = this.imgArr[0].length;
        int[][] temp = new int[colLen][rowLen];
        for (int row = 0; row < colLen; row++){
            for (int col = 0; col < rowLen; col++){
                temp[row][col] = this.imgArr[col][row];
            }
        }
        this.imgArr = temp;
    }

    // actions that each individual rubiks type has to do with
    public abstract void encrypt();
    public abstract void decrypt();

    protected abstract void rowShift();
    protected abstract void colShift();
    protected abstract void rowXOR();
    protected abstract void colXOR();

    protected abstract void shiftElements(Direction direction, int index, boolean key);
    protected abstract void xorShift(int i, int j, boolean key);
    protected abstract void rotXorShift(int i, int j, boolean key);

    // object


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int[] i : this.imgArr) {
            str.append(Arrays.toString(i));
            str.append("\n");
        }
        return "Rubiks{" +
                "\nvectKey=" + vectKey.toString() +
                "\n, ITERMAX=" + ITERATOR_MAX +
                "\n, ip=" + ip +
                "\n, imgArr=" + str.toString() +
                "\n}\n";
    }
}
