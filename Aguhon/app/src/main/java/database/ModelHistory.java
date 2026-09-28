package database;

import session.TransactionType;
import session.ViewType;

public class ModelHistory {
    public ModelAccount getAccount() {
        return account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public ModelAccount getAdminAccount() {
        return adminAccount;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    ModelAccount account;
    TransactionType transactionType;
    AccountType accountType;
    ModelAccount adminAccount;
    ModelSchedule schedule;
    ModelAnnouncement announcement;
    ViewType viewType;
    String timeStamp;
    String id;
    int index;
    public ModelHistory(ModelAccount adminAccount, ModelAccount modelAccount, AccountType accountType, TransactionType transactionType, ViewType viewType, String timeStamp, int index)
    {
        this.account = modelAccount;
        this.transactionType = transactionType;
        this.index = index;
        this.adminAccount = adminAccount;
        this.viewType = viewType;
        this.accountType = accountType;
        this.timeStamp = timeStamp;
        this.id = STRING.HISTORY+Counter.historyCounter;
        Counter.historyCounter++;
    }

    public ModelHistory(ModelAccount adminAccount,
                        ModelSchedule schedule,
                        TransactionType transactionType,
                        ViewType viewType,
                        String timeStamp,
                        int index)
    {
        this.schedule = schedule;
        this.transactionType = transactionType;
        this.index = index;
        this.adminAccount = adminAccount;
        this.viewType = viewType;
        this.timeStamp = timeStamp;
        this.id = STRING.HISTORY+Counter.historyCounter;
        Counter.historyCounter++;
    }
    public ModelHistory(ModelAccount adminAccount,
                        ModelAnnouncement announcement,
                        TransactionType transactionType,
                        ViewType viewType,
                        String timeStamp,
                        int index)
    {
        this.announcement = announcement;
        this.transactionType = transactionType;
        this.index = index;
        this.adminAccount = adminAccount;
        this.viewType = viewType;
        this.timeStamp = timeStamp;
        this.id = STRING.HISTORY+Counter.historyCounter;
        Counter.historyCounter++;
    }

    public ErrorArr restoreAccount()
    {
        if(transactionType == TransactionType.EDIT)
        {
            if(accountType == AccountType.STUDENT)
                DatabaseManager.studentAccounts.add(index, (ModelStudentAccount) account);
            else if(accountType == AccountType.TEACHER)
                DatabaseManager.teacherAccounts.add(index, account);
            else if(accountType == AccountType.ADMIN)
                DatabaseManager.adminAccounts.add(index, account);
        }else if(transactionType == TransactionType.DELETE)
        {

        }
        return new ErrorArr(false, "");
    }
    private boolean restoreSchedule()
    {
        return false;
    }
    private boolean restoreAnnouncement()
    {
        return false;
    }
    public ModelAccount getAdmin()
    {
        return adminAccount;
    }
    public String getAdminFirstName()
    {
        return adminAccount.getFirstName();
    }
    public String getAdminMiddleName()
    {
        return adminAccount.getMiddleName();
    }
    public String getAdminLastName()
    {
        return adminAccount.getLastName();
    }
    public ModelSchedule getSchedule() {
        return schedule;
    }

    public ModelAnnouncement getAnnouncement() {
        return announcement;
    }
}
