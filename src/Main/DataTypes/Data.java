package Main.DataTypes;

import java.io.Serializable;



abstract public class Data<T> implements Serializable {
    /**
     * Every data type which inherits from this redefines this function
     * @return a type defined in DataType as an enum
     */
    public DataType getType(){return DataType.Data;}

    /**
     * @return the data stored
     */
    abstract public T getData();

    /**
     * @param t sets a new data
     */
    abstract public void setData(T t);
    public String toString(){return "null";}
    public Data(){}
}
