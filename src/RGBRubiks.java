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
}
