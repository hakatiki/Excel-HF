package Main.GUI;

import Main.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveMenu extends JMenuItem implements ActionListener {
    JFrame saveFrame;
    JTextField textField;

    /**
     * When the menu is selected it creates a new JFrame
     * @param ae ...
     */
    @Override
    public void actionPerformed(ActionEvent ae){
        saveFrame = new JFrame("Save frame");
        saveFrame.setSize(new Dimension(300,400));
        JLabel label = new JLabel("Enter file path:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        saveFrame.add(label, BorderLayout.NORTH);

        textField = new JTextField(System.getProperty("user.dir"));
        textField.setSize(new Dimension(100, 30));
        textField.setMinimumSize(new Dimension(100, 30));
        textField.setMaximumSize(new Dimension(110, 35));
        textField.addActionListener(new textFieldListener());
        JButton button = new JButton("Save");
        button.addActionListener(new textFieldListener());
        saveFrame.add(button, BorderLayout.SOUTH);
        saveFrame.add(textField,BorderLayout.CENTER);
        saveFrame.pack();
        saveFrame.setVisible(true);
    }

    /**
     * if entes is pressed it saves the table to the specified place and name
     */
    private class textFieldListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            String path = textField.getText();
            Manager.save(path);
            saveFrame.dispose();
        }
    }
    public SaveMenu(String s, Display d){
        super(s);
        this.addActionListener(this);
    }
}
