package Main.GUI;

import Main.Cell;
import Main.DataTypes.Data;
import Main.DataTypes.DataType;
import Main.Functions.Functions;
import Main.Manager;
import Main.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;


public class Display implements ActionListener {
    Table table;
    JTextField functionField;
    JTextField echoTextField;
    JPanel grid = new JPanel();
    JFrame frame = new JFrame("Haka Excel");
    JPanel textField = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel echoField = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private HashMap<String, JTextField> textfields = new HashMap<String, JTextField>();

    /**
     * Creates a display with menus, grid, echo text field and function text field
     * @param t the table where the data is managed
     */
    public Display(Table t) {
        table = t;

        createTextField();

        createEchoTextField();

        createMenu();

        createGrid();

        frame.add(textField, BorderLayout.NORTH);
        frame.add(echoField, BorderLayout.CENTER);
        frame.add(grid, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 400));

        frame.setVisible(true);
    }

    /**
     * closes the display when exit menu is choosen or typed as a function
     */
    public void close() {
        frame.dispose();
    }

    /**
     * the old table is replaced with a new, happens when loading a saved table
     * @param t the new table, probably returned from Manager.load(---)
     */
    public void updateTableAndGrid(Table t) {
        table = t;
        updateGrid();
    }

    /**
     * Listens to the function field, when it has a ';' as it's last char it excecutes the command typed in
     * @param ae ...
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String func = functionField.getText();
        if (func.length() != 0) {
            Character lastChar = func.charAt(func.length() - 1);
            if (lastChar.equals(new Character('\n')) || lastChar.equals(new Character(';'))) {
                try{
                selectCommandType(func);
                }catch (Exception e){}
            }
        }
    }

    /**
     * Refreshes the grid
     */
    private void updateGrid() {
        for (char i = 'a'; i <= table.getHeight(); i++) {
            for (int j = 1; j < table.getLength(); j++) {
                String id = Character.toString(i).concat(Integer.toString(j));
                Data d = table.getCell(id).getData();
                if (d != null) {
                    if (d.getType().equals(DataType.Double)) {

                        DecimalFormat df = new DecimalFormat("0.00");
                        String data = df.format(d.getData());

                        textfields.get(id).setText(data);
                    } else
                        textfields.get(id).setText(d.toString());
                } else {
                    textfields.get(id).setText("");
                }

            }

        }
    }

    /**
     * Creates the gird, used by the constructor
     */
    private void createGrid() {
        for (int i = 0; i < table.getLength(); i++) {
            JTextField header;
            if (i == 0)
                header = new JTextField("");
            else
                header = new JTextField(String.valueOf(i));
            header.setEditable(false);
            header.setHorizontalAlignment(JTextField.CENTER);

            grid.add(header);
        }

        for (char i = 'a'; i <= table.getHeight(); i++) {
            JTextField header = new JTextField(String.valueOf(i));
            header.setEditable(false);
            header.setHorizontalAlignment(JTextField.CENTER);
            grid.add(header);
            for (int j = 1; j < table.getLength(); j++) {
                JTextField field = new JTextField();

                field.setSize(new Dimension(75, 20));
                field.setMinimumSize(new Dimension(75, 20));
                field.setMaximumSize(new Dimension(80, 20));
                field.setEditable(false);

                grid.add(field);
                String id = Character.toString(i).concat(Integer.toString(j));
                textfields.put(id, field);
            }
        }
        grid.setLayout(new GridLayout(table.getHeight() - 'a' + 1 + 1, table.getLength()));
    }

    /**
     * Used by ctor, creates the menus
     */
    private void createMenu() {
        if (System.getProperty("os.name").equals("Mac OS X"))
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Stuff");
        // JMenuItem itemLoad = new JMenuItem("Load...");
        JMenuItem itemLoad = new LoadMenu("Load", this);
        //JMenuItem itemSave = new JMenuItem("Save...");
        JMenuItem itemSave = new SaveMenu("Save", this);
        //JMenuItem itemExit = new JMenuItem("Exit");
        JMenuItem itemExit = new ExitMenu("Exit", this);
        menu.add(itemLoad);
        menu.add(itemSave);
        menu.add(itemExit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    /**
     * Creates the function field, used by ctor
     */
    private void createTextField() {
        functionField = new JTextField("");
        functionField.setColumns(50);
        functionField.setHorizontalAlignment(JTextField.LEFT);
        functionField.addActionListener(this);
        textField.add(functionField, BorderLayout.WEST);
    }

    /**
     * Creates echo text filed, used by ctor
     */
    private void createEchoTextField() {
        echoTextField = new JTextField("");
        echoTextField.setColumns(50);
        echoTextField.setHorizontalAlignment(JTextField.LEFT);
        echoTextField.addActionListener(this);
        echoTextField.setEditable(false);
        echoField.add(echoTextField, BorderLayout.WEST);
    }

    /**
     * Selects the type of command, if it is relevant to display, it is excecuted
     * @param func the command passed from ActionListener
     */
    private void selectCommandType(String func) {
        func = func.substring(0, func.length() - 1);
        if (func.indexOf("echo") >= 0) {
            java.util.List<Cell> cells = Functions.getReferencedCells(func, table);
            if (cells.size() == 1) {
                if (cells.get(0).getFunc() != null)
                    echoTextField.setText(cells.get(0).getFunc());
                else if (cells.get(0).getData() != null) {
                    echoTextField.setText(cells.get(0).getData().toString());
                }
                else
                    echoTextField.setText("");
            }
        } else if (func.indexOf("save") >= 0) {
            Manager.save(func.split(" ")[1]);
            updateGrid();
        } else if (func.indexOf("load") >= 0) {
            table = Manager.load(func.split(" ")[1]);
            updateGrid();
        } else if (func.indexOf("exit") >= 0) {
            frame.dispose();
        } else {
            table.addFunction(func);
            updateGrid();
        }
    }

}
