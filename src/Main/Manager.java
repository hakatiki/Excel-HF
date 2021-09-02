package Main;

import Main.GUI.Display;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Manager {
    static Table table = new Table();
    static Display display = new Display(table);

    /**
     * Saves the table that it owns
     * @param path where we want to save the file
     */
    public static void save(String path){
        try{
            FileOutputStream fOutStream = new FileOutputStream(path);
            ObjectOutputStream oOutStream = new ObjectOutputStream(fOutStream);
            oOutStream.writeObject(table);
            oOutStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Loads the file from specified path
     * @param path where the file is located
     * @return a new table bacause the display needs to know about the new table
     */
    public static Table load(String path){
        try{
            FileInputStream fInptStream = new FileInputStream(path);
            ObjectInputStream oInptStream = new ObjectInputStream(fInptStream);
            table = (Table)oInptStream.readObject();
            oInptStream.close();
            return table;
        }catch(Exception e){
            e.printStackTrace();
        }
        return table;
    }
}
