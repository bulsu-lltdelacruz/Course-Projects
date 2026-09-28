package main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.AccountType;
import database.DatabaseManager;
import database.ModelAccount;
import database.ModelAnnouncement;
import database.ModelSchedule;
import database.ModelStudentAccount;
import session.GradeLevel;
import session.SESSION;
import session.TransactionType;
import session.ViewType;

public class AnnouncementDetailsActivity extends AppCompatActivity {
    AccountType accountType;
    FloatingActionButton btnAddAccount;
    GradeLevel gradeLevel;
    TextView tvheader;
    Context c = this;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.announcement_details);
        Intent intent = getIntent();
        gradeLevel = (GradeLevel) intent.getSerializableExtra("gradeLevel");
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
        table = findViewById(R.id.tableAnnouncements);
        tvheader.setText(util.toTitleCase(gradeLevel.toString())+" Announcements");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) btnAddAccount.getLayoutParams();
        layoutParams.setMargins(0,0,layoutParams.getMarginEnd(), 200);
    }
    private void addListeners()
    {
        btnAddAccount.setOnClickListener(v ->
        {
            Intent switchIntent = new Intent(c, CreateAnnouncementActivity.class);
            switchIntent.putExtra("viewType", ViewType.ANNOUNCEMENT);
            switchIntent.putExtra("gradeLevel", gradeLevel);
            switchIntent.putExtra("transactionType", TransactionType.CREATE);
            startActivityForResult(switchIntent, 1);
        });

    }
    private void renderTable()
    {
        if(DatabaseManager.announcementList.size() > 0)
            findViewById(R.id.emptyPlaceholder).setVisibility(TextView.GONE);
        View lastDivider = null;
        for (ModelAnnouncement announcement:DatabaseManager.announcementList) {
            String[] targetGrades = announcement.getTargetGrade().split(",");
            boolean match = false;
            for (String item: targetGrades) {
                item = item.trim();
                if(item.equalsIgnoreCase(gradeLevel.toString()))
                    match = true;
            }
            if(!match)
                continue;
            TableRow row = util.createAnnouncementRow(c, announcement.gettitle(), announcement.getTargetGrade(), announcement.getId());
            table.addView(row);
            lastDivider = util.createDivider(c);
            table.addView(lastDivider);
        }
        table.removeView(lastDivider);

    }
}
