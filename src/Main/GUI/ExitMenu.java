package Main.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitMenu extends JMenuItem implements ActionListener {
    JFrame exitFrame;
    Display owner;

    /**
     * When the menu is clicked construct a new JFrame
     * @param ae ...
     */
    @Override
    public void actionPerformed(ActionEvent ae){
        exitFrame = new JFrame("Exit frame");
        JLabel label = new JLabel("Are you sure you want to exit?");
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        exitFrame.add(label, BorderLayout.NORTH);
        JButton noButton = new JButton("No");
        noButton.addActionListener(new noActionListener());
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new yesActionListener());
        exitFrame.add(noButton, BorderLayout.WEST);
        exitFrame.add(yesButton, BorderLayout.EAST);
        exitFrame.pack();
        exitFrame.setVisible(true);
    }

    /**
     * Listens to the yes button and exits when pressed
     */
    private class yesActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            owner.close();
            exitFrame.dispose();
        }
    }

    /**
     * Listens to no button closes JFrame when pressed
     */
    private class noActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            exitFrame.dispose();
        }
    }
    public ExitMenu(String s, Display d){
        super(s);
        this.addActionListener(this);
        owner = d;
    }
}