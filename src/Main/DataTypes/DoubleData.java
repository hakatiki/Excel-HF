package Main.DataTypes;


// For documentation go to Data
public class DoubleData extends Data<Double> {
    private Double data;
    @Override
    public DataType getType(){return DataType.Double;}
    @Override
    public Double getData(){return data;}
    @Override
    public void setData(Double newData){data = newData;}
    @Override
    public  String toString(){return Double.toString(data);}
    public DoubleData(Double newData){data = newData;}


}