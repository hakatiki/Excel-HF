package Main;

import Main.Functions.Functions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Table implements Serializable {
    private int length = 15;
    private char height = 't';
    private HashMap<String, Cell> cells = new HashMap<String, Cell>();

    /**
     * The loop should be use with equality for the hight and for the length of the table with less than, why?
     * Because...
     */
    public Table(){
        for (char i = 'a'; i <= height; i++){
            for (int j = 1; j < length; j++){
                String id = Character.toString(i).concat(Integer.toString(j));
                cells.put(id, new Cell(this, id));
            }
        }
    }

    /**
     * Adds the function to the relevant cell
     * @param func function to be added may conatin: whitespaces, capital letters, other cells...
     */
    public void addFunction(String func){
        List<Cell> cells = Functions.getReferencedCells(func, this);
        try{
        cells.get(0).updateFunc(func);
        }catch(Exception e){}
    }

    /**
     * always should be used with less than in a loop
     * @return returns the length of the table
     */
    public int getLength(){return length;}

    /**
     * Always should be used with equality in a loop
     * @return The hight as a character
     */
    public char getHeight(){return height;}

    /**
     * @param key cell id
     * @return reference to the cell
     */
    public Cell getCell(String key){return cells.get(key);}
}
