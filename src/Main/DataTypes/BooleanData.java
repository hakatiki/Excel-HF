package Main.DataTypes;


// For documentation go to Data
public class BooleanData extends Data<Boolean> {
    private Boolean data;
    @Override
    public DataType getType(){return DataType.Boolean;}
    @Override
    public Boolean getData(){return data;}
    @Override
    public void setData(Boolean newData){data = newData;}
    @Override
    public  String toString(){return Boolean.toString(data);}
    public BooleanData(Boolean newData){data = newData;}
}