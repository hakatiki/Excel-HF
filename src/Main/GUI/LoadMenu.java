package Main.GUI;

import Main.Manager;
import Main.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadMenu extends JMenuItem implements ActionListener {
    JFrame loadFrame;
    JTextField textField;
    Display owner;

    /**
     * Creates a new JFrame when menu is selected
     * @param ae ...
     */
    @Override
    public void actionPerformed(ActionEvent ae){
        loadFrame = new JFrame("Load frame");
        loadFrame.setSize(new Dimension(300,400));
        JLabel label = new JLabel("Enter file path:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        loadFrame.add(label, BorderLayout.NORTH);

        textField = new JTextField(System.getProperty("user.dir"));
        textField.setSize(new Dimension(100, 30));
        textField.setMinimumSize(new Dimension(100, 30));
        textField.setMaximumSize(new Dimension(110, 35));
        textField.addActionListener(new textFieldListener());
        JButton button = new JButton("Load");
        button.addActionListener(new textFieldListener());
        loadFrame.add(button, BorderLayout.SOUTH);
        loadFrame.add(textField,BorderLayout.CENTER);
        loadFrame.pack();
        loadFrame.setVisible(true);
    }

    /**
     * When enter is hit it loads the specified file
     */
    private class textFieldListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            String path = textField.getText();
            Table t = Manager.load(path);
            owner.updateTableAndGrid(t);
            loadFrame.dispose();
        }
    }
    public LoadMenu(String s, Display d){
        super(s);
        this.addActionListener(this);
        owner = d;
    }
}