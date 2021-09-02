package Main.Tester;

import Main.Cell;
import Main.DataTypes.DoubleData;
import Main.Functions.Functions;
import Main.Table;

public class FuncTester {
    public static String refenencedTest(){
        Table t = new Table();
        String func = "A2 = Boolean:A2:A3";
        String func1 = "A2 = Boolean: avg(b69)     ";
        String func2 = "A2 =    Integer:sum(C9:C11)";
        String func3 = "A2 = function:A10:c10";
        System.out.println(Functions.getReferencedCells(func, t));
        System.out.println(Functions.getReferencedCells(func1, t));
        System.out.println(Functions.getReferencedCells(func2, t));
        for (Cell c : Functions.getReferencedCells(func3, t))
            System.out.println(c.getName());
        return "Refeerenced cell test";
    }
    public static String baseBuiltInTest(){
        Table t = new Table();
        t.getCell("a1").updateData(new DoubleData(2.0));
        t.getCell("a2").updateData(new DoubleData(3.0));
        t.getCell("a3").updateData(new DoubleData(4.0));
        String func = "A1 = Function:sum(A2:A3)";
        t.addFunction(func);
        System.out.println((t.getCell("a1").getData()));
        t.getCell("b10").updateData(new DoubleData( .0));
        t.getCell("c10").updateData(new DoubleData(69.0));
        String func1 = "A1=Function:sum(a2:a3)+min(A2:A3)-Min(b10:c10)" ;
        t.addFunction(func1);
        System.out.println((t.getCell("a1").getData()));
        return "Base Built in Function Test Completed!";
    }
}
