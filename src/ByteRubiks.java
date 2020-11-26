import ij.ImagePlus;
import ij.process.*;

public class ByteRubiks extends Rubiks {

    private ByteProcessor bp = null;

    public ByteRubiks(){
        super();
    }

    public ByteRubiks(ImagePlus imp, ImageProcessor ip){
        super.imp = imp;
        super.ip = ip;
        generateVectors();
    }

    @Override
    public void encrypt() {
//         Algorithm Steps
//        * 1. Generate 2 different size arrays R and C
//        * 2. Determine the max number of Iterations //
        this.bp = new ByteProcessor(ip,true);
        int iter = 0;
        while (!checkIter(iter)) {
//        * 3. Increment iterator by 1
            iter++;
//        * 4. For each row of image:
            for(int row=0; row < this.ip.getWidth(); row++) {
//        *   a. calculate sum all values in row
//        *   b. take modulo 2
//        *   c. if 0 -> right circular shift
//        *      else -> left circular shift
            }
//        * 5. For each column of image:
            for(int col=0; col < this.ip.getHeight(); col++) {
//        *   a. calculate the sum of all column values
//        *   b. compute modulo 2
//        *   c. if 0 -> up circular shift
//        *      else -> down circular shift
            }
//        * 6. Using array C apply XOR
//        *   a. apply XOR to
//        * 7.
//        * 8. if iter and iterMax are equal then its done else go again
        }
    }

    @Override
    public void decrypt() {

    }
}
