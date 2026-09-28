package database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;

import com.example.aguhan.R;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import main.util;
import session.GradeLevel;
import session.SESSION;
import session.TransactionType;
import session.ViewType;

public class DatabaseManager {
    public static LinkedList<ModelAccount> adminAccounts = new LinkedList<ModelAccount>();
    public static LinkedList<ModelAccount> teacherAccounts = new LinkedList<ModelAccount>();
    public static LinkedList<ModelStudentAccount> studentAccounts = new LinkedList<ModelStudentAccount>();
    public static LinkedList<ModelSchedule> scheduleList = new LinkedList<ModelSchedule>();
    public static LinkedList<ModelAnnouncement> announcementList = new LinkedList<ModelAnnouncement>();
    public static LinkedList<ModelHistory> historyList = new LinkedList<ModelHistory>();
    private static boolean skipHistoryCreation = false;

    public static void start()
    {
        ModelAccount admin1 = new ModelAccount(AccountType.ADMIN, "Leo", "Lorenzo", "Dela Cruz",
                "leolorenzo@gmail.com", "09667099184", "admin", "admin123", null);
        createAccount(AccountType.ADMIN, admin1);
        SESSION.currentAdminAccount = admin1;
        DatabaseSeeder.seedDatabase();
        SESSION.currentAdminAccount = null;
    }
    public static ErrorArr createAccount(AccountType accountType, ModelAccount account)
    {
        ErrorArr errorArr;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        switch (accountType)
        {
            case STUDENT:
                if (!(account instanceof ModelStudentAccount))
                    return new ErrorArr(false,"Account type is not a student");
                if(DuplicateCheck.isDuplicateAccount(account).success)
                    return new ErrorArr(false,DuplicateCheck.isDuplicateAccount(account).error);

                account.id = STRING.STUDENT+Counter.studentAccountCounter;
                Counter.studentAccountCounter++;
                studentAccounts.push((ModelStudentAccount) account);
                historyList.push(new ModelHistory(SESSION.currentAdminAccount, account, AccountType.STUDENT, TransactionType.CREATE, ViewType.ACCOUNT, formatted, -1));
                return new ErrorArr(true,"");
            case TEACHER:
                if(DuplicateCheck.isDuplicateAccount(account).success)
                {
                    return new ErrorArr(false,DuplicateCheck.isDuplicateAccount(account).error);
                }
                account.id = STRING.TEACHER+Counter.teacherAccountCounter;
                Counter.teacherAccountCounter++;
                teacherAccounts.push(account);
                historyList.push(new ModelHistory(SESSION.currentAdminAccount, account, AccountType.TEACHER, TransactionType.CREATE, ViewType.ACCOUNT, formatted, -1));
                return new ErrorArr(true,"");
            case ADMIN:
                if(DuplicateCheck.isDuplicateAccount(account).success)
                {
                    return new ErrorArr(false,DuplicateCheck.isDuplicateAccount(account).error);
                }

                account.id = STRING.ADMIN+Counter.adminAccountCounter;
                Counter.adminAccountCounter++;
                adminAccounts.push(account);
                historyList.push(new ModelHistory(SESSION.currentAdminAccount, account, AccountType.ADMIN, TransactionType.CREATE, ViewType.ACCOUNT, formatted, -1));
                return new ErrorArr(true,"");
        }
        return new ErrorArr(false, "Unknown Error");
    }

    public static ErrorArr editAccount(AccountType accountType, ModelAccount account, String id)
    {
        ErrorArr errorArr;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        int position = -1;
        switch (accountType)
        {
            case STUDENT:
                if (!(account instanceof ModelStudentAccount)) {
                    System.err.println("Error: Account type mismatch for STUDENT");
                    return new ErrorArr(false,"Account type is not a student");
                }
                if(DuplicateCheck.isDuplicateAccount(account, id))
                {
                    return new ErrorArr(false,"Duplicate Account");
                }
                for (ModelStudentAccount studentAccount: studentAccounts) {
                    if(studentAccount.getId().equals(id))
                        position = studentAccounts.indexOf(studentAccount);
                }
                if(position < 0)
                    return new ErrorArr(false,"Account Not Found");
                account.id = id;
                studentAccounts.set(position, (ModelStudentAccount) account);

                // history lang
                if (!skipHistoryCreation) {
                    historyList.push(new ModelHistory(SESSION.currentAdminAccount, account, AccountType.STUDENT, TransactionType.EDIT, ViewType.ACCOUNT, formatted, 0));
                }
                return new ErrorArr(true,"");

            case TEACHER:
                if(DuplicateCheck.isDuplicateAccount(account, id))
                {
                    return new ErrorArr(false,"Duplicate Account");
                }
                for (ModelAccount specAccount: teacherAccounts) {
                    if(specAccount.getId().equals(id))
                        position = teacherAccounts.indexOf(specAccount);
                }
                if(position < 0)
                    return new ErrorArr(false,"Account Not Found");
                account.id = id;
                teacherAccounts.set(position, account);

                if (!skipHistoryCreation) {
                    historyList.push(new ModelHistory(SESSION.currentAdminAccount, account, AccountType.TEACHER, TransactionType.EDIT, ViewType.ACCOUNT, formatted, 0));
                }
                return new ErrorArr(true,"");

            case ADMIN:
                if(DuplicateCheck.isDuplicateAccount(account, id))
                {
                    return new ErrorArr(false,"Duplicate Account");
                }
                for (ModelAccount specAccount: adminAccounts) {
                    if(specAccount.getId().equals(id))
                        position = adminAccounts.indexOf(specAccount);
                }
                if(position < 0)
                    return new ErrorArr(false,"Account Not Found");
                account.id = id;
                adminAccounts.set(position, account);

                if (!skipHistoryCreation) {
                    historyList.push(new ModelHistory(SESSION.currentAdminAccount, account, AccountType.ADMIN, TransactionType.EDIT, ViewType.ACCOUNT, formatted, 0));
                }
                return new ErrorArr(true,"");
        }
        return new ErrorArr(false, "Unknown Error");
    }
    public static boolean deleteAccount(String id, AccountType accountType, Context c)
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        if(accountType == database.AccountType.STUDENT)
        {
            for (ModelStudentAccount specAccount:studentAccounts) {

                if(specAccount.getId().equals(id))
                {
                    historyList.push(new ModelHistory(SESSION.currentAdminAccount, specAccount, database.AccountType.STUDENT, TransactionType.DELETE, ViewType.ACCOUNT, formatted, -1));
                    studentAccounts.remove(specAccount);
                    return true;
                }
            }
        }
        else
        {
            if(accountType == database.AccountType.TEACHER)
                for (ModelAccount specAccount:teacherAccounts) {

                    if(specAccount.getId().equals(id))
                    {
                        historyList.push(new ModelHistory(SESSION.currentAdminAccount, specAccount, AccountType.TEACHER, TransactionType.DELETE, ViewType.ACCOUNT, formatted, -1));
                        teacherAccounts.remove(specAccount);
                        return true;
                    }
                }
            else
                for (ModelAccount specAccount:adminAccounts) {

                    if(specAccount.getId().equals(id))
                    {
                        historyList.push(new ModelHistory(SESSION.currentAdminAccount, specAccount, AccountType.ADMIN, TransactionType.DELETE, ViewType.ACCOUNT, formatted, -1));
                        adminAccounts.remove(specAccount);
                        return true;
                    }
                }
        }
        return false;


    }
    public static ErrorArr createSchedule(ModelSchedule schedule)
    {
        ErrorArr errorArr;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);
        if(DuplicateCheck.isScheduleClashing(schedule))
        {
            return new ErrorArr(false,"Schedule cannot coexist");
        }
        schedule.id = STRING.SCHEDULE+Counter.scheduleCounter;
        Counter.scheduleCounter++;
        scheduleList.push(schedule);
        Log.d("waaaaaaaaaaaaaaa"+scheduleList.getLast().daysOfWeek, "createSchedule: ");

        historyList.push(new ModelHistory(SESSION.currentAdminAccount, schedule, TransactionType.CREATE, ViewType.SCHEDULE, formatted, -1));
        return new ErrorArr(true,"");
    }

    public static ErrorArr editSchedule(ModelSchedule schedule, String id)
    {
        ErrorArr errorArr;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        int position = -1;

        for (ModelSchedule specSchedule: scheduleList) {
            Log.d("FDSFDSSFD"+specSchedule.getId()+"  "+id, "editSchedule: ");
            if(specSchedule.getId().equals(id))
                position = scheduleList.indexOf(specSchedule);
        }
        if(position < 0)
            return new ErrorArr(false,"Schedule Not Found");
        schedule.id = id;
        scheduleList.set(position, schedule);

        if (!skipHistoryCreation) {

            historyList.push(new ModelHistory(SESSION.currentAdminAccount, schedule, TransactionType.EDIT, ViewType.SCHEDULE, formatted, 0));
        }

        return new ErrorArr(true,"");
    }
    public static boolean deleteSchedule(String id, Context c)
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        for (ModelSchedule specSchedule:scheduleList) {

            if(specSchedule.getId().equals(id))
            {
                historyList.push(new ModelHistory(SESSION.currentAdminAccount, specSchedule, TransactionType.DELETE, ViewType.SCHEDULE, formatted, -1));
                scheduleList.remove(specSchedule);
                return true;
            }
        }
        return false;
    }
    public static ModelSchedule getScheduleById(String id)
    {
        for (ModelSchedule item:scheduleList) {
            if(item.getId().equals(id))
                return item;
        }
        return null;
    }
    public static ModelAccount getTeacherAccountById(String id)
    {
        for (ModelAccount item:teacherAccounts) {
            if(item.getId().equals(id))
                return item;
        }
        return null;
    }
    public static ModelStudentAccount getStudentAccountById(String id)
    {
        for (ModelStudentAccount item:studentAccounts) {
            if(item.getId().equals(id))
                return item;
        }
        return null;
    }
    public static ModelAccount getAdminAccountById(String id)
    {
        for (ModelAccount item:adminAccounts) {
            if(item.getId().equals(id))
                return item;
        }
        return null;
    }
    public static ErrorArr createAnnouncement(ModelAnnouncement announcement)
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        announcement.id = STRING.ANNOUNCEMENT + Counter.announcementCounter;
        Counter.announcementCounter++;
        announcementList.push(announcement);
        historyList.push(new ModelHistory(SESSION.currentAdminAccount, announcement, TransactionType.CREATE, ViewType.ANNOUNCEMENT, formatted, -1));
        return new ErrorArr(true, "");
    }

    public static ErrorArr editAnnouncement(ModelAnnouncement announcement, String id)
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        int position = -1;
        for (ModelAnnouncement specAnnouncement : announcementList) {
            if (specAnnouncement.getId().equals(id))
                position = announcementList.indexOf(specAnnouncement);
        }

        if (position < 0)
            return new ErrorArr(false, "Announcement Not Found");

        announcement.id = id;
        announcementList.set(position, announcement);

        if (!skipHistoryCreation) {
            historyList.push(new ModelHistory(SESSION.currentAdminAccount, announcement, TransactionType.EDIT, ViewType.ANNOUNCEMENT, formatted, 0));
        }

        return new ErrorArr(true, "");
    }

    public static boolean deleteAnnouncement(String id, Context c)
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
        String formatted = sdf.format(now);

        for (ModelAnnouncement specAnnouncement : announcementList) {
            if (specAnnouncement.getId().equals(id)) {
                historyList.push(new ModelHistory(SESSION.currentAdminAccount, specAnnouncement, TransactionType.DELETE, ViewType.ANNOUNCEMENT, formatted, -1));
                announcementList.remove(specAnnouncement);
                return true;
            }
        }
        return false;
    }


    public static ModelHistory getHistoryById(String id) {
        for (ModelHistory history : historyList) {
            if (history.getId().equals(id)) {
                return history;
            }
        }
        return null;
    }
    public static LinkedList<ModelSchedule> getScheduleListWith(GradeLevel gradeLevel)
    {
        LinkedList<ModelSchedule> sched = new LinkedList<>();
        for (ModelSchedule item: scheduleList)
            if(gradeLevel.toString().equalsIgnoreCase(item.getGradeLevel()))
                sched.push(item);
        return sched;
    }

    public static ModelAccount getPreviousAccountState(ModelHistory history) {
        if (history.getTransactionType() != TransactionType.EDIT || history.getAccount() == null) {
            return null;
        }

        String accountId = history.getAccount().getId();
        ModelHistory previousHistory = null;

        for (int i = historyList.indexOf(history) + 1; i < historyList.size(); i++) {
            ModelHistory h = historyList.get(i);
            if (h.getViewType() == ViewType.ACCOUNT &&
                    h.getAccount() != null &&
                    h.getAccount().getId().equals(accountId)) {
                previousHistory = h;
                break;
            }
        }

        return previousHistory != null ? previousHistory.getAccount() : null;
    }

    public static ModelSchedule getPreviousScheduleState(ModelHistory history) {
        if (history.getTransactionType() != TransactionType.EDIT || history.getSchedule() == null) {
            return null;
        }

        String scheduleId = history.getSchedule().getId();
        ModelHistory previousHistory = null;

        for (int i = historyList.indexOf(history) + 1; i < historyList.size(); i++) {
            ModelHistory h = historyList.get(i);
            if (h.getViewType() == ViewType.SCHEDULE &&
                    h.getSchedule() != null &&
                    h.getSchedule().getId().equals(scheduleId)) {
                previousHistory = h;
                break;
            }
        }

        return previousHistory != null ? previousHistory.getSchedule() : null;
    }

    public static ModelAnnouncement getPreviousAnnouncementState(ModelHistory history) {
        if (history.getTransactionType() != TransactionType.EDIT || history.getAnnouncement() == null) {
            return null;
        }

        String announcementId = history.getAnnouncement().getId();
        ModelHistory previousHistory = null;

        for (int i = historyList.indexOf(history) + 1; i < historyList.size(); i++) {
            ModelHistory h = historyList.get(i);
            if (h.getViewType() == ViewType.ANNOUNCEMENT &&
                    h.getAnnouncement() != null &&
                    h.getAnnouncement().getId().equals(announcementId)) {
                previousHistory = h;
                break;
            }
        }

        return previousHistory != null ? previousHistory.getAnnouncement() : null;
    }

    public static boolean undoHistory(ModelHistory history, Context c) {
        TransactionType transactionType = history.getTransactionType();
        ViewType viewType = history.getViewType();

        boolean success = false;

        if (transactionType == TransactionType.EDIT) {
            skipHistoryCreation = true;

            if (viewType == ViewType.ACCOUNT && history.getAccount() != null) {
                ModelAccount previousAccount = getPreviousAccountState(history);
                if (previousAccount != null) {
                    ErrorArr result = editAccount(history.getAccountType(), previousAccount, history.getAccount().getId());
                    success = result.success;
                }
            } else if (viewType == ViewType.SCHEDULE && history.getSchedule() != null) {
                ModelSchedule previousSchedule = getPreviousScheduleState(history);
                if (previousSchedule != null) {
                    ErrorArr result = editSchedule(previousSchedule, history.getSchedule().getId());
                    success = result.success;
                }
            } else if (viewType == ViewType.ANNOUNCEMENT && history.getAnnouncement() != null) {
                ModelAnnouncement previousAnnouncement = getPreviousAnnouncementState(history);
                if (previousAnnouncement != null) {
                    ErrorArr result = editAnnouncement(previousAnnouncement, history.getAnnouncement().getId());
                    success = result.success;
                }
            }

            skipHistoryCreation = false;

            if (success) {
                historyList.remove(history);
            }

        } else if (transactionType == TransactionType.DELETE) {
            if (viewType == ViewType.ACCOUNT && history.getAccount() != null) {
                ModelAccount account = history.getAccount();
                if (history.getAccountType() == AccountType.STUDENT) {
                    studentAccounts.add((ModelStudentAccount) account);
                } else if (history.getAccountType() == AccountType.TEACHER) {
                    teacherAccounts.add(account);
                } else if (history.getAccountType() == AccountType.ADMIN) {
                    adminAccounts.add(account);
                }
                success = true;
            } else if (viewType == ViewType.SCHEDULE && history.getSchedule() != null) {
                scheduleList.add(history.getSchedule());
                success = true;
            } else if (viewType == ViewType.ANNOUNCEMENT && history.getAnnouncement() != null) {
                announcementList.add(history.getAnnouncement());
                success = true;
            }

            if (success) {
                historyList.remove(history);
            }
        }

        return success;
    }

    public static boolean canUndoHistory(ModelHistory history) {
        TransactionType transactionType = history.getTransactionType();
        ViewType viewType = history.getViewType();

        if (transactionType == TransactionType.CREATE) {
            return false;
        }

        if (transactionType == TransactionType.EDIT) {
            if (viewType == ViewType.ACCOUNT && history.getAccount() != null) {
                String accountId = history.getAccount().getId();
                AccountType accountType = history.getAccountType();

                boolean accountExists = false;
                if (accountType == AccountType.STUDENT) {
                    for (ModelStudentAccount acc : studentAccounts) {
                        if (acc.getId().equals(accountId)) {
                            accountExists = true;
                            break;
                        }
                    }
                } else if (accountType == AccountType.TEACHER) {
                    for (ModelAccount acc : teacherAccounts) {
                        if (acc.getId().equals(accountId)) {
                            accountExists = true;
                            break;
                        }
                    }
                } else if (accountType == AccountType.ADMIN) {
                    for (ModelAccount acc : adminAccounts) {
                        if (acc.getId().equals(accountId)) {
                            accountExists = true;
                            break;
                        }
                    }
                }

                return accountExists;

            } else if (viewType == ViewType.SCHEDULE && history.getSchedule() != null) {
                String scheduleId = history.getSchedule().getId();
                for (ModelSchedule s : scheduleList) {
                    if (s.getId().equals(scheduleId)) {
                        return true;
                    }
                }
                return false;

            } else if (viewType == ViewType.ANNOUNCEMENT && history.getAnnouncement() != null) {
                String announcementId = history.getAnnouncement().getId();
                for (ModelAnnouncement a : announcementList) {
                    if (a.getId().equals(announcementId)) {
                        return true;
                    }
                }
                return false;
            }
        }

        if (transactionType == TransactionType.DELETE) {
            return true;
        }

        return false;
    }

    public static boolean deleteHistoryPermanently(ModelHistory history, Context c) {
        ViewType viewType = history.getViewType();

        if (viewType == ViewType.ACCOUNT && history.getAccount() != null) {
            String accountId = history.getAccount().getId();

            historyList.removeIf(h ->
                    h.getViewType() == ViewType.ACCOUNT &&
                            h.getAccount() != null &&
                            h.getId().equals(history.getId())
            );

            //deleteAccount(accountId, history.getAccountType(), c);

        } else if (viewType == ViewType.SCHEDULE && history.getSchedule() != null) {
            String scheduleId = history.getSchedule().getId();

            historyList.removeIf(h ->
                    h.getViewType() == ViewType.SCHEDULE &&
                            h.getSchedule() != null &&
                            h.getId().equals(history.getId())
            );

            //deleteSchedule(scheduleId, c);

        } else if (viewType == ViewType.ANNOUNCEMENT && history.getAnnouncement() != null) {
            String announcementId = history.getAnnouncement().getId();

            historyList.removeIf(h ->
                    h.getViewType() == ViewType.ANNOUNCEMENT &&
                            h.getAnnouncement() != null &&
                            h.getId().equals(history.getId())
            );

            //deleteAnnouncement(announcementId, c);
        }

        return true;
    }



    public static ModelAccount getRandomTeacher()
    {
        if (teacherAccounts == null || teacherAccounts.isEmpty()) {
            return null;
        }

        int randomIndex = new java.util.Random().nextInt(teacherAccounts.size());
        return teacherAccounts.get(randomIndex);
    }

    public static ErrorArr checkScheduleConflict(ModelSchedule newSchedule, String excludeId) {
        String newSection = newSchedule.getSection();
        String newGrade = newSchedule.getGradeLevel();
        String newStart = newSchedule.getScheduleStart();
        String newEnd = newSchedule.getScheduleEnd();
        String newDays = newSchedule.getDaysOfWeek();
        String newTeacher = newSchedule.getTeacher();

        String[] newDaysArray = newDays.split(",");
        for (int i = 0; i < newDaysArray.length; i++) {
            newDaysArray[i] = newDaysArray[i].trim();
        }

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("h:mma");
            sdf.setLenient(false);

            java.util.Date newStartTime = sdf.parse(newStart.toUpperCase());
            java.util.Date newEndTime = sdf.parse(newEnd.toUpperCase());

            for (ModelSchedule existingSchedule : scheduleList) {
                if ((excludeId != null && existingSchedule.getId().equals(excludeId))) {
                    continue;
                }

                String existingDays = existingSchedule.getDaysOfWeek();
                String[] existingDaysArray = existingDays.split(",");
                for (int i = 0; i < existingDaysArray.length; i++) {
                    existingDaysArray[i] = existingDaysArray[i].trim();
                }

                boolean daysOverlap = false;//IF DAYS NG SCHEDULE OVERLAP
                for (String newDay : newDaysArray) {
                    for (String existingDay : existingDaysArray) {
                        if (newDay.equalsIgnoreCase(existingDay)) {
                            daysOverlap = true;
                            break;
                        }
                    }
                    if (daysOverlap) break;
                }

                if (!daysOverlap) {
                    continue;
                }

                java.util.Date existingStartTime = sdf.parse(existingSchedule.getScheduleStart().toUpperCase());
                java.util.Date existingEndTime = sdf.parse(existingSchedule.getScheduleEnd().toUpperCase());

                //IF TIME ITSELF NG SCHEDULE OVERLAP
                boolean timeOverlap = !newStartTime.before(existingStartTime) && newStartTime.before(existingEndTime) ||
                        !newEndTime.after(existingEndTime) && newEndTime.after(existingStartTime) ||
                        !newStartTime.after(existingStartTime) && !newEndTime.before(existingEndTime);

                if (!timeOverlap) {
                    continue;
                }

                //CONTINUE PAG DI OVERLAP, SO ABOT LANG DINE PAG OVERLAP :(((
                if (existingSchedule.getGradeLevel().equalsIgnoreCase(newGrade)
                    && existingSchedule.getSection().equalsIgnoreCase(newSection)) {//CHECK IF GRADELEVEL AND SECTION ARE SAME
                    String conflictMsg = String.format(
                            "Grade Level Schedule Conflict\n\n" +
                                    "This time slot is already occupied for %s.\n\n" +
                                    "Existing Schedule:\n" +
                                    "Section: %s\n" +
                                    "Subject: %s\n" +
                                    "Teacher: %s\n" +
                                    "Time: %s - %s\n" +
                                    "Days: %s",
                            newGrade,
                            existingSchedule.getSection(),
                            existingSchedule.getSubject(),
                            existingSchedule.getTeacher(),
                            existingSchedule.getScheduleStart(),
                            existingSchedule.getScheduleEnd(),
                            existingSchedule.getDaysOfWeek()
                    );
                    return new ErrorArr(false, conflictMsg);
                }
                if (newTeacher != null && !newTeacher.isEmpty() &&//CHECK IF TEACHER ARE SAME
                        existingSchedule.getTeacher() != null && !existingSchedule.getTeacher().isEmpty() &&
                        newTeacher.trim().equalsIgnoreCase(existingSchedule.getTeacher().trim())) {

                    String conflictMsg = String.format(
                            "Teacher Schedule Conflict\n\n" +
                                    "%s is already assigned to another class at this time.\n\n" +
                                    "Existing Schedule:\n" +
                                    "Grade Level: %s\n" +
                                    "Subject: %s\n" +
                                    "Time: %s - %s\n" +
                                    "Days: %s",
                            newTeacher,
                            existingSchedule.getGradeLevel(),
                            existingSchedule.getSubject(),
                            existingSchedule.getScheduleStart(),
                            existingSchedule.getScheduleEnd(),
                            existingSchedule.getDaysOfWeek()
                    );
                    return new ErrorArr(false, conflictMsg);
                }
            }

            return new ErrorArr(true, "No conflicts");

        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorArr(false, "Error checking schedule conflicts");
        }
    }

    public static ErrorArr checkAccount(AccountType accountType, String username, String password)
    {
        LinkedList<ModelAccount> accounts = null;
        switch (accountType)
        {
            case STUDENT:
                for (ModelStudentAccount student: studentAccounts) {
                    if(username.equals(student.username)&&password.equals(student.password))
                    {
                        return new ErrorArr(true, "");
                    }
                }
                break;
            case TEACHER:
                for (ModelAccount teacher: teacherAccounts) {
                    if(username.equals(teacher.username)&&password.equals(teacher.password))
                    {
                        return new ErrorArr(true, "");
                    }
                }
                break;
            case ADMIN:
                for (ModelAccount admin: adminAccounts) {
                    if(username.equals(admin.username)&&password.equals(admin.password))
                    {
                        return new ErrorArr(true, "");
                    }
                }

                break;
        }
        return new ErrorArr(false, "Invalid Username or Password");
    }

    public static ModelAccount getAccount(AccountType accountType, String username, String password)
    {
        LinkedList<ModelAccount> accounts = null;
        switch (accountType)
        {
            case STUDENT:
                for (ModelAccount student: studentAccounts) {
                    if(username.equals(student.username)&&password.equals(student.password))
                    {
                        return student;
                    }
                }
                break;
            case TEACHER:
                accounts = teacherAccounts;
                break;
            case ADMIN:
                accounts = adminAccounts;
                break;
        }
        if(accounts != null)
            for (ModelAccount account: accounts) {
                if(username.equals(account.username)&&password.equals(account.password))
                {
                    return account;
                }
            }
        return null;
    }
    public static int getHistoryCount(TransactionType transactionType)
    {
        int counter = 0;
        for (ModelHistory history:historyList)
            if(history.getTransactionType() == transactionType)
                counter++;
        return counter;
    }
    public static int getAccountCount(AccountType accountType)
    {
        if(accountType == AccountType.STUDENT)
            return studentAccounts.size();
        else if(accountType == AccountType.TEACHER)
            return teacherAccounts.size();
        else if(accountType == AccountType.ADMIN)
            return adminAccounts.size();

        return 0;
    }
    public static int geTeacherCountAtGradeLevel(GradeLevel gradeLevel)
    {
        int counter = 0;
        for (ModelAccount teacher : teacherAccounts)
        {
            if(teacher.getGradeLevel().trim().equalsIgnoreCase(gradeLevel.toString().trim()))
                counter++;
        }

        return counter;
    }
    public static int getHistoryCount()
    {
        return historyList.size();
    }

}

class DuplicateCheck{
    public static ErrorArr isDuplicateAccount(ModelAccount accountToBeChecked)
    {
        boolean isDuplicate = false;
        for (ModelStudentAccount account:DatabaseManager.studentAccounts) {
            if(accountToBeChecked.username.equals(account.username))
            {
                return new ErrorArr(true, "Duplicate Username");
            }
            if(accountToBeChecked.getEmailAddress().equals(account.getEmailAddress()))
            {
                return new ErrorArr(true, "Duplicate Email");
            }
            if(accountToBeChecked.getFirstName().equalsIgnoreCase(account.getFirstName())
                    && accountToBeChecked.getLastName().equalsIgnoreCase(account.getLastName()))
            {
                return new ErrorArr(true, "Duplicate Name");
            }
        }
        for (ModelAccount account:DatabaseManager.teacherAccounts) {
            if(accountToBeChecked.username.equals(account.username))
            {
                return new ErrorArr(true, "Duplicate Username");
            }
            if(accountToBeChecked.getEmailAddress().equals(account.getEmailAddress()))
            {
                return new ErrorArr(true, "Duplicate Email");
            }
            if(accountToBeChecked.getFirstName().equalsIgnoreCase(account.getFirstName())
                    && accountToBeChecked.getLastName().equalsIgnoreCase(account.getLastName()))
            {
                return new ErrorArr(true, "Duplicate Name");
            }
        }
        for (ModelAccount account:DatabaseManager.adminAccounts) {
            if(accountToBeChecked.username.equals(account.username))
            {
                return new ErrorArr(true, "Duplicate Username");
            }
            if(accountToBeChecked.getEmailAddress().equals(account.getEmailAddress()))
            {
                return new ErrorArr(true, "Duplicate Email");
            }
            if(accountToBeChecked.getFirstName().equalsIgnoreCase(account.getFirstName())
                    && accountToBeChecked.getLastName().equalsIgnoreCase(account.getLastName()))
            {
                return new ErrorArr(true, "Duplicate Name");
            }
        }
        return new ErrorArr(false, "Unknown Error");
    }
    public static boolean isDuplicateAccount(ModelAccount accountToBeChecked, String id)
    {
        boolean isDuplicate = false;

        for (ModelStudentAccount account:DatabaseManager.studentAccounts) {
            if(id.equals(account.getId()))
                continue;
            if(accountToBeChecked.username.equals(account.username) &&
                    !id.equals(account.getId())||(accountToBeChecked.getEmailAddress().equals(account.getEmailAddress()))||
                    (accountToBeChecked.getFirstName().equalsIgnoreCase(account.getFirstName()) &&
                            accountToBeChecked.getLastName().equalsIgnoreCase(account.getLastName()))
            )
            {
                isDuplicate = true;
            }
        }
        for (ModelAccount account:DatabaseManager.teacherAccounts) {
            if(id.equals(account.getId()))
                continue;
            if(accountToBeChecked.username.equals(account.username) &&
                    !id.equals(account.getId())||(accountToBeChecked.getEmailAddress().equals(account.getEmailAddress()))||
                    (accountToBeChecked.getFirstName().equalsIgnoreCase(account.getFirstName()) &&
                            accountToBeChecked.getLastName().equalsIgnoreCase(account.getLastName())))
            {
                isDuplicate = true;
            }
        }
        for (ModelAccount account:DatabaseManager.adminAccounts) {
            if(id.equals(account.getId()))
                continue;
            if(accountToBeChecked.username.equals(account.username) &&
                    !id.equals(account.getId())||(accountToBeChecked.getEmailAddress().equals(account.getEmailAddress()))||
                    (accountToBeChecked.getFirstName().equalsIgnoreCase(account.getFirstName()) &&
                            accountToBeChecked.getLastName().equalsIgnoreCase(account.getLastName())))
            {
                isDuplicate = true;
            }
        }
        return isDuplicate;
    }

    public static boolean isScheduleClashing(ModelSchedule schedule) {
        ErrorArr result = DatabaseManager.checkScheduleConflict(schedule, null);
        return !result.success;
    }
}
class Counter {
    static int adminAccountCounter = 0;
    static int teacherAccountCounter = 0;
    static int studentAccountCounter = 0;
    static int scheduleCounter = 0;
    static int announcementCounter = 0;
    static int historyCounter = 0;
}
