package Main.DataTypes;


//Not used!
public class IntegerData extends Data<Integer> {
    private Integer data;
    @Override
    public DataType getType(){return DataType.Integer;}
    @Override
    public Integer getData(){return data;}
    @Override
    public void setData(Integer newData){data = newData;}
    @Override
    public  String toString(){return Integer.toString(data);}
    public IntegerData(Integer newData){data = newData;}
}
