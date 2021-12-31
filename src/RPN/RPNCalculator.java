package RPN;
import java.util.*;

public class RPNCalculator {
    //计算结果为整数
    private static int calculateD(ArrayList<String> input){
        if(input == null) return 0;
        //用于存放数据
        Stack<String> data = new Stack<>();
        for(String str : input){
            //如果是数字，直接入栈
            if(str.matches("\\d+"))
                data.push(str);
            //单目运算
            else{
                if("++".equals(str) || "--".equals(str) || "~".equals(str)){
                    int res = 0;
                    int n1 = Integer.parseInt(data.pop());
                    if("++".equals(str))
                        res = n1 + 1;
                    if("--".equals(str))
                        res = n1 - 1;
                    if("~".equals(str))
                        res = ~n1 + 1;
                    //在结果后加一个空字符串将结果转为字符串保存入栈中
                    data.push(String.valueOf(res));
                }
                //双目运算
                else {
                    int n1 = Integer.parseInt(data.pop());
                    int n2 = Integer.parseInt(data.pop());
                    int res = switch (str) {
                        case "+" -> n2 + n1;
                        case "-" -> n2 - n1;
                        case "*" -> n2 * n1;
                        case "/" -> n2 / n1;
                        case "%" -> n2 % n1;
                        case ">>>" -> n2 >>> n1;
                        case "<<" -> n2 << n1;
                        case ">>" -> n2 >> n1;
                        case "&" -> n2 & n1;
                        case "|" -> n2 | n1;
                        case "^" -> n2 ^ n1;
                        default -> 0;
                    };
                    data.push(String.valueOf(res));
                }
            }
        }
        //将栈中剩余的数字即答案弹出
        return Integer.parseInt(data.pop());
    }
    //计算结果可为小数
    private static float calculateF(ArrayList<String> input){
        if(input == null) return 0;
        Stack<String> data = new Stack<>();
        for(String str : input){
            if(str.matches("\\d+"))
                data.push(str);
            else if("~".equals(str)){
                int n1 = Integer.parseInt(data.pop());
                //~是求补码，与负数的二进制差1，故加1以实现负数
                int res = ~n1 + 1;
                data.push(String.valueOf(res));
            }
            else {
                float n1 = Float.parseFloat(data.pop());
                float n2 = Float.parseFloat(data.pop());
                float res = switch (str) {
                    case "+" -> n2 + n1;
                    case "-" -> n2 - n1;
                    case "*" -> n2 * n1;
                    case "/" -> n2 / n1;
                    default -> 0;
                };
                data.push(String.valueOf(res));
            }
        }
        return Float.parseFloat(data.pop());
    }
    //计算结果为布尔值
    private static boolean calculateB(ArrayList<String> input){
        if(input == null) return false;
        Stack<String> data = new Stack<>();
        for(String str : input){
            if(str.matches("\\d+"))
                data.push(str);
            else if("&&".equals(str)){
                boolean b1 = Boolean.parseBoolean(data.pop());
                boolean b2 = Boolean.parseBoolean(data.pop());
                //逻辑运算短路
                if(!b2)
                    return false;
                data.push(String.valueOf(b1));
            }
            else if("||".equals(str)){
                boolean b1 = Boolean.parseBoolean(data.pop());
                boolean b2 = Boolean.parseBoolean(data.pop());
                //逻辑运算短路
                if(b2)
                    return true;
                data.push(String.valueOf(b1));
            }
            else if(">".equals(str)){
                float b1 = Float.parseFloat(data.pop());
                float b2 = Float.parseFloat(data.pop());
                if(b2 > b1)
                    data.push(String.valueOf(true));
                else
                    data.push(String.valueOf(false));
            }
            else if (">=".equals(str)){
                float b1 = Float.parseFloat(data.pop());
                float b2 = Float.parseFloat(data.pop());
                if(b2 >= b1)
                    data.push(String.valueOf(true));
                else
                    data.push(String.valueOf(false));
            }
            else if ("<".equals(str)){
                float b1 = Float.parseFloat(data.pop());
                float b2 = Float.parseFloat(data.pop());
                if(b2 < b1)
                    data.push(String.valueOf(true));
                else
                    data.push(String.valueOf(false));
            }
            else if ("<=".equals(str)){
                float b1 = Float.parseFloat(data.pop());
                float b2 = Float.parseFloat(data.pop());
                if(b2 <= b1)
                    data.push(String.valueOf(true));
                else
                    data.push(String.valueOf(false));
            }
            else {
                float n1 = Float.parseFloat(data.pop());
                float n2 = Float.parseFloat(data.pop());
                float res = switch (str) {
                    case "+" -> n2 + n1;
                    case "-" -> n2 - n1;
                    case "*" -> n2 * n1;
                    case "/" -> n2 / n1;
                    default -> 0;
                };
                data.push(String.valueOf(res));
            }
        }
        return Boolean.parseBoolean(data.pop());
    }

    //判断运算结果是整数，小数还是布尔值
    private static int check(ArrayList<String> input){
        if(input == null) return 0;
        for (String str : input){
            if(!str.matches("\\d+")) {
                //如果有位运算和自增自减运算，结果为整数
                if ("++".equals(str) || "--".equals(str) || "%".equals(str) || "<<".equals(str) ||
                    ">>".equals(str) || ">>>".equals(str) || "&".equals(str) || "|".equals(str) || "^".equals(str))
                    return 1;
                //如果有关系运算符或逻辑运算符，结果为布尔值
                if("&&".equals(str) || "||".equals(str) || ">".equals(str) || "<".equals(str) ||
                   ">=".equals(str) || "<=".equals(str))
                    return 3;
            }
        }
        return 2;
    }

    public static void main(String[] args){
        //String input = "[6 * (5 >> 2)]++ - 7 ^ 3";
        String input = "3 * 2 < 5 || 6 < 7 && 1 > 2";
        ArrayList<String> s1 = ReversePolish.toInfixList(input);
        ArrayList<String> s2 = ReversePolish.infixToPostfix(s1);
        System.out.println(s2);
        if(check(s2) == 1) {
            int output = calculateD(s2);
            System.out.println(output);
        }
        else if(check(s2) == 2){
            float output = calculateF(s2);
            System.out.println(output);
        }
        else {
            boolean output = calculateB(s2);
            System.out.println(output);
        }
    }
}
