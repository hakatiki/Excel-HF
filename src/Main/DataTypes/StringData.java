package Main.DataTypes;

// For documentation go to Data
public class StringData extends Data<String> {
    private String data;
    @Override
    public DataType getType(){return DataType.String;}
    @Override
    public String getData(){return data;}
    @Override
    public void setData(String newData){data = newData;}
    @Override
    public  String toString(){return data;}
    public StringData(String newData){data = newData;}
}