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
        private ImagePlus imp;

        Runner(String command, ImagePlus imp){
            super(command);
            this.command = command;
            this.imp = imp;
            setPriority(Math.max(getPriority()-2, MIN_PRIORITY));
            start();
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
            if (command.equals(choices[0])){
                msg = choices[0] + " is not implemented ";
            }else if (command.equals(choices[1])) {
                msg = choices[1] + " is not implemented ";
            }else if (command.equals(choices[2])) {
                msg = choices[2] + " is not implemented ";
            }else if (command.equals(choices[3])) {
                msg = choices[3] + " is not implemented ";
            }
            imp.updateAndDraw();
            imp.unlock();
            IJ.showStatus(msg + (System.currentTimeMillis()-startTime)+" milliseconds");
        }
    }
}
