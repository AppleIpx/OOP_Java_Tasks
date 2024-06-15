import java.util.Stack;

public class CalculatorModel {
    public double calculate(String equation) throws Exception {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        boolean lastCharWasOp = true;

        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == ' ') {
                continue;
            }

            if ((equation.charAt(i) >= '0' && equation.charAt(i) <= '9') ||
                (equation.charAt(i) == '-' && lastCharWasOp)) {
                StringBuilder sbuf = new StringBuilder();

                if (equation.charAt(i) == '-' && lastCharWasOp) {
                    sbuf.append('-');
                    i++;
                }

                while (i < equation.length() && ((equation.charAt(i) >= '0' && equation.charAt(i) <= '9') || equation.charAt(i) == '.')) {
                    sbuf.append(equation.charAt(i++));
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
                lastCharWasOp = false;
            } else if (equation.charAt(i) == '(') {
                ops.push(equation.charAt(i));
                lastCharWasOp = true;
            } else if (equation.charAt(i) == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
                lastCharWasOp = false;
            } else if (isOperator(equation.charAt(i))) {
                while (!ops.empty() && hasPrecedence(equation.charAt(i), ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(equation.charAt(i));
                lastCharWasOp = true;
            }
        }

        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/' || op1 == '#') && (op2 == '+' || op2 == '-')) {
            return false;
        } else if (op1 == '^' && (op2 == '*' || op2 == '/' || op2 == '#')) {
            return false;
        } else {
            return true;
        }
    }

    private double applyOp(char op, double b, double a) throws Exception {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new Exception("Cannot divide by zero");
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
            case '#':
                if (b == 0) {
                    throw new Exception("Cannot divide by zero");
                }
                return Math.floor(a / b);
        }
        return 0;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '#';
    }
}
