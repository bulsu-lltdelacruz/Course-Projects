package main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;

import org.w3c.dom.Text;

import database.AccountType;
import database.DatabaseManager;
import database.ModelAccount;
import database.ModelSchedule;
import session.TransactionType;
import session.ViewType;

public class ViewActivity  extends AppCompatActivity {
    TextView tvTitle;
    String id;
    String firstName;
    ViewType viewType;
    AccountType accountType;
    LinearLayout llView;
    LinearLayout llEdit;
    LinearLayout llDelete;

    TextView viewSub, editSub, deleteSub;
    Context c = this;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view);
        intent = getIntent();

        accountType = (AccountType)intent.getSerializableExtra("accountType");
        viewType = (ViewType) intent.getSerializableExtra("viewType");
        id = intent.getStringExtra("id");
        firstName = intent.getStringExtra("firstName");

        initialize();
        addListeners();
        if(viewType == ViewType.SCHEDULE)
        {
            tvTitle.setText("Manage Schedule Account");
            viewSub.setText("View Schedules");
            editSub.setText("Edit Schedules");
            deleteSub.setText("Delete Schedules");
            tvTitle.setText("Manage Schedule");
        }else if(viewType == ViewType.ACCOUNT)
        {
            viewSub.setText("View Accounts");
            editSub.setText("Edit Accounts");
            deleteSub.setText("Delete Accounts");
            tvTitle.setText("Manage "+ firstName+" 's Account");
        }

    }
    private void initialize()
    {
        ImageButton back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());
        tvTitle = findViewById(R.id.head);
        llView = findViewById(R.id.llView);
        llEdit = findViewById(R.id.llEdit);
        llDelete = findViewById(R.id.llDelete);
        viewSub = findViewById(R.id.viewSubtext);
        editSub = findViewById(R.id.editSubtext);
        deleteSub = findViewById(R.id.deleteSubtext);
    }

    private void addListeners()
    {
        View.OnClickListener onView = null;
        View.OnClickListener onEdit = null;
        View.OnClickListener onDelete = null;
        if(viewType == ViewType.ACCOUNT)
        {
            onView = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c, AccountViewActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                }
            };
            onEdit = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if(accountType == AccountType.STUDENT)
                    {
                        intent = new Intent(c, CreateStudentAccountActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("transactionType", TransactionType.EDIT);
                        intent.putExtra("accountType", accountType);
                    }
                    else if(accountType == AccountType.TEACHER){
                        intent = new Intent(c, CreateAccountActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("transactionType", TransactionType.EDIT);
                        intent.putExtra("accountType", accountType);
                    }
                    else if(accountType == AccountType.ADMIN){
                        intent = new Intent(c, CreateAccountActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("transactionType", TransactionType.EDIT);
                        intent.putExtra("accountType", accountType);
                    }
                    startActivityForResult(intent, 1);
                }
            };
            onDelete = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(accountType == AccountType.STUDENT)
                    {
                        ModelAccount studentToBeDeleted = DatabaseManager.getStudentAccountById(id);

                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to Delete this Record?");

                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            AlertDialog.Builder confirm = new AlertDialog.Builder(c);
                            if(DatabaseManager.deleteAccount(id, accountType,c))
                            {
                                confirm.setTitle("Confirm Deletion");
                                confirm.setMessage("Account successfully deleted");
                                confirm.setPositiveButton("Ok", (dialog1, which1) -> {
                                    finish();
                                });
                                confirm.show();
                            }
                        });
                        builder.setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        builder.show();
                    }else if(accountType == AccountType.TEACHER)
                    {
                        int scheduleCount = 0;
                        ModelAccount teacherToBeDeleted = DatabaseManager.getTeacherAccountById(id);
                        String teacherFullName = teacherToBeDeleted.getFirstName() + " " +
                                (teacherToBeDeleted.getMiddleName() != null && !teacherToBeDeleted.getMiddleName().isEmpty() ?
                                        teacherToBeDeleted.getMiddleName().substring(0,1) + ". " : "") +
                                teacherToBeDeleted.getLastName();
                        for (ModelSchedule schedule : DatabaseManager.scheduleList) {
                            if (schedule.getTeacher() != null &&
                                    schedule.getTeacher().trim().equalsIgnoreCase(teacherFullName.trim())) {
                                scheduleCount++;
                            }
                        }
                        String message = "";
                        if (scheduleCount > 0) {
                            message = "\nDeleting this record will produce conflicts with "
                                    + scheduleCount + " schedule/s currently assigned to this teacher.\n\n" +
                                    "Do you want to continue?";
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to Delete this Record?"+message);

                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            AlertDialog.Builder confirm = new AlertDialog.Builder(c);
                            if(DatabaseManager.deleteAccount(id, accountType,c))
                            {
                                confirm.setTitle("Confirm Deletion");
                                confirm.setMessage("Account successfully deleted");
                                confirm.setPositiveButton("Ok", (dialog1, which1) -> {
                                    finish();
                                });
                                confirm.show();
                            }
                        });
                        builder.setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        builder.show();

                    } else if(accountType == AccountType.ADMIN)
                    {
                        ModelAccount studentToBeDeleted = DatabaseManager.getStudentAccountById(id);

                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to Delete this Record?");

                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            AlertDialog.Builder confirm = new AlertDialog.Builder(c);
                            if(DatabaseManager.deleteAccount(id, accountType,c))
                            {
                                confirm.setTitle("Confirm Deletion");
                                confirm.setMessage("Account successfully deleted");
                                confirm.setPositiveButton("Ok", (dialog1, which1) -> {
                                    finish();
                                });
                                confirm.show();
                            }
                        });
                        builder.setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        builder.show();
                    }
                }
            };
        }else if(viewType == ViewType.SCHEDULE)
        {

            onView = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c, ScheduleViewActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            };
            onEdit = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(c, CreateEditScheduleActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("gradeLevel", DatabaseManager.getScheduleById(id).getGradeLevel());
                    intent.putExtra("transactionType", TransactionType.EDIT);
                    startActivityForResult(intent, 1);
                }
            };
            onDelete = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to Delete this Record?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        AlertDialog.Builder confirm = new AlertDialog.Builder(c);
                        if(DatabaseManager.deleteSchedule(id,c))
                        {
                            confirm.setTitle("Confirm Deletion");
                            confirm.setMessage("Schedule successfully deleted");
                            confirm.setPositiveButton("Ok", (dialog1, which1) -> {
                                finish();
                            });
                            confirm.show();
                        }else
                        {
                            confirm.setTitle("There was an Error");
                            confirm.setMessage("Schedule was not deleted");
                            confirm.setPositiveButton("Ok", (dialog1, which1) -> {
                                finish();
                            });
                            confirm.show();
                        }
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    builder.show();
                }
            };

        } else if(viewType == ViewType.ANNOUNCEMENT)
        {

        } else if(viewType == ViewType.HISTORY)
        {

        }
        llView.setOnClickListener(onView);
        llEdit.setOnClickListener(onEdit);
        llDelete.setOnClickListener(onDelete);
    }
}
