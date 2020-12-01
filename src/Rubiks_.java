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

    final String[] choices = {"Grey-Encrypt",
                              "Grey-Decrypt",
                              "RGB-Encrypt",
                              "RGB-Decrypt"};

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
        for (int i=0; i<choices.length;i++){
            addButton(choices[i],i);
        }
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
        new Runner(label, imp);
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

        Runner(String command, ImagePlus imp){
            super(command);
            this.command = command;
            this.imp = imp;
            //TODO: differentiate between n-bit GREYS and RGB
            this.choice = Choices.GREY;
            switch (this.choice){
                case GREY:
                    this.rubiks = new ByteRubiks();
                    break;
                case RGB:
                    break;
            }

            setPriority(Math.max(getPriority()-2, MIN_PRIORITY));
            start();
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
                    if (command.equals(choices[0])){
                        rubiks.encrypt();
                        msg = choices[0] + " took ";
                    }else if (command.equals(choices[1])) {
                        rubiks.decrypt();
                        msg = choices[1] + " is not implemented ";
                    }
                    break;
                case RGB:
                    rubiks = new RGBRubiks(imp, ip);
                    if (command.equals(choices[2])) {
                        rubiks.encrypt();
                        msg = choices[2] + " is not implemented ";
                    }else if (command.equals(choices[3])) {
                        rubiks.decrypt();
                        msg = choices[3] + " is not implemented ";
                    }
                    break;
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