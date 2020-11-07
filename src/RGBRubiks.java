import ij.ImagePlus;
import ij.process.ImageProcessor;

public class RGBRubiks extends GreyRubiks {

    public RGBRubiks(ImagePlus imp, ImageProcessor ip){
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
