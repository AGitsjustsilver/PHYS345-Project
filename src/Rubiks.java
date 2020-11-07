import ij.*;
import ij.process.ImageProcessor;
import java.lang.Number;
/*
 * The back end methods for the plugin interface
 * */

abstract class Rubiks {
    private Vector vectR;
    private Vector vectC;
    private int bitSize;

    public ImagePlus imp;
    public ImageProcessor ip;

    //methods
    public void generateVector(){

    }

    private boolean shiftLeft(){
        return false;
    }

    private boolean shiftRight(){
        return false;
    }

    // actions that each individual rubiks type has to do with
    public abstract void encrypt();
    public abstract void decrypt();

    private class Vector<E extends Number>{
        Vector(int size){

        }
    }
}