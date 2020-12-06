package algorithm;

import ij.ImagePlus;
import ij.plugin.ChannelSplitter;
import ij.plugin.RGBStackMerge;
import util.VectorKeys;

import java.util.Arrays;

public class RGBRubiks extends ByteRubiks {

    private ImagePlus[] rgb;
    private ByteRubiks r;
    private ByteRubiks g;
    private ByteRubiks b;

    public RGBRubiks(ImagePlus imagePlus, VectorKeys keys){
        super(imagePlus, keys);
        this.rgb = ChannelSplitter.split(imagePlus);
        r = new ByteRubiks(rgb[0], keys);
        g = new ByteRubiks(rgb[1], keys);
        b = new ByteRubiks(rgb[2], keys);
    }

    @Override
    public void encrypt() {
        this.r.encrypt();
        this.g.encrypt();
        this.b.encrypt();
        updatePluStack();
        merge();
    }

    @Override
    public void decrypt() {
        this.r.decrypt();
        this.g.decrypt();
        this.b.decrypt();
        updatePluStack();
        merge();
    }

    private void updatePluStack(){
        rgb[0] = r.imPlus;
        rgb[1] = g.imPlus;
        rgb[2] = b.imPlus;
        System.out.println("updated");
    }

    private void merge(){
        System.out.println("merged");
        this.imPlus = RGBStackMerge.mergeChannels(this.rgb, true);
        this.ip.convertToRGB();
    }

    @Override
    public String toString() {
        return "RGBRubiks{" +
                "rgb=" + Arrays.toString(rgb) +
                "\n, r=" + r.toString() +
                "\n, g=" + g.toString() +
                "\n, b=" + b.toString() +
                "\n}";
    }
}
