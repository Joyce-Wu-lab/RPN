package RPN;
import java.util.*;

public class ReversePolish {
    public static ArrayList<String> infixToPostfix(ArrayList<String> infix) {
        if(infix == null) return null;
        //用于存放运算符的栈
        Stack<String> temp = new Stack<>();
        //用于存储逆波兰式
        ArrayList<String> postfix = new ArrayList<>();
        //遍历中缀表达式，将中缀表达式转为逆波兰式
        for (String str : infix) {
            //如果是数字，直接存进结果中
            if (str.matches("\\d+"))
                postfix.add(str);
            //如果不是数字，即为运算符
            else {
                //如果栈是空的或识别到的运算符是“{”或“[”或“(”或者优先级高于栈中最后所存的运算符，将该运算符存入栈
                if (temp.isEmpty() || str.equals("{") || str.equals("[") ||
                    str.equals("(") || priority(str) > priority(temp.peek()))
                    temp.push(str);
                //如果识别到的运算符是“)”，把栈中“(”前所有的运算符弹出存入结果中，同时将“(”也弹出
                else if (str.equals(")")) {
                    String s;
                    while (!"(".equals(s = temp.pop()))
                        postfix.add(s);
                }
                //如果识别到的运算符是“]”，把栈中“[”前所有运算符弹出存入结果中，同时将“[”也弹出
                else if (str.equals("]")) {
                    String s;
                    while (!"[".equals(s = temp.pop()))
                        postfix.add(s);
                }
                //如果识别到的运算符是“}”，把栈中“{”前所有运算符弹出存入结果中，同时将“}”也弹出
                else if(str.equals("}")){
                    String s;
                    while(!"{".equals(s = temp.pop()))
                        postfix.add(s);
                }
                //如果识别到的是其他运算符，即优先级不高于与栈中最后进入的运算符
                else {
                    //将栈中优先级高于识别到的运算符弹出，存入结果中
                    while (!temp.isEmpty() && priority(str) <= priority(temp.peek()))
                        postfix.add(temp.pop());
                    //将识别到的运算符存入栈中
                    temp.push(str);
                }
            }
        }
        //遍历完后，将栈中剩余的运算符弹出存入结果中
        while (temp.size() != 0)
            postfix.add(temp.pop());
        return postfix;
    }
    //优先级定义
    private static int priority(String op) {
        int r = switch (op) {
            case "||" -> 1;
            case "&&" -> 2;
            case "|" -> 3;
            case "^" -> 4;
            case "&" -> 5;
            case ">", "<", ">=", "<=" -> 6;
            case "<<", ">>", ">>>" -> 7;
            case "+", "-" -> 8;
            case "*", "/", "%" -> 9;
            case "++", "--", "~" -> 10;
            default -> -1;
        };
        return r;
    }
    //将输入的表达式转为ArrayList类型
    public static ArrayList<String> toInfixList(String s) {
        ArrayList<String> result = new ArrayList<>();
        //去除字符串中所有空格
        s = s.replaceAll("\\s+", "");
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            //如果不是数字
            if (c < '0' || c > '9') {
                //判断是否是自增
                if (c == '+') {
                    char t = s.charAt(i + 1);
                    if (t == '+') {
                        result.add("" + c + t);
                        i++;
                        continue;
                    }
                }
                //判断是否是自减
                if(c == '-'){
                    char t = s.charAt(i + 1);
                    if(t == '-'){
                        result.add("" + c + t);
                        i++;
                        continue;
                    }
                }
                //判断是否是逻辑与
                if(c == '&'){
                    char t = s.charAt(i + 1);
                    if(t == '&'){
                        result.add("" + c + t);
                        i++;
                        continue;
                    }
                }
                //判断是否是逻辑或
                if(c == '|'){
                    char t = s.charAt(i + 1);
                    if(t == '|'){
                        result.add("" + c + t);
                        i++;
                        continue;
                    }
                }
                if(c == '<'){
                    char t = s.charAt(i + 1);
                    //判断是否是左移
                    if(t == '<'){
                        result.add("" + c + t);
                        i++;
                        continue;
                    }
                    //判断是否是<=
                    if(t == '='){
                        result.add("" + c + t);
                        i++;
                        continue;
                    }
                }
                if(c == '>'){
                    char t = s.charAt(i + 1);
                    //判断是否是右移
                    if(t == '>'){
                        char q = s.charAt(i + 2);
                        //判断是否是无符号右移
                        if(q == '>'){
                            result.add("" + c + t + q);
                            i += 2;
                        }
                        else {
                            result.add("" + c + t);
                            i++;
                        }
                        continue;
                    }
                    //判断是否是>=
                    if (t == '='){
                        result.add("" + c + t);
                        i++;
                        continue;
                    }
                }
                //除数为0时报错
                if(c == '/'){
                    char t = s.charAt(i + 1);
                    if(t == '0'){
                        System.out.println("Invalid!");
                        return null;
                    }
                }
                //小数报错
                if(c == '.'){
                    System.out.println("Invalid!");
                    return null;
                }
                result.add(String.valueOf(c));
            }
            //如果字符是数字，需要注意保存多位数
            else {
                StringBuilder str = new StringBuilder();
                //用str来保存多位数
                while (i < s.length() && (c = s.charAt(i)) >= '0' && (c = s.charAt(i)) <= '9') {
                    str.append(c);
                    i++;
                }i--;
                result.add(str.toString());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        //String input = "(3 * 2 & 5 | 2) ^ 4";
        String input = "3 + 1 < 5 || 6 * 3 >= 7 / 2 && 1 > 2 || 5 <= 9 - 6";
        ArrayList<String> string = toInfixList(input);
        System.out.println(string);
        System.out.println(infixToPostfix(string));
    }
}
