package moneytracker.parser;

import moneytracker.command.Command;
import moneytracker.command.AddExpenseCategoryCommand;
import moneytracker.command.AddIncomeCategoryCommand;
import moneytracker.command.HelpCommand;
import moneytracker.command.ListCategoryCommand;
import moneytracker.command.DeleteCategoryCommand;
import moneytracker.command.EditCategoryCommand;
import moneytracker.command.AddIncomeCommand;
import moneytracker.command.AddExpenseCommand;
import moneytracker.command.ListCommand;
import moneytracker.command.DeleteCommand;
import moneytracker.command.ClearCommand;
import moneytracker.command.ReportCommand;
import moneytracker.command.ExitCommand;
import moneytracker.command.UnknownCommand;
import moneytracker.exception.MoneyTrackerException;
import moneytracker.transaction.TransactionList;
import moneytracker.transaction.Transaction;
import moneytracker.transaction.Income;
import moneytracker.transaction.Expense;
import moneytracker.transaction.Category;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contains methods that deal with parsing user commands to extract meaningful details from them.
 */
public class Parser {

    /**
     * Gets the command word from user's input string.
     * @param fullCommand User's full input string.
     * @return Command word from user's input string.
     */
    public static String getCommand(String fullCommand) {
        return fullCommand.split(" ")[0];
    }

    /**
     * Creates an income <code>Category</code> object.
     *
     * @param fullCommand User's full input string.
     * @return <code>Category</code> object.
     * @throws MoneyTrackerException If income category is missing.
     */
    public static Category createIncomeCategory(String fullCommand) throws MoneyTrackerException {
        assert !fullCommand.isBlank() : "fullCommand should not be blank";
        String name = fullCommand.replaceFirst("(?i)addcati", "").toUpperCase().trim();
        if (name.isEmpty()) {
            throw new MoneyTrackerException("The income category name is missing.");
        }
        return new Category(name, "INCOME");
    }

    /**
     * Creates an expense <code>Category</code> object.
     *
     * @param fullCommand User's full input string.
     * @return <code>Category</code> object.
     * @throws MoneyTrackerException If expense category is missing.
     */
    public static Category createExpenseCategory(String fullCommand) throws MoneyTrackerException {
        assert !fullCommand.isBlank() : "fullCommand should not be blank";
        String name = fullCommand.replaceFirst("(?i)addcate", "").toUpperCase().trim();
        if (name.isEmpty()) {
            throw new MoneyTrackerException("The expense category name is missing.");
        }
        return new Category(name, "EXPENSE");
    }

    /**
     * Creates an <code>Income</code> object.
     *
     * @param fullCommand User's full input string.
     * @return <code>Income</code> object.
     * @throws MoneyTrackerException If amount or income category is missing.
     */
    public static Income createIncome(String fullCommand) throws MoneyTrackerException {
        assert !fullCommand.isBlank() : "fullCommand should not be blank";
        String commandParameterString = fullCommand.replaceFirst("(?i)addi", "").trim();
        if (commandParameterString.isEmpty()) {
            throw new MoneyTrackerException("The parameters of the command are missing.");
        }
        HashMap<String, String> commandParameters = getCommandParameters(commandParameterString);
        if (!(commandParameters.containsKey("amount"))) {
            throw new MoneyTrackerException("The amount parameter is missing.");
        }
        if (!(commandParameters.containsKey("category"))) {
            throw new MoneyTrackerException("The income category parameter is missing.");
        }
        String amount = commandParameters.get("amount");
        String description = commandParameters.get("description");
        String date = commandParameters.get("date");
        String incomeCategory = commandParameters.get("category");
        try {
            if (commandParameters.containsKey("date")) {
                return new Income(Double.parseDouble(amount), description, date, incomeCategory);
            } else {
                return new Income(Double.parseDouble(amount), description, incomeCategory);
            }
        } catch (NumberFormatException e) {
            throw new MoneyTrackerException("The amount must be a decimal value. E.g. 30.50");
        }
    }

    /**
     * Creates an <code>Expense</code> object.
     *
     * @param fullCommand User's full input string.
     * @return <code>Expense</code> object.
     * @throws MoneyTrackerException If amount or expense category is missing.
     */
    public static Expense createExpense(String fullCommand) throws MoneyTrackerException {
        assert !fullCommand.isBlank() : "fullCommand should not be blank";
        String commandParameterString = fullCommand.replaceFirst("(?i)adde", "").trim();
        if (commandParameterString.isEmpty()) {
            throw new MoneyTrackerException("The parameters of the command are missing.");
        }
        HashMap<String, String> commandParameters = getCommandParameters(commandParameterString);
        if (!(commandParameters.containsKey("amount"))) {
            throw new MoneyTrackerException("The amount parameter is missing.");
        }
        if (!(commandParameters.containsKey("category"))) {
            throw new MoneyTrackerException("The expense category parameter is missing.");
        }
        String amount = commandParameters.get("amount");
        String description = commandParameters.get("description");
        String date = commandParameters.get("date");
        String expenseCategory = commandParameters.get("category");
        try {
            if (commandParameters.containsKey("date")) {
                return new Expense(Double.parseDouble(amount), description, date, expenseCategory);
            } else {
                return new Expense(Double.parseDouble(amount), description, expenseCategory);
            }
        } catch (NumberFormatException e) {
            throw new MoneyTrackerException("The amount must be a decimal value. E.g. 30.50");
        }
    }

    /**
     * Gets transaction/category index from user's full input string.
     *
     * @param fullCommand User's full input string.
     * @return Index of transaction/category.
     * @throws MoneyTrackerException If index is missing or invalid.
     */
    public static int getDeleteIndex(String fullCommand) throws MoneyTrackerException {
        String commandParameterString =
                fullCommand.replaceFirst("(?i)deletecat", "")
                        .replaceFirst("(?i)delete", "").trim();
        if (commandParameterString.isEmpty()) {
            throw new MoneyTrackerException("The index is missing.");
        }
        int index;
        try {
            index = Integer.parseInt(commandParameterString);
        } catch (NumberFormatException e) {
            throw new MoneyTrackerException("The index is invalid.");
        }
        return index - 1;
    }

    /**
     * Gets parameters for editing category from user's full input string.
     *
     * @param fullCommand User's full input string.
     * @return Parameters for editing category
     * @throws MoneyTrackerException If index is missing or invalid.
     */
    public static String[] getEditParameters(String fullCommand) throws MoneyTrackerException {
        String commandParameterString = fullCommand.replaceFirst("(?i)editcat", "").trim();
        if (commandParameterString.isEmpty()) {
            throw new MoneyTrackerException("The index is missing.");
        }
        String[] editParameters = commandParameterString.split("/n");
        for (int i = 0; i < editParameters.length; i++) {
            editParameters[i] = editParameters[i].trim();
        }
        return editParameters;
    }

    /**
     * Gets <code>Command</code> object from user's full input string.
     *
     * @param fullCommand User's full input string.
     * @return <code>Command</code> object.
     */
    public static Command parse(String fullCommand) {
        String command = getCommand(fullCommand).toLowerCase();
        switch (command) {
        case "help":
            return new HelpCommand();
        case "addcati":
            return new AddIncomeCategoryCommand(fullCommand);
        case "addcate":
            return new AddExpenseCategoryCommand(fullCommand);
        case "listcat":
            return new ListCategoryCommand(fullCommand);
        case "deletecat":
            return new DeleteCategoryCommand(fullCommand);
        case "editcat":
            return new EditCategoryCommand(fullCommand);
        case "addi":
            return new AddIncomeCommand(fullCommand);
        case "adde":
            return new AddExpenseCommand(fullCommand);
        case "list":
            return new ListCommand(fullCommand);
        case "delete":
            return new DeleteCommand(fullCommand);
        case "clear":
            return new ClearCommand();
        case "report":
            return new ReportCommand(fullCommand);
        case "exit":
            return new ExitCommand();
        default:
            return new UnknownCommand();
        }
    }

    private static HashMap<String, String> getCommandParameters(String commandParameterString) {
        assert !commandParameterString.isBlank() : "commandParameterString should not be blank";
        HashMap<String, String> commandParametersMap = new HashMap<>();
        String[] commandParametersArray = commandParameterString.split("/");
        for (String commandParameter : commandParametersArray) {
            if (commandParameter.startsWith("a")) {
                commandParametersMap.put("amount", commandParameter.substring(1).trim());
            } else if (commandParameter.startsWith("c")) {
                commandParametersMap.put("category", commandParameter.substring(1).toUpperCase().trim());
            } else if (commandParameter.startsWith("d")) {
                commandParametersMap.put("date", commandParameter.substring(1).trim());
            } else if (commandParameter.startsWith("e")) {
                commandParametersMap.put("description", commandParameter.substring(1).trim());
            }
        }
        commandParametersMap.putIfAbsent("description", "");
        return commandParametersMap;
    }

    /**
     * Gets date from user's full input string.
     *
     * @param fullCommand User's full input string.
     * @return date from user's input string.
     * @throws MoneyTrackerException if date format incorrect.
     */
    public static String getDate(String fullCommand) throws MoneyTrackerException {
        assert !fullCommand.isBlank() : "fullCommand should not be blank";
        String date = fullCommand.replaceFirst("report", "").trim();
        if (date.isEmpty()) {
            throw new MoneyTrackerException("The report date is missing, e.g. 2020-09.");
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(date);
            } catch (ParseException e) {
                throw new MoneyTrackerException("Date should be in yyyy-MM format. E.g. 2020-09");
            }
        }
        return date;
    }

    /**
     * Gets days of month from user full command string input.
     *
     * @param date date month of <code>Transaction</code> objects.
     * @return days of month
     */
    public static int getDaysOfMonth(String date) {
        int year = Integer.parseInt(date.split("-")[0]) - 1900;
        int month = Integer.parseInt(date.split("-")[1]) - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int numDays = calendar.getActualMaximum(Calendar.DATE);
        System.out.println("date: " + numDays);
        return numDays;
    }

    /**
     * Gets total month income transactions from user's full input string.
     *
     * @param transactions transactions List of <code>Transaction</code> objects.
     * @param date date date month of <code>Transaction</code> objects.
     * @return total month income of transactions
     */
    public static double getTotalIncome(TransactionList transactions, String date) {
        double totalIncome = 0;

        for (int i = 0; i < transactions.getSize(); i++) {
            Transaction temp = transactions.getTransaction(i);
            double amount = Double.parseDouble(transactions.getTransaction(i).getAmount());
            if (temp instanceof Income & transactions.getTransaction(i).setMonth().equals(date)) {
                totalIncome = totalIncome + amount;
            }
        }
        return totalIncome;
    }

    /**
     * Gets total month expense transactions from user's full input string.
     *
     * @param transactions transactions List of <code>Transaction</code> objects.
     * @param date date date month of <code>Transaction</code> objects.
     * @return total month expense of transactions
     */
    public static double getTotalExpense(TransactionList transactions, String date) {
        double totalExpense = 0;

        for (int i = 0; i < transactions.getSize(); i++) {
            Transaction temp = transactions.getTransaction(i);
            double amount = Double.parseDouble(transactions.getTransaction(i).getAmount());
            if (temp instanceof Expense & transactions.getTransaction(i).setMonth().equals(date)) {
                totalExpense = totalExpense + amount;
            }
        }
        return totalExpense;
    }

    /**
     * Gets highest month income transactions from user's full input string.
     *
     * @param transactions transactions List of <code>Transaction</code> objects.
     * @param date date date month of <code>Transaction</code> objects.
     * @return highest month income of transactions
     */
    public static String getHighestIncome(TransactionList transactions, String date) {
        double highestIncome = 0;
        String highestIncomes = null;

        for (int i = 0; i < transactions.getSize(); i++) {
            Transaction temp = transactions.getTransaction(i);
            double amount = Double.parseDouble(transactions.getTransaction(i).getAmount());
            if (temp instanceof Income & transactions.getTransaction(i).setMonth().equals(date)) {
                if (highestIncome < amount) {
                    highestIncomes = temp.toString();
                    highestIncome = amount;
                }
            }
        }
        return highestIncomes;
    }

    /**
     * Gets highest month expense transactions from user's full input string.
     *
     * @param transactions transactions List of <code>Transaction</code> objects.
     * @param date date date month of <code>Transaction</code> objects.
     * @return highest month expense of transactions
     */
    public static String getHighestExpense(TransactionList transactions, String date) {
        double highestExpense = 0;
        String highestExpenses = null;

        for (int i = 0; i < transactions.getSize(); i++) {
            Transaction temp = transactions.getTransaction(i);
            double amount = Double.parseDouble(transactions.getTransaction(i).getAmount());
            if (temp instanceof Expense & transactions.getTransaction(i).setMonth().equals(date)) {
                if (highestExpense < amount) {
                    highestExpenses = temp.toString();
                    highestExpense = amount;
                }
            }
        }
        return highestExpenses;
    }

    /**
     * Gets frequent month income category from user's full input string.
     *
     * @param transactions transactions List of <code>Transaction</code> objects.
     * @param date date date month of <code>Transaction</code> objects.
     * @return frequent month income category
     */
    public static Object getInCatFreq(TransactionList transactions, String date) {
        ArrayList incomeCate = new ArrayList();

        for (int i = 0; i < transactions.getSize(); i++) {
            Transaction temp = transactions.getTransaction(i);
            if (temp instanceof Income & transactions.getTransaction(i).setMonth().equals(date)) {
                incomeCate.add(temp.getTypeName(temp.toString()));
            }
        }
        return getFrequency(incomeCate);
    }

    /**
     * Gets frequent month expense category from user's full input string.
     *
     * @param transactions transactions List of <code>Transaction</code> objects.
     * @param date date date month of <code>Transaction</code> objects.
     * @return frequent month expense category
     */
    public static Object getExpCatFreq(TransactionList transactions, String date) {
        ArrayList expCate = new ArrayList();

        for (int i = 0; i < transactions.getSize(); i++) {
            Transaction temp = transactions.getTransaction(i);
            if (temp instanceof Expense & transactions.getTransaction(i).setMonth().equals(date)) {
                expCate.add(temp.getTypeName(temp.toString()));
            }
        }
        return getFrequency(expCate);
    }

    /**
     * get list elements frequent.
     *
     * @param list arraylist
     * @return map object with list elements and frequent.
     */
    public static Map<String, Long> getFrequency(ArrayList list) {
        return (Map<String, Long>) list.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
    }
}