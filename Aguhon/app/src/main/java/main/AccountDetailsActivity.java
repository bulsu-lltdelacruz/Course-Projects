package main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

import database.AccountType;
import database.DatabaseManager;
import database.ModelAccount;
import database.ModelStudentAccount;
import database.STRING;
import session.SESSION;
import session.TransactionType;
import session.ViewType;

public class AccountDetailsActivity extends AppCompatActivity {
    AccountType accountType;
    FloatingActionButton btnAddAccount;
    TextView tvheader;
    Context c = this;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.account_details);
        Intent intent = getIntent();
        accountType = (AccountType)intent.getSerializableExtra("accountType");
        initialize();
        renderTable();
        addListeners();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void initialize()
    {
        ImageButton back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());
        tvheader = findViewById(R.id.title);
        btnAddAccount = findViewById(R.id.add);
        table = findViewById(R.id.table);
        tvheader.setText(util.toTitleCase(accountType.toString())+" Accounts");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) btnAddAccount.getLayoutParams();
        layoutParams.setMargins(0,0,layoutParams.getMarginEnd(), 200);
    }
    private void addListeners()
    {
        if(accountType == AccountType.STUDENT)
        {
            btnAddAccount.setOnClickListener(v ->
            {
                Intent switchIntent = new Intent(c, CreateStudentAccountActivity.class);
                switchIntent.putExtra("viewType", ViewType.ACCOUNT);
                switchIntent.putExtra("accountType", accountType);
                switchIntent.putExtra("transactionType", TransactionType.CREATE);
                startActivity(switchIntent);
            });
        }else
        {
            btnAddAccount.setOnClickListener(v ->
            {
                Intent switchIntent = new Intent(c, CreateAccountActivity.class);
                switchIntent.putExtra("viewType", ViewType.ACCOUNT);
                switchIntent.putExtra("accountType", accountType);
                switchIntent.putExtra("transactionType", TransactionType.CREATE);
                startActivityForResult(switchIntent, 1);
            });
        }
    }
    private void renderTable()
    {
        if(accountType == AccountType.STUDENT)
        {
            if(DatabaseManager.studentAccounts.size() > 0)
                findViewById(R.id.emptyPlaceholder).setVisibility(TextView.GONE);
            int counter=0;
            for (ModelStudentAccount studentAccount:DatabaseManager.studentAccounts) {
                String fullName = studentAccount.getFirstName()+" "+(studentAccount.getMiddleName() != null && !studentAccount.getMiddleName().isEmpty() ?
                        studentAccount.getMiddleName().charAt(0) + ". " : "")+studentAccount.getLastName();
                TableRow row = util.createStudentRow(c, new String[]{fullName, studentAccount.getGradeLevel(), studentAccount.getSection()}, studentAccount.getId(), studentAccount.getFirstName(), accountType);
                table.addView(row);
                counter++;
                if(counter<DatabaseManager.studentAccounts.size())
                    table.addView(util.createDivider(c));
            }
        }else if(accountType == AccountType.TEACHER)
        {

            findViewById(R.id.sectionTv).setVisibility(TextView.GONE);
            if(DatabaseManager.teacherAccounts.size() > 0)
                findViewById(R.id.emptyPlaceholder).setVisibility(TextView.GONE);
            int counter=0;
            for (ModelAccount teacherAcc:DatabaseManager.teacherAccounts) {
                Log.d(teacherAcc.getAccountType().toString(), "renderTable: ");
                String fullName = teacherAcc.getFirstName()+" "+(teacherAcc.getMiddleName() != null && !teacherAcc.getMiddleName().isEmpty() ?
                        teacherAcc.getMiddleName().charAt(0) + ". " : "")+teacherAcc.getLastName();
                TableRow row = util.createStudentRow(c, new String[]{fullName, teacherAcc.getGradeLevel()}, teacherAcc.getId(), teacherAcc.getFirstName(), accountType);
                table.addView(row);
                counter++;
                if(counter<DatabaseManager.teacherAccounts.size())
                    table.addView(util.createDivider(c));
            }
        }else if(accountType == AccountType.ADMIN)
        {
            findViewById(R.id.gradeTv).setVisibility(TextView.GONE);
            findViewById(R.id.sectionTv).setVisibility(TextView.GONE);
            if(DatabaseManager.adminAccounts.size() > 0)
                findViewById(R.id.emptyPlaceholder).setVisibility(TextView.GONE);
            int counter=0;
            for (ModelAccount adminAcc:DatabaseManager.adminAccounts) {
                counter++;
                String fullName = adminAcc.getFirstName()+" "+(adminAcc.getMiddleName() != null && !adminAcc.getMiddleName().isEmpty() ?
                        adminAcc.getMiddleName().charAt(0) + ". " : "")+adminAcc.getLastName();
                TableRow row = util.createStudentRow(c, new String[]{fullName}, adminAcc.getId(), adminAcc.getFirstName(), accountType);
                table.addView(row);
                if(counter<DatabaseManager.adminAccounts.size())
                    table.addView(util.createDivider(c));
            }
        }

    }
}
