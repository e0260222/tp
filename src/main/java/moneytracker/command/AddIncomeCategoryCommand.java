package moneytracker.command;

import moneytracker.exception.MoneyTrackerException;
import moneytracker.parser.Parser;
import moneytracker.storage.Storage;
import moneytracker.transaction.ExpenseCategoryList;
import moneytracker.transaction.IncomeCategoryList;
import moneytracker.transaction.TransactionList;
import moneytracker.ui.Ui;

public class AddIncomeCategoryCommand extends Command {
    private final String fullCommand;

    /**
     * Initializes a <code>AddIncomeCategoryCommand</code> object.
     *
     * @param fullCommand User's full input string.
     */
    public AddIncomeCategoryCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public void execute(TransactionList transactions, Ui ui, Storage storage,
                        IncomeCategoryList incomeCategories,
                        ExpenseCategoryList expenseCategories) throws MoneyTrackerException {
        incomeCategories.addIncomeCategory((Parser.getIncomeCategory(fullCommand)));
        ui.printAddedExpenseCategory(expenseCategories);
        ui.printAddedIncomeCategory(incomeCategories);
    }
}
