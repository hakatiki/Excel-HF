package Main.Tester;

import Main.Cell;
import Main.DataTypes.BooleanData;
import Main.DataTypes.DataType;
import Main.DataTypes.DoubleData;
import Main.DataTypes.StringData;
import Main.Table;
import org.junit.Assert;
import org.junit.Test;

public class TestClass {
    @Test
    public void DoubleDataTester(){
        DoubleData d = new DoubleData(new Double(420.0));
        Assert.assertEquals("getType", d.getType(), DataType.Double);
        Assert.assertEquals("getData", d.getData(), 420.0,0.00001);
        Assert.assertEquals("toString", d.toString(), "420.0");
    }

    @Test
    public void BooleanDataTester(){
        BooleanData d = new BooleanData(new Boolean(true));
        Assert.assertEquals("getType", d.getType(), DataType.Boolean);
        Assert.assertEquals("getData", d.getData(), true);
    }

    @Test
    public void StringDataTester(){
        StringData d = new StringData("hello");
        Assert.assertEquals("getType", d.getType(), DataType.String);
        Assert.assertEquals("getData", d.getData(), "hello");
    }
    @Test
    public void CellTester(){
        Table table = new Table();
        Cell c = new Cell(table, "a1");

        c.updateFunc("a1 = 420*10");
        double correct = 420*10;
        Assert.assertEquals("Cell test", correct, c.getData().getData());

        c.updateData(new StringData("hello"));
        Assert.assertEquals("Cell test", "hello", c.getData().getData());
    }
}
