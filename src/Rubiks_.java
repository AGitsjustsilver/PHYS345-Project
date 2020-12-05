/*
* The plugin face for ImageJ
* Button Menu
*
* Code Written By: Alessandro Guaresti
* */

import algorithm.*;

import ij.*;
import ij.gui.*;
import ij.io.LogStream;
import ij.plugin.frame.PlugInFrame;
import ij.process.ImageProcessor;

import util.Choices;
import util.VectorKeys;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Rubiks_ extends PlugInFrame implements ActionListener {
    private Panel panel;
    private int previousID;
    private static Frame instance;

    private ImagePlus currImage;
    private VectorKeys vect;
    private Rubiks rubiks;

    public Rubiks_() {
        super("Rubik's Encrypt/Decrypt");
        if(instance!=null){
            instance.toFront();
            return;
        }
        instance = this;
        addKeyListener(IJ.getInstance());

        setLayout(new FlowLayout());
        panel = new Panel();
        panel.setLayout(new GridLayout(2,2,3,3));
        addButton("Reset", 0);
        addButton("New-Vectors", 1);
        addButton("Encrypt",2 );
        addButton("Decrypt", 3);
        add(panel);

        pack();
        GUI.center(this);

        this.currImage = null;
        this.vect = new VectorKeys();

        LogStream.redirectSystem();

        setVisible(true);
    }

    //TODO: Fix potential recreation of instances based on button press.
    void addButton(String label, int index) {
        Button b = new Button(label);
        b.addActionListener(this);
        b.addKeyListener(IJ.getInstance());
        panel.add(b, index);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePlus imp = WindowManager.getCurrentImage();
        if(imp==null) {
            IJ.beep();
            IJ.showStatus("No Image");
            previousID = 0;
            return;
        }
        if(!imp.lock()) {
            previousID = 0;
            return;
        }
        int id = imp.getID();// vectors still resetting
        if (id!=previousID){
            System.out.println("New Image!");
            imp.getProcessor().snapshot();
            this.currImage = imp;
            this.vect = new VectorKeys(imp);
        }
        previousID = id;
        String label = e.getActionCommand();
        new Runner(label, imp, this.vect, this.rubiks);
    }

    @Override
    public void windowClosing(WindowEvent e){
        LogStream.revertSystem();
        super.windowClosing(e);
    }

    public void processWindowEvent(WindowEvent e){
        super.processWindowEvent(e);
        if(e.getID()==WindowEvent.WINDOW_CLOSING){
            instance = null;
        }
    }

    class Runner extends Thread {
        private String command;
        private Choices choice;
        private Rubiks rubiks;
        private ImagePlus imp;
        private VectorKeys vect;

        Runner(String command, ImagePlus imp, VectorKeys v, Rubiks r){
            super(command);
            this.command = command;
            this.imp = imp;
            this.choice = (command.equals("Reset")||command.equals("New-Vectors"))? Choices.CMD: this.imageType();
            System.out.println(choice);
            this.vect = v;
            this.rubiks = r;
            setPriority(Math.max(getPriority()-2, MIN_PRIORITY));
            start();
        }

        private Choices imageType(){
            int type = this.imp.getType();
            System.out.println("TYPE = " + type);
             switch (type){
                 case ImagePlus.GRAY8:
                 case ImagePlus.GRAY16:
                 case ImagePlus.GRAY32:
                     return Choices.GREY;
                 case ImagePlus.COLOR_256:
                 case ImagePlus.COLOR_RGB:
                     return Choices.RGB;
                 default:
                     return Choices.CMD;
             }
        }

        public void run(){
            try {
                runCommand(command, imp);
            } catch (OutOfMemoryError e){
                IJ.outOfMemory(command);
                if (imp!=null) imp.unlock();
            } catch (Exception e) {
                CharArrayWriter caw = new CharArrayWriter();
                PrintWriter pw = new PrintWriter(caw);
                e.printStackTrace(pw);
                IJ.log(caw.toString());
                IJ.showStatus("");
                if (imp!=null) imp.unlock();
            }
        }

        void runCommand(String command, ImagePlus imp){
            ImageProcessor ip = imp.getProcessor();
            IJ.showStatus(command + "...");
            long startTime = System.currentTimeMillis();
            String msg = "";
            switch (this.choice){
                case GREY:
                    rubiks = new ByteRubiks(ip,this.vect);
                    if (command.equals("Encrypt")){
                        rubiks.encrypt();
                        msg = "Grey-Encrypt took: ";
                    }else if (command.equals("Decrypt")) {
                        rubiks.decrypt();
                        msg = "Grey-Decrypt took: ";
                    }
                    System.out.println(rubiks.toString());
                    break;
                case RGB:
                    rubiks = new RGBRubiks(ip,this.vect);
                    if (command.equals("Encrypt")) {
                        rubiks.encrypt();
                        msg = "RGB-Encrypt took: ";
                    }else if (command.equals("Decrypt")) {
                        rubiks.decrypt();
                        msg = "RGB-Decrypt took: ";
                    }
                    System.out.println(rubiks.toString());
                    break;
                case CMD:
                    if (command.equals("Reset")){
                        ip.reset();
                        msg = "Image Reset. It took: ";
                        System.out.println("Resetting Image");
                    }else if (command.equals("New-Vectors")){
                        GenericDialog gd = new GenericDialog("Warning");
                        gd.addMessage("This recreates the Vectors for the image.\n Further Decryption will not work.");
                        gd.showDialog();
                        if(!gd.wasCanceled()) {
                            VectorKeys.updateKeys(this.vect, imp);
                            msg = "Keys were remade. It took: ";
                            System.out.println("Keys Remade\n" + this.vect.toString());
                        }else {
                            msg = "Keys stay the same. It took: ";
                        }
                    }
            }
            imp.updateAndDraw();
            imp.unlock();
            IJ.showStatus(msg + (System.currentTimeMillis()-startTime)+" milliseconds");
        }
    }

    public static void main(String[] args) {
        int w = 4, h = 3;
        int[] arr = {0,1,2,3,
                     4,5,6,7,
                     8,9,10,11};

    }
}