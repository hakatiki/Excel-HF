package Main;

import Main.DataTypes.Data;
import Main.Functions.Functions;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Cell implements Serializable , Comparable<Cell> {
    private Table owner;
    private Data data = null;
    private String func = null;
    private String name = null;
    protected Boolean visited = false;
    private Set<Cell> references = new TreeSet<Cell>(); // Needs to be a set, could be hashset or should be i don't know...


    /**
     * @param table_ The owner of the cell
     * @param name_ The name of the cell like "b2"
     */
    public Cell(Table table_, String name_){
        owner = table_;
        name = name_;
    }

    /**
     * @param newData A new data
     */
    public void updateData(Data newData){
        data = newData;
        notifyOfChange();
    }

    /**
     * @param newFunc The new value of the func attribute, updates data too
     */
    public void updateFunc(String newFunc){
        List<Cell> refered = Functions.getReferencedCells(newFunc, owner);
        refered.remove(0);
        if (refered.indexOf(this) < 0) {
            func = newFunc;
            Set<Cell> set= new TreeSet(refered);
            for (Cell c:set){
                c.addToReferenced(this);
            }

            try {
                data = Functions.parse(func, owner);
            } catch (Exception e) {
                e.printStackTrace();
            }

            notifyOfChange();
        }
    }




    /**
     * @return the data attribute
     */
    public Data getData(){return data;}

    /**
     * @return name attribute
     */
    public String getName(){return name;}

    /**
     * @return the function attribute
     */
    public String getFunc(){return func;}

    @Override
    public int compareTo(Cell T){
        return name.equals(T.getName())? 0: 1;
    }

    /**
     * @param c a cell which is referenced by "this" so when "this" is updated that should be too
     */
    private void addToReferenced(Cell c){
        references.add(c);
    }

    /**
     * Notifies referenced cells of the changes made to the cells from where it's value is calculated
     */
    private void notifyOfChange(){
        try{
            for(Cell t : references){
                t.recalculate();
                t.notifyOfChange();
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.printf("Circular dependeny\n");
        }
        for(Cell t : references)
            if (t.visited.equals(true))
                t.removeVisitation();
    }

    /**
     * Recalulates the value of the cell
     * @throws Exception when something is not coolio
     */
    private void recalculate() throws Exception{
        if (visited)
            throw new Exception("Already updated sad!");
        visited = true;
        if (func != null)
            data = Functions.parse(func, owner);
    }

    /**
     * Needed for the dfs
     */
    private void removeVisitation(){
        visited = false;
        for(Cell t : references)
            if (t.visited.equals(true))
                t.removeVisitation();
    }

}
