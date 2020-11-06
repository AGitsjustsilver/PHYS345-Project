/*
* The plugin interface face for ImageJ
* */


import ij.IJ;
import ij.gui.GUI;
import ij.plugin.frame.PlugInFrame;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Rubiks_ extends PlugInFrame implements ActionListener {
    private Panel panel;
    private int previousID;
    private static Frame instance;

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
        panel.setLayout(new GridLayout(3,3,3,3));
        addButton("Grey-Encrypt");
        addButton("Grey-Decrypt");
        add(panel);

        pack();
        GUI.center(this);
        setVisible(true);
    }

    void addButton(String label) {
        Button b = new Button(label);
        b.addActionListener(this);
        b.addKeyListener(IJ.getInstance());
        panel.add(b)
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
