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
    public boolean checkIteratorMax(int iter){
        return !(iter == ITERATOR_MAX);
    }


    public VectorKeys getVectKey(){
        return this.vectKey;
    }

    protected int position(boolean rowcol, int iterator, int staticField){
        return ((rowcol)? (iterator+(staticField*this.WIDTH)): (staticField+(iterator*this.WIDTH)));
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

    //TODO: CHANGE
    @Override
    public String toString() {
        return "algorithm.Rubiks{" +
                "\nvectKey=" + vectKey.toString() +
                "\n, ITERMAX=" + ITERATOR_MAX +
                "\n, ip=" + ip +
                "\n}\n";
    }
}
