import ij.ImagePlus;
import ij.process.ImageProcessor;

public class RGB_Rubiks extends Grey_Rubiks {

    public RGB_Rubiks(ImagePlus imp, ImageProcessor ip){
        super(imp, ip);
    }

    @Override
    public void encrypt() {
        super.encrypt();
    }

    @Override
    public void decrypt() {
        super.decrypt();
    }
}
