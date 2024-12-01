package test;

import vista.Automata;

import javax.swing.*;

public class Vista {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Vista");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(900, 700);
        frame.setContentPane(new Automata());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
