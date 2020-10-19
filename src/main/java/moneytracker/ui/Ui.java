package moneytracker.ui;

import moneytracker.exception.MoneyTrackerException;
import moneytracker.parser.Parser;
import moneytracker.transaction.TransactionList;
import moneytracker.transaction.Transaction;
import moneytracker.transaction.Income;
import moneytracker.transaction.Expense;
import moneytracker.transaction.Category;
import moneytracker.transaction.CategoryList;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains methods that interacts with the users such as obtaining commands
 * from user input and displaying messages to the users.
 */
public class Ui {
    public static final String LINE = "____________________________________________________________________";
    public static final String INDENT = "   ";

    /**
     * Gets the input stream from the user.
     * @return Input stream from the user.
     */
    public String readUserCommand() {
        Scanner in = new Scanner(System.in);
        System.out.print("You:  ");
        return in.nextLine().trim();
    }

    public void printWelcome() {
        String logo = " __  __                          _______             _             " + System.lineSeparator()
                + "|  \\/  |                        |__   __|           | |            " + System.lineSeparator()
                + "| \\  / | ___  _ __   ___ _   _     | |_ __ __ _  ___| | _____ _ __ " + System.lineSeparator()
                + "| |\\/| |/ _ \\| '_ \\ / _ \\ | | |    | | '__/ _` |/ __| |/ / _ \\ '__|" + System.lineSeparator()
                + "| |  | | (_) | | | |  __/ |_| |    | | | | (_| | (__|   <  __/ |   " + System.lineSeparator()
                + "|_|  |_|\\___/|_| |_|\\___|\\__, |    |_|_|  \\__,_|\\___|_|\\_\\___|_|   " + System.lineSeparator()
                + "                          __/ |                                    " + System.lineSeparator()
                + "                         |___/                                     " + System.lineSeparator();
        System.out.println(logo);
        System.out.println("Hello! What can I do for you?");
        printLine();
    }

    public void printAddedTransaction(TransactionList transactions) throws MoneyTrackerException {
        Transaction transactionToPrint = transactions.getTransaction(transactions.getSize() - 1);
        if (transactionToPrint instanceof Income) {
            System.out.println("Got it! I have added this income:");
        } else if (transactionToPrint instanceof Expense) {
            System.out.println("Got it! I have added this expense:");
        } else {
            throw new MoneyTrackerException("The transaction type is invalid");
        }
        printIndentation();
        System.out.println(transactionToPrint.toString());
        printIndentation();
        System.out.println("Now you have " + transactions.getSize() + " transactions in your list.");
        printLine();
    }

    public void printAddCategory(CategoryList categories) throws MoneyTrackerException {
        Category categoryToPrint = categories.getCategory(categories.getSize() - 1);
        if (categoryToPrint.getType().equals("INCOME")) {
            System.out.println("Got it! I have added this income category:");
        } else if (categoryToPrint.getType().equals("EXPENSE")) {
            System.out.println("Got it! I have added this expense category:");
        } else {
            throw new MoneyTrackerException("The category type is invalid");
        }
        printIndentation();
        System.out.println(categoryToPrint);
        printIndentation();
        System.out.println("Now you have " + categories.getSize() + " categories in your list.");
        printLine();
    }

    public void printRemoveCategory(int size, String description, String type) {
        System.out.println("Noted! I have removed this " + type + ": ");
        printIndentation();
        System.out.println(description);
        printIndentation();
        System.out.println("Now you have " + size + " categories in the list.");
        printLine();
    }

    public void printEditCategory(String oldDescription, String newDescription, String type) {
        System.out.println("Noted! I have edited this " + type + ": ");
        printIndentation();
        System.out.println("From " + oldDescription + " to " + newDescription);
        printLine();
    }

    public void printError(String errorMessage) {
        System.out.println("OOPS!! " + errorMessage);
        printLine();
    }

    public void printHelp() {
        System.out.println("Please refer this online user guide:");
        System.out.println("https://ay2021s1-tic4001-2.github.io/tp/UserGuide.html");
        printLine();
    }

    public void printGoodbye() {
        System.out.println("Bye! Hope to see you again soon.");
        printLine();
    }

    public void printClearConfirmation() {
        System.out.println("Are you sure you want to clear all data? Y / N");
    }

    public void printClearAllData(String confirmation) {
        if (confirmation.equals("Y")) {
            System.out.println("Noted! I have cleared all data.");
        } else if (confirmation.equals("N")) {
            System.out.println("Noted! Your data is untouched.");
        } else {
            System.out.println("Sorry, please enter \"Y\" or \"N\" only");
        }
        printLine();
    }

    public void printLine() {
        System.out.println(LINE);
    }

    public void printIndentation() {
        System.out.print(INDENT);
    }

    public void printListTransaction(TransactionList transactions) {
        if (transactions.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your transactions:");
            for (int i = 0; i < transactions.getSize(); i++) {
                printIndentation();
                System.out.println((i + 1) + ". " + transactions.getTransaction(i).toString());
            }
        }
        printLine();
    }

    public void printRemoveTransaction(int size, String description, String type) {
        System.out.println("Noted! I have removed this " + type + ": ");
        printIndentation();
        System.out.println(description);
        printIndentation();
        System.out.println("Now you have " + size + " transactions in the list.");
        printLine();
    }

    public void printFilteredTransactions(TransactionList transactions) {
        ArrayList<Integer> searchResultIndexes = transactions.getSearchResultIndexes();
        {
            for (int i = 0; i < searchResultIndexes.size(); i++) {
                printIndentation();
                System.out.println((i + 1) + ". " + transactions.getTransaction(searchResultIndexes.get(i)).toString());
            }
        }
        printLine();
    }

    public void printListTransactionExpenseOnly(TransactionList transactions) {
        transactions.setIsInitialized(true);
        transactions.clearSearchResultIndexes();

        if (transactions.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your expenses:");
            for (int i = 0; i < transactions.getSize(); i++) {
                Transaction parentRecord = transactions.getTransaction(i);
                if (parentRecord instanceof Expense) {
                    transactions.addSearchResultIndex(i);
                }
            }

        }

        printFilteredTransactions(transactions);

    }

    public void printListTransactionIncomeOnly(TransactionList transactions) {
        transactions.setIsInitialized(true);
        transactions.clearSearchResultIndexes();

        if (transactions.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your incomes:");
            for (int i = 0; i < transactions.getSize(); i++) {
                Transaction parentRecord = transactions.getTransaction(i);
                if (parentRecord instanceof Income) {
                    transactions.addSearchResultIndex(i);
                }
            }
        }

        printFilteredTransactions(transactions);
    }


    public void printListTransactionMonthOnly(TransactionList transactions, String listMonthName) {
        transactions.setIsInitialized(true);
        transactions.clearSearchResultIndexes();

        if (transactions.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your transactions for " + listMonthName + " :");
            for (int i = 0; i < transactions.getSize(); i++) {
                if (transactions.getTransaction(i).setMonth().equals(listMonthName)) {
                    transactions.addSearchResultIndex(i);
                }
            }

        }
        printFilteredTransactions(transactions);
    }


    public void printListTransactionIncomeByMonth(TransactionList transactions, String listMonthName) {
        transactions.setIsInitialized(true);
        transactions.clearSearchResultIndexes();

        if (transactions.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your income records for " + listMonthName + " :");
            for (int i = 0; i < transactions.getSize(); i++) {
                Transaction parentRecord = transactions.getTransaction(i);
                if (parentRecord instanceof Income & transactions.getTransaction(i).setMonth().equals(listMonthName)) {
                    transactions.addSearchResultIndex(i);
                }
            }
        }

        printFilteredTransactions(transactions);
    }

    public void printListTransactionExpenseByMonth(TransactionList transactions, String listMonthName) {
        transactions.setIsInitialized(true);
        transactions.clearSearchResultIndexes();

        if (transactions.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {

            System.out.println("Here are your expense records for " + listMonthName + " :");
            for (int i = 0; i < transactions.getSize(); i++) {
                Transaction parentRecord = transactions.getTransaction(i);
                if (parentRecord instanceof Expense & transactions.getTransaction(i).setMonth().equals(listMonthName)) {
                    transactions.addSearchResultIndex(i);
                }
            }
        }
        printFilteredTransactions(transactions);
    }

    private void printFilteredCategories(CategoryList categories) {
        ArrayList<Integer> searchResultIndexes = categories.getSearchResultIndexes();
        {
            for (int i = 0; i < searchResultIndexes.size(); i++) {
                printIndentation();
                System.out.println((i + 1) + ". " + categories.getCategory(searchResultIndexes.get(i)).toString());
            }
        }
        printLine();
    }

    public void printListCategory(CategoryList categories) {
        if (categories.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your categories:");
            for (int i = 0; i < categories.getSize(); i++) {
                printIndentation();
                System.out.println((i + 1) + ". " + categories.getCategory(i).toString());
            }
        }
        printLine();
    }

    public void printListCategoryExpenseOnly(CategoryList categories) {
        categories.setIsInitialized(true);
        categories.clearSearchResultIndexes();

        if (categories.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your expenses categories:");
            for (int i = 0; i < categories.getSize(); i++) {
                if (categories.getCategory(i).getType().equals("EXPENSE")) {
                    categories.addSearchResultIndex(i);
                }
            }
        }
        printFilteredCategories(categories);
    }

    public void printListCategoryIncomeOnly(CategoryList categories) {
        categories.setIsInitialized(true);
        categories.clearSearchResultIndexes();

        if (categories.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            System.out.println("Here are your income categories:");
            for (int i = 0; i < categories.getSize(); i++) {
                if (categories.getCategory(i).getType().equals("INCOME")) {
                    categories.addSearchResultIndex(i);
                }
            }
        }
        printFilteredCategories(categories);
    }

    /**
     * Gets transaction/category  Month report from user's full input string.
     *
     * @param transactions List of <code>Transaction</code> objects.
     * @param date month of <code>Transaction</code> objects.
     */
    public void printReport(TransactionList transactions, String date) {
        transactions.setIsInitialized(true);
        transactions.clearSearchResultIndexes();

        printLine();
        if (transactions.getSize() == 0) {
            System.out.println("Sorry, there is no record in your list.");
        } else {
            double totalIncome = Parser.getTotalIncome(transactions,date);
            double totalExpense = Parser.getTotalExpense(transactions,date);
            double balance = totalIncome - totalExpense;
            double average = totalExpense / Parser.getDaysOfMonth(date);
            String highestIncomeTrans = Parser.getHighestIncome(transactions,date);
            String highestExpTrans = Parser.getHighestExpense(transactions,date);
            var incomeCatFreq = Parser.getInCatFreq(transactions,date);
            var expCatFreq = Parser.getExpCatFreq(transactions,date);

            System.out.println("Here is your report for " + date + " :");
            System.out.println("Total Income: $" + totalIncome);
            System.out.println("Total Expense: $" + totalExpense);
            System.out.println("Balance: $" + balance);
            System.out.printf("Average Expense Per Day: $%.2f\n",(average));
            System.out.println("          *****          ");
            System.out.println("Highest Income: \n" + "  " + highestIncomeTrans);
            System.out.println("Highest Expense: \n" + "  " + highestExpTrans + "");
            System.out.println("          *****          ");
            System.out.println("Frequency of Income Category:\n" + "  " + incomeCatFreq);
            System.out.println("Frequency of Expense Category:\n" + "  " + expCatFreq);
            printLine();
        }
    }
}