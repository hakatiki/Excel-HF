package Main.Functions;

import Main.Cell;
import Main.DataTypes.*;
import Main.Table;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

enum State {nothing, char_found, num_one, num_two, separator, sep_char_found, sep_num_one, sep_num_two}

public class Functions {
    /**
     * @param func The function to be parsed
     * @param table The table from where the the parser can get the relevant data
     * @return  A Data with the value of the passed function
     * @throws Exception When somethin is not coolio
     */
    public static Data parse(String func, Table table) throws Exception {
        func = func.replaceAll("\\s", "");
        func = func.toLowerCase();
        String[] tokens = func.split("=");
        if (tokens.length != 2) {
            throw new Exception("Invalid function!");
        }
        Data ret = selectType(tokens[1], table);
        return ret;
    }

    /**
     * @param func The function from where we want to get the relevant cells
     * @param table Where they are referencing
     * @return  The cells referrenced, first being the lvalue of the function
     */
    public static List<Cell> getReferencedCells(String func, Table table) {
        func = func.replaceAll("\\s", "");
        func = func.toLowerCase();
        List<Cell> refered = new LinkedList<Cell>();
        State state = State.nothing;
        int beginIndex = -1;
        int endIndex = -1;
        int endTermIndex = -1;
        for (int i = 0; i < func.length(); i++) {
            if (state.equals(State.nothing) && isLetter(func.charAt(i)) && i != func.length()-1) {
                state = State.char_found;
                beginIndex = i;
                endTermIndex = i;
                if (isNumber(func.charAt(endTermIndex + 1))) {
                    state = State.num_one;
                    endTermIndex = i + 1;
                    if (endTermIndex + 1 < func.length() && state.equals(State.num_one) && isNumber(func.charAt(endTermIndex + 1))) {
                        state = State.num_two;
                        endTermIndex = endTermIndex + 1;
                    }
                    if (endTermIndex + 1 < func.length() && func.charAt(endTermIndex + 1) == ':') {
                        state = State.separator;
                        endTermIndex = endTermIndex + 1;
                    } else {
                        refered.add(table.getCell(func.substring(beginIndex, endTermIndex + 1)));
                        state = State.nothing;
                    }
                }
                i = endTermIndex;
            } else if (state.equals(State.separator) && isLetter(func.charAt(i))) {
                state = State.sep_char_found;
                if (isNumber(func.charAt(i + 1))) {
                    endIndex = i + 1;
                    state = State.sep_num_one;
                    if (endIndex + 1 < func.length() && state.equals(State.sep_num_one) && isNumber(func.charAt(endIndex + 1))) {
                        state = State.sep_num_two;
                        endIndex = endIndex + 1;
                    }
                    addBlockToList(beginIndex, endTermIndex, endIndex, func, refered, table);
                }
                i = endIndex;
                state = State.nothing;
            } else
                state = State.nothing;
        }
        return refered;
    }

    /**
     * @param c A character
     * @return  A boolean-true if the char was a number from 0-9, else false
     */
    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * @param c A character
     * @return  A boolean-true if the char was a letter from a-z, else false
     */
    private static boolean isLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    /**
     * @param b Beginging of the block to be added to the list
     * @param m The position of the separator ':'
     * @param e The last pos, not inclusive
     * @param s The string from where we calculate the stuff
     * @param l The list where we add the stuff
     * @param t The table where the referenced cells are located
     */
    private static void addBlockToList(int b, int m, int e, String s, List<Cell> l, Table t) {
        String str = new String("");
        if (s.charAt(b) == s.charAt(m + 1)) {
            int firstNum = s.charAt(b + 1) - '0';
            if (m != b + 2)
                firstNum = firstNum * 10 + s.charAt(b + 2) - '0';
            int secondNum = s.charAt(m + 2) - '0';
            if (e == m + 3)
                secondNum = secondNum * 10 + s.charAt(m + 3) - '0';
            while (firstNum <= secondNum) {
                String cellString = ((Character) s.charAt(b)).toString() + ((Integer) firstNum).toString();
                l.add(t.getCell(cellString));
                firstNum++;
            }
        } else {
            int firstNum = s.charAt(b + 1) - '0';
            if (m != b + 2)
                firstNum = firstNum * 10 + s.charAt(b + 2) - '0';
            int secondNum = s.charAt(m + 2) - '0';
            if (e == m + 3)
                secondNum = secondNum * 10 + s.charAt(m + 3) - '0';
            if (firstNum != secondNum) {
                return;
            }
            char indexChar = s.charAt(b);
            char endChar = s.charAt(m + 1);
            while (indexChar <= endChar) {
                String cellString = ((Character) indexChar).toString() + ((Integer) firstNum).toString();
                l.add(t.getCell(cellString));
                indexChar++;
            }
        }
    }

    /**
     *
     * @param expr Function but with mixed types, thus the further processing
     * @param table See above
     * @return  A Data to .parse(...)
     * @throws Exception when something is not coolio
     */
    private static Data selectType(String expr, Table table) throws Exception {
        if (expr.indexOf("true") >= 0 || expr.indexOf("false") >= 0)
            return new BooleanData(Boolean.parseBoolean(expr));
        else if (expr.indexOf("\"") >= 0)
            return new StringData(expr.replaceAll("[\"]", ""));
        else {
            String formated = replaceAllFuncdtions(expr, table);
            return new DoubleData(eval(formated));
        }
    }

    /**
     * @param func Fromats the function in a way that is accepted by eval, replaces every reference with the value
     * @param table See above
     * @return A formated string
     */
    private static String replaceAllFuncdtions(String func, Table table) {
        func = replaceSum(func, table);
        func = replaceAvg(func, table);
        func = replaceMin(func, table);
        func = replaceMax(func, table);
        func = replaceReferences(func, table);
        return func;
    }

    /**
     * @param func ...
     * @param table ...
     * @return A string without sum(...) functions only values of the sum
     */
    private static String replaceSum(String func, Table table) {
        int sumIndex = func.indexOf("sum", 0);
        while (sumIndex >= 0) {
            int endIndex = sumIndex + 1;
            while (endIndex < func.length() && func.charAt(endIndex) != ')')
                endIndex++;
            List<Cell> sumCells = getReferencedCells(func.substring(sumIndex + 4, endIndex + 1), table);
            double sum = 0;
            for (Cell cell : sumCells) {
                Data d = cell.getData();
                if (d.getType().equals(DataType.Integer) || d.getType().equals(DataType.Double))
                    sum += (Double) d.getData();
            }
            func = func.substring(0, sumIndex) + ((Double) sum).toString() + func.substring(endIndex + 1);
            sumIndex = func.indexOf("sum", 0);
        }
        return func;
    }

    /**
     * @param func ...
     * @param table ...
     * @return A string without avg(...) functions only values of the avg
     */
    private static String replaceAvg(String func, Table table) {
        int sumIndex = func.indexOf("avg", 0);
        while (sumIndex >= 0) {
            int endIndex = sumIndex + 1;
            while (endIndex < func.length() && func.charAt(endIndex) != ')')
                endIndex++;
            List<Cell> sumCells = getReferencedCells(func.substring(sumIndex + 4, endIndex + 1), table);
            double sum = 0;
            double div = 0;
            for (Cell cell : sumCells) {
                Data d = cell.getData();
                if (d.getType().equals(DataType.Integer) || d.getType().equals(DataType.Double)) {
                    sum += (double) d.getData();
                    div = div + 1;
                }
            }
            func = func.substring(0, sumIndex) + ((Double) (sum / div)).toString() + func.substring(endIndex + 1);
            sumIndex = func.indexOf("avg", 0);
        }
        return func;
    }

    /**
     * @param func ...
     * @param table ...
     * @return A string without min(...) functions only values of the min
     */
    private static String replaceMin(String func, Table table) {
        int sumIndex = func.indexOf("min", 0);
        while (sumIndex >= 0) {
            int endIndex = sumIndex + 1;
            while (endIndex < func.length() && func.charAt(endIndex) != ')')
                endIndex++;
            List<Cell> sumCells = getReferencedCells(func.substring(sumIndex + 4, endIndex + 1), table);
            double min = Double.POSITIVE_INFINITY;
            for (Cell cell : sumCells) {
                Data d = cell.getData();
                if (d.getType().equals(DataType.Integer) || d.getType().equals(DataType.Double)) {
                    min = (double) d.getData() > min ? min : (double) d.getData();
                }
            }
            func = func.substring(0, sumIndex) + ((Double) min).toString() + func.substring(endIndex + 1);
            sumIndex = func.indexOf("min", 0);
        }
        return func;
    }

    /**
     * @param func ...
     * @param table ...
     * @return A string without max(...) functions only values of the max
     */
    private static String replaceMax(String func, Table table) {
        int sumIndex = func.indexOf("max", 0);
        while (sumIndex >= 0) {
            int endIndex = sumIndex + 1;
            while (endIndex < func.length() && func.charAt(endIndex) != ')')
                endIndex++;
            List<Cell> sumCells = getReferencedCells(func.substring(sumIndex + 4, endIndex + 1), table);
            double max = Double.NEGATIVE_INFINITY;
            for (Cell cell : sumCells) {
                Data d = cell.getData();
                if (d.getType().equals(DataType.Integer) || d.getType().equals(DataType.Double)) {
                    max = (double) d.getData() < max ? max : (double) d.getData();
                }
            }
            func = func.substring(0, sumIndex) + ((Double) max).toString() + func.substring(endIndex + 1);
            sumIndex = func.indexOf("max", 0);
        }
        return func;
    }

    /**
     * @param func ...
     * @param table ...
     * @return Replaces single references like "a1*b20" with "10*30"
     */
    private static String replaceReferences(String func, Table table){
        List<Cell> refered = getReferencedCells(func, table);
        for (Cell c : refered){
            String id = c.getName();
            String dataAsString = c.getData().toString();
            int pos = func.indexOf(id);
            int end = pos + id.length();
            if(end >= func.length())
                func = func.substring(0,pos).concat(dataAsString);
            else
                func = func.substring(0,pos).concat(dataAsString).concat(func.substring(end));
        }
        return func;
    }

    /**
     * @param str the function containing only numbers and/or sin, cos...
     * @return A double, the caclulated value
     */
    private static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }


    @Test
    public void referencedCellTester(){
        Table table = new Table();
        String func = "a1 a2 a3";
        List<Cell> list = getReferencedCells(func, table);
        Assert.assertEquals("a1 a2 a3, 3 referenced cells", list.size(), 3);

        func = "a1:a10";
        list = getReferencedCells(func, table);
        Assert.assertEquals("a1:a10, 10 referenced cells", list.size(), 10);

        func = "b1:d1";
        list = getReferencedCells(func, table);
        Assert.assertEquals("b1:d1, 3 referenced cells, vertical vector", list.size(), 3);

        func = "b1:d1 a1:a10 a1 a2 a3";
        list = getReferencedCells(func, table);
        Assert.assertEquals("b1:d1 a1:a10 a1 a2 a3, 16 referenced cells, vertical vector", list.size(), 16);
    }

    @Test
    public void evalTester(){
        String func = "420*69";
        double calculated = eval(func);
        double actual = 420*69;
        Assert.assertEquals("420*69", calculated, actual, 0.000001);

        func = "1+ 1 + 1 + 1";
        calculated = eval(func);
        actual = 4;
        Assert.assertEquals("420*69", calculated, actual, 0.000001);

        func = "1*2-3";
        calculated = eval(func);
        actual = -1;
        Assert.assertEquals("420*69", calculated, actual, 0.000001);

    }

    @Test
    public void parseTester(){
        Table table = new Table();
        table.addFunction("a2 = 69");
        String func = "a1 = 420*a2";
        DoubleData calculated = new DoubleData(0.00);
        try{
            calculated = (DoubleData)parse("a1 = 420*a2", table);
        }catch(Exception e){}

        double actual = 420*69;
        Assert.assertEquals("420*69", calculated.getData(), actual, 0.000001);

        table.addFunction("a3 = 69");
        func = "a1 = sum(a2:a3)-420";
        calculated = new DoubleData(0.00);
        try{
            calculated = (DoubleData)parse("a1 = sum(a2:a3)-420", table);
        }catch(Exception e){}

        actual = 69.0+69.0-420.0;
        Assert.assertEquals("sum(a2:a3)-420", calculated.getData(), actual, 0.000001);

        func = "a1 = sum(a2:a3)+min(a2:a3)+max(a2:a3)+avg(a2:a3)";
        calculated = new DoubleData(0.00);
        try{
            calculated = (DoubleData)parse( "a1 = sum(a2:a3)+min(a2:a3)+max(a2:a3)+avg(a2:a3)", table);
        }catch(Exception e){}

        actual = 69.0+69.0+69.0+69.0+69.0;
        Assert.assertEquals("a1 = sum(a2:a3)+min(a2:a3)+max(a2:a3)+avg(a2:a3)", calculated.getData(), actual, 0.000001);

    }
}
