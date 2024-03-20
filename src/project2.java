import java.util.*;

public class project2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the infix expression or 'exit': ");
            String infix = scanner.nextLine();
            if (infix.equalsIgnoreCase("exit")){
                System.out.println("Exiting...");
                break;
            }
            if (isValid(infix)) { //if the infix expression is valid
                Map<Character, Integer> values = new HashMap<>(); //has map to map variable chars to int values
                for (char variable : getVariables(infix)) { //for loop to enter variable values
                    System.out.println("Enter the value for " + variable + ":");
                    try { //make sure the user inputs an integer
                        int value = scanner.nextInt();
                        values.put(variable, value);
                    } catch (InputMismatchException e) { //if user input is not int
                        System.out.println("You can't enter anything but numbers in this section but here is the postfix expression anyways:");
                        break;
                    }
                }
                scanner.nextLine();
                String postFix = infixToPostfix(infix); //send infix expression to driver method
                System.out.println("Postfix Expression: " + postFix);
                int result = calculatePostFix(postFix, values);
                if (result != invalidResult) {
                    System.out.println("Evaluated result: " + result);
                    System.out.println("--------------------");
                }
                else {
                    System.out.println("Invalid expression");
                    System.out.println("--------------------");
                }
            }
            else {
                System.out.println("Invalid expression");
                System.out.println("--------------------");
            }
        }
    }

    private static boolean isValid(String infix) { //checks to make sure the postfix expression is valid based off of project sheet requirements
        int i = -1;
        boolean isChar = false;
        boolean isOperator = true;
        do {
            i++;
            if (infix.charAt(i) == ' ') {
                continue;
            }
            if (Character.isAlphabetic(infix.charAt(i))) {
                if (isChar) {
                    return false;
                }
                isChar = true;
                isOperator = false;
            }
            if (precedence(infix.charAt(i)) > 0 && isOperator) {
                return false;
            } else if (precedence(infix.charAt(i)) > 0) {
                isChar = false;
                isOperator = true;
            }
        }
        while (i < infix.length() - 1);
        if (!isChar) {
            return false;
        }
        return true;
    }

    public static int precedence(char ch) { //helper method for deciding precidence of operends vs operators
        if (ch == '+' || ch == '-' || ch == '/' || ch == '*') {
            return 1;
        }
        else {
            return 0;
        }
    }

    private static Set<Character> getVariables(String infix) { //method to take infix string and isolate the variable chars
        Set<Character> variables = new HashSet<>();
        for (char cha : infix.toCharArray()) {
            if(Character.isLetter(cha)) {
                variables.add(cha);
            }
        }
        return variables;
    }

    private static String infixToPostfix(String infix) { //method to take infix expression and turn it into postfix expression
        StringBuilder postFix = new StringBuilder();
        Stack<Character> stack = new Stack<>(); //postfix stack

        for (char cha : infix.toCharArray()) {  //for loop to loop through infix expression to add to postfix array in the correct order
            if(Character.isLetter(cha)) {
                postFix.append(cha);
            }
            else if (cha == '+' || cha == '-') {
                while (!stack.isEmpty() && (stack.peek() == '+' || stack.peek() == '-' || stack.peek() == '*' || stack.peek() == '/')) {
                    postFix.append(stack.pop());
                }
                stack.push(cha);
            }
            else if (cha == '*' || cha == '/') {
                while (!stack.isEmpty() && (stack.peek() == '*' || stack.peek() =='/')) {
                    postFix.append(stack.pop());
                }
                stack.push(cha);
            }
            else if (cha == ' ') {
                continue;
            }
            else {
                continue; //if the character is not valid based off of project sheet, treats it as whitespace
            }
        }
        while (!stack.isEmpty()) {
            postFix.append(stack.pop()); //while stack is not empty, all chars that were not already handled get added into postfix string
        }
        return postFix.toString();
    }

    private static final int invalidResult = Integer.MIN_VALUE; //  this is so the calculatePostFix() method can still return an int

    private static int calculatePostFix(String postfix, Map<Character, Integer> variables) { //computational driver method
        Stack<Integer> stack = new Stack<>(); //driver data structure
        for(char cha : postfix.toCharArray()) { //loop through postfix string
            if (Character.isLetter(cha)) {
                if (variables.containsKey(cha)) {
                    stack.push(variables.get(cha));
                }
                else {
                    return invalidResult;
                }
            }
            else if (cha == '+' || cha == '-' || cha == '*' || cha == '/') {
                if (stack.size() < 2) {
                    return invalidResult; //if there is no number to add/subtract/multiply/divide
                }
                int operand1 = stack.pop(); //first two numbers available
                int operand2 = stack.pop();
                int result; //return value

                switch (cha) {
                    case '+':
                        result = operand1 + operand2;
                        stack.push(result);
                        break;
                    case '-':
                        result = operand2 - operand1;
                        stack.push(result);
                        break;
                    case '*':
                        result = operand1 * operand2;
                        stack.push(result);
                        break;
                    case '/':
                        if (operand1 == 0) {
                            System.out.println("Can't divide by 0");
                            break;
                        }
                        result = operand2 / operand1;
                        stack.push(result);
                        break;
                    default:
                        return invalidResult;
                }
            }
        }
        if (stack.size() != 1){
            return invalidResult;
        }
        return stack.pop(); //return result
    }
}
