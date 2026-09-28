package main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;

import database.AccountType;
import database.DatabaseManager;
import database.ModelAccount;
import database.ModelAnnouncement;
import database.ModelHistory;
import database.ModelSchedule;
import database.ModelStudentAccount;
import session.TransactionType;
import session.ViewType;

public class HistoryViewActivity extends AppCompatActivity {

    private TextView txtAdminName, txtTimestamp, txtActionType, txtViewType, txtItemModified;
    private TextView txtStateBefore, txtStateAfter;
    private TextView lblStateBefore, lblStateAfter;
    private LinearLayout containerStateBefore, containerStateAfter, containerActionButtons;
    private Button btnUndo, btnDeletePermanent, btnClose;
    private ImageView imgProfileBefore, imgProfileAfter, imgProfileCreate;

    private String historyId;
    private ModelHistory history;
    private Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.history_view);

        Intent intent = getIntent();
        historyId = intent.getStringExtra("id");

        initializeViews();

        loadHistoryData();

        setupListeners();
    }

    private void initializeViews() {
        ImageButton back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());
        txtAdminName = findViewById(R.id.txtAdminName);
        txtTimestamp = findViewById(R.id.txtTimestamp);
        txtActionType = findViewById(R.id.txtActionType);
        txtViewType = findViewById(R.id.txtViewType);
        txtItemModified = findViewById(R.id.txtItemModified);
        txtStateBefore = findViewById(R.id.txtStateBefore);
        txtStateAfter = findViewById(R.id.txtStateAfter);
        lblStateBefore = findViewById(R.id.lblStateBefore);
        lblStateAfter = findViewById(R.id.lblStateAfter);
        containerStateBefore = findViewById(R.id.containerStateBefore);
        containerStateAfter = findViewById(R.id.containerStateAfter);
        containerActionButtons = findViewById(R.id.containerActionButtons);
        btnUndo = findViewById(R.id.btnUndo);
        btnDeletePermanent = findViewById(R.id.btnDeletePermanent);
        btnClose = findViewById(R.id.btnClose);
        imgProfileBefore = findViewById(R.id.imgProfileBefore);
        imgProfileAfter = findViewById(R.id.imgProfileAfter);
    }

    private void loadHistoryData() {
        history = DatabaseManager.getHistoryById(historyId);

        if (history == null) {
            Toast.makeText(this, "History not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String adminName = history.getAdminFirstName() + " " +
                (history.getAdminMiddleName() != null && !history.getAdminMiddleName().isEmpty() ?
                        history.getAdminMiddleName().substring(0, 1) + ". " : "") +
                history.getAdminLastName();
        txtAdminName.setText(adminName);
        txtTimestamp.setText(history.getTimeStamp());
        txtActionType.setText(history.getTransactionType().toString());
        txtViewType.setText(history.getViewType().toString());

        switch (history.getTransactionType()) {
            case CREATE:
                txtActionType.setTextColor(getResources().getColor(R.color.green));
                break;
            case EDIT:
                txtActionType.setTextColor(getResources().getColor(R.color.orange));
                break;
            case DELETE:
                txtActionType.setTextColor(getResources().getColor(R.color.red));
                break;
        }

        displayHistoryDetails();

        configureActionButtons();
    }

    private void displayHistoryDetails() {
        ViewType viewType = history.getViewType();
        TransactionType transactionType = history.getTransactionType();

        if (viewType == ViewType.ACCOUNT) {
            displayAccountHistory();
        } else if (viewType == ViewType.SCHEDULE) {
            displayScheduleHistory();
        } else if (viewType == ViewType.ANNOUNCEMENT) {
            displayAnnouncementHistory();
        }
    }

    private void displayAccountHistory() {
        ModelAccount account = history.getAccount();
        if (account == null) return;

        String accountInfo = account.getFirstName() + " " + account.getLastName();
        txtItemModified.setText(history.getAccountType().toString() + " Account - " + accountInfo);

        TransactionType transactionType = history.getTransactionType();

        if (transactionType == TransactionType.EDIT) {
            lblStateBefore.setVisibility(View.VISIBLE);
            containerStateBefore.setVisibility(View.VISIBLE);
            lblStateAfter.setVisibility(View.VISIBLE);
            containerStateAfter.setVisibility(View.VISIBLE);

            lblStateBefore.setText("State Before Changes Were Made");

            ModelAccount previousAccount = DatabaseManager.getPreviousAccountState(history);
            if (previousAccount != null) {
                if(previousAccount.getImgBit()!=null)
                {
                    imgProfileBefore.setImageBitmap(previousAccount.getImgBit());
                    imgProfileBefore.setVisibility(View.VISIBLE);
                }
                if(account.getImgBit()!=null)
                {
                    imgProfileAfter.setImageBitmap(account.getImgBit());
                    imgProfileAfter.setVisibility(View.VISIBLE);
                }


                txtStateBefore.setText(formatAccountData(previousAccount));
            } else {
                txtStateBefore.setText("Previous state not available");
            }

            txtStateAfter.setText(formatAccountData(account));

        } else if (transactionType == TransactionType.DELETE) {
            lblStateBefore.setVisibility(View.VISIBLE);
            containerStateBefore.setVisibility(View.VISIBLE);
            lblStateBefore.setText("State Before Deletion");
            txtStateBefore.setText(formatAccountData(account));

        } else if (transactionType == TransactionType.CREATE) {
            lblStateAfter.setVisibility(View.VISIBLE);
            containerStateAfter.setVisibility(View.VISIBLE);

            imgProfileAfter.setImageBitmap(account.getImgBit());
            imgProfileAfter.setVisibility(View.VISIBLE);
            lblStateAfter.setText("Created Data");
            txtStateAfter.setText(formatAccountData(account));
        }
    }

    private void displayScheduleHistory() {
        ModelSchedule schedule = history.getSchedule();
        if (schedule == null) return;

        txtItemModified.setText("Schedule - " + schedule.getSubject());

        TransactionType transactionType = history.getTransactionType();

        if (transactionType == TransactionType.EDIT) {
            lblStateBefore.setVisibility(View.VISIBLE);
            containerStateBefore.setVisibility(View.VISIBLE);
            lblStateAfter.setVisibility(View.VISIBLE);
            containerStateAfter.setVisibility(View.VISIBLE);

            lblStateBefore.setText("State Before Changes Were Made");

            ModelSchedule previousSchedule = DatabaseManager.getPreviousScheduleState(history);
            if (previousSchedule != null) {
                txtStateBefore.setText(formatScheduleData(previousSchedule));
            } else {
                txtStateBefore.setText("Previous state not available");
            }

            txtStateAfter.setText(formatScheduleData(schedule));

        } else if (transactionType == TransactionType.DELETE) {
            lblStateBefore.setVisibility(View.VISIBLE);
            containerStateBefore.setVisibility(View.VISIBLE);
            lblStateBefore.setText("State Before Deletion");
            txtStateBefore.setText(formatScheduleData(schedule));

        } else if (transactionType == TransactionType.CREATE) {
            lblStateAfter.setVisibility(View.VISIBLE);
            containerStateAfter.setVisibility(View.VISIBLE);
            lblStateAfter.setText("Created Data");
            txtStateAfter.setText(formatScheduleData(schedule));
        }
    }

    private void displayAnnouncementHistory() {
        ModelAnnouncement announcement = history.getAnnouncement();
        if (announcement == null) return;

        txtItemModified.setText("Announcement - " + announcement.gettitle());

        TransactionType transactionType = history.getTransactionType();

        if (transactionType == TransactionType.EDIT) {
            lblStateBefore.setVisibility(View.VISIBLE);
            containerStateBefore.setVisibility(View.VISIBLE);
            lblStateAfter.setVisibility(View.VISIBLE);
            containerStateAfter.setVisibility(View.VISIBLE);

            lblStateBefore.setText("State Before Changes Were Made");

            ModelAnnouncement previousAnnouncement = DatabaseManager.getPreviousAnnouncementState(history);
            if (previousAnnouncement != null) {
                txtStateBefore.setText(formatAnnouncementData(previousAnnouncement));
            } else {
                txtStateBefore.setText("Previous state not available");
            }

            txtStateAfter.setText(formatAnnouncementData(announcement));

        } else if (transactionType == TransactionType.DELETE) {
            lblStateBefore.setVisibility(View.VISIBLE);
            containerStateBefore.setVisibility(View.VISIBLE);
            lblStateBefore.setText("State Before Deletion");
            txtStateBefore.setText(formatAnnouncementData(announcement));

        } else if (transactionType == TransactionType.CREATE) {
            lblStateAfter.setVisibility(View.VISIBLE);
            containerStateAfter.setVisibility(View.VISIBLE);
            lblStateAfter.setText("Created Data");
            txtStateAfter.setText(formatAnnouncementData(announcement));
        }
    }

    private String formatAccountData(ModelAccount account) {
        StringBuilder sb = new StringBuilder();
        sb.append("First Name: ").append(account.getFirstName()).append("\n");

        if (account.getMiddleName() != null && !account.getMiddleName().isEmpty()) {
            sb.append("Middle Name: ").append(account.getMiddleName()).append("\n");
        }

        sb.append("Last Name: ").append(account.getLastName()).append("\n");
        sb.append("Email: ").append(account.getEmailAddress()).append("\n");
        sb.append("Phone: ").append(account.getPhoneNumber()).append("\n");
        sb.append("Username: ").append(account.getUsername());

        if (account instanceof ModelStudentAccount) {
            ModelStudentAccount student = (ModelStudentAccount) account;
            sb.append("\nStudent Code: ").append(student.getStudentCode());
            sb.append("\nGrade Level: ").append(student.getGradeLevel());
            sb.append("\nRelation to Student: ").append(student.getRelationToStudent());
            sb.append("\nGuardian: ").append(student.getGuardianFirstName());

            if (student.getGuardianMiddleName() != null && !student.getGuardianMiddleName().isEmpty()) {
                sb.append(" ").append(student.getGuardianMiddleName());
            }

            sb.append(" ").append(student.getGuardianLastName());
        }

        return sb.toString();
    }

    private String formatScheduleData(ModelSchedule schedule) {
        StringBuilder sb = new StringBuilder();
        sb.append("Section: ").append(schedule.getSection()).append("\n");
        sb.append("Subject: ").append(schedule.getSubject()).append("\n");
        sb.append("Grade Level: ").append(schedule.getGradeLevel()).append("\n");
        sb.append("Start Time: ").append(schedule.getScheduleStart()).append("\n");
        sb.append("End Time: ").append(schedule.getScheduleEnd()).append("\n");
        sb.append("Days: ").append(schedule.getDaysOfWeek());
        return sb.toString();
    }

    private String formatAnnouncementData(ModelAnnouncement announcement) {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(announcement.gettitle()).append("\n");
        sb.append("Detail: ").append(announcement.getdetail()).append("\n");
        sb.append("Target Grade: ").append(announcement.getTargetGrade()).append("\n");
        sb.append("Date: ").append(announcement.getDate());
        return sb.toString();
    }

    private void configureActionButtons() {
        TransactionType transactionType = history.getTransactionType();

        if (transactionType == TransactionType.CREATE) {
            containerActionButtons.setVisibility(View.GONE);
        } else {
            containerActionButtons.setVisibility(View.VISIBLE);

            if (!DatabaseManager.canUndoHistory(history)) {
                btnUndo.setEnabled(false);
                btnUndo.setAlpha(0.5f);
            }
        }
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());

        btnUndo.setOnClickListener(v -> handleUndo());

        btnDeletePermanent.setOnClickListener(v -> handleDeletePermanent());
    }

    private void handleUndo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Confirm Undo");
        builder.setMessage("Are you sure you want to undo this change? This will restore the previous state.");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            boolean success = DatabaseManager.undoHistory(history, c);

            if (success) {
                Toast.makeText(c, "Change undone successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(c, "Failed to undo change", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void handleDeletePermanent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Confirm Permanent Deletion");
        builder.setMessage("Are you sure you want to permanently delete this history record? This action cannot be undone.");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            boolean success = DatabaseManager.deleteHistoryPermanently(history, c);

            if (success) {
                Toast.makeText(c, "History deleted permanently", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(c, "Failed to delete history", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}