package moneytracker.command;

import moneytracker.exception.MoneyTrackerException;
import moneytracker.storage.Storage;
import moneytracker.transaction.ExpenseCategoryList;
import moneytracker.transaction.IncomeCategoryList;
import moneytracker.transaction.TransactionList;
import moneytracker.ui.Ui;

public class ListCommand extends Command {
    private final String fullCommand;

    /**
     * Initializes a <code>ListCommand</code> object.
     *
     * @param fullCommand User's full input string.
     */
    public ListCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public void execute(TransactionList transactions, Ui ui, Storage storage,
                        IncomeCategoryList incomeCategories,
                        ExpenseCategoryList expenseCategories) throws MoneyTrackerException {
        transactions.setIsInitialized(true);
        transactions.clearSearchResultIndexes();
        for (int i = 0; i < transactions.getSize(); i++) {
            transactions.addSearchResultIndex(i);

        }
        ui.printListTransaction(transactions);
    }
}
