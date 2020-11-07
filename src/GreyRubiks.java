import ij.ImagePlus;
import ij.process.ImageProcessor;

public class GreyRubiks extends Rubiks {

    public GreyRubiks(ImagePlus imp, ImageProcessor ip){
        super.imp = imp;
        super.ip = ip;
        generateVector();
    }

    @Override
    public void encrypt() {
        /* Algorithm Steps
        * 1. Generate 2 random size arrays
        * 2. Determine the number of Iterations
        * 3. Increment iterator by 1
        * 4. For each row of image:
        *   a. calculate sum all values in row
        *   b. take modulo 2
        *   c. if 0 -> right circular shift
        *      else -> left circular shift
        * 5. For each column of image:
        * */
    }

    @Override
    public void decrypt() {

    }
}
