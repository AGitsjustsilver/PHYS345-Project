/*
* The plugin face for ImageJ
* Button Menu
*
* Code Written By: Alessandro Guaresti
* */

import ij.*;
import ij.gui.*;
import ij.plugin.frame.PlugInFrame;
import ij.process.ImageProcessor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Rubiks_ extends PlugInFrame implements ActionListener {
    private Panel panel;
    private int previousID;
    private static Frame instance;
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
        panel.setLayout(new GridLayout(3,2,3,3));
        addButton("Reset", 0);
        addButton("New-Vectors", 1);
        addButton("Grey-Encrypt",2 );
        addButton("Grey-Decrypt", 3);
        addButton("RGB-Encrypt", 4);
        addButton("RGB-Decrypt", 5);
        add(panel);

        pack();
        GUI.center(this);
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
    public void actionPerformed(ActionEvent e) { // might be the point at which it re runs?
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
        int id = imp.getID();
        if (id!=previousID){
            imp.getProcessor().snapshot();
        }
        previousID = id;
        String label = e.getActionCommand();
        new Runner(label, imp, Choices.GREY, this.rubiks);
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
            this.choice = this.imageType();
            this.vect = v;
            this.rubiks = r;
            setPriority(Math.max(getPriority()-2, MIN_PRIORITY));
            start();
        }

        private Choices imageType(){
             switch (this.imp.getCompositeMode()){
                 case IJ.GRAYSCALE:
                     return Choices.GREY;
                 case IJ.COMPOSITE:
                 case IJ.COLOR:
                     return Choices.RGB;
                 default:
                     return null;
             }
        }

        public void run(){
            try {
                runCommand(command, imp, rubiks);
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

        void runCommand(String command, ImagePlus imp, Rubiks rubiks){
            ImageProcessor ip = imp.getProcessor();
            IJ.showStatus(command + "...");
            long startTime = System.currentTimeMillis();
            String msg = "";
            switch (this.choice){
                case GREY:
                    rubiks = new ByteRubiks(ip);
                    if (command.equals("Grey-Encrypt")){
                        rubiks.encrypt();
                        msg = "Grey-Encrypt took ";
                    }else if (command.equals("Grey-Decrypt")) {
                        rubiks.decrypt();
                        msg = "Grey-Decrypt took ";
                    }
                    break;
                case RGB:
                    rubiks = new RGBRubiks(imp, ip);
                    if (command.equals("RGB-Encrypt")) {
                        rubiks.encrypt();
                        msg = "RGB-Encrypt took ";
                    }else if (command.equals("RGB-Decrypt")) {
                        rubiks.decrypt();
                        msg = "RGB-Decrypt took ";
                    }
                    break;
                default:
                    if (command.equals("reset")){
                        ip.reset();
                    }else if (command.equals("New-Vectors")){

                    }
            }
            imp.updateAndDraw();
            imp.unlock();
            IJ.showStatus(msg + (System.currentTimeMillis()-startTime)+" milliseconds");
        }
    }
}

enum Choices{
    GREY,
    RGB
}