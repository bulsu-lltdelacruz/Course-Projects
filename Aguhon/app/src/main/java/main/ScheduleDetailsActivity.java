package main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

import database.AccountType;
import database.DatabaseManager;
import database.ModelSchedule;
import database.ModelStudentAccount;
import session.GradeLevel;
import session.TransactionType;
import session.ViewType;

public class ScheduleDetailsActivity extends AppCompatActivity {

    FloatingActionButton btnAddAccount;
    GradeLevel gradeLevel;
    TextView tvheader;
    Context c = this;
    TableLayout table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.schedule_detail);
        Intent intent = getIntent();
        gradeLevel = (GradeLevel)intent.getSerializableExtra("gradeLevel");
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
        tvheader.setText(util.toTitleCase(gradeLevel.toString())+" Schedule");
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) btnAddAccount.getLayoutParams();
        layoutParams.setMargins(0,0,layoutParams.getMarginEnd(), 200);
    }
    private void addListeners()
    {
        btnAddAccount.setOnClickListener(v ->
        {
            Intent switchIntent = new Intent(c, CreateEditScheduleActivity.class);
            switchIntent.putExtra("transactionType", TransactionType.CREATE);
            switchIntent.putExtra("gradeLevel", util.toTitleCase(gradeLevel.toString()));
            startActivity(switchIntent);
        });
    }
    private void renderTable()
    {
        TableRow empty = findViewById(R.id.emptyPlaceholder);
        LinkedList<ModelSchedule> specSched =DatabaseManager.getScheduleListWith(gradeLevel);
        if(specSched.size()>0)
            table.removeView(empty);
        int counter=0;
        for (ModelSchedule schedule: specSched) {
            String sched = schedule.getScheduleStart()+" - "+schedule.getScheduleEnd();
            TableRow row = util.createScheduleRow(c, new String[]{schedule.getSubject()+"\n Section: "+schedule.getSection(),schedule.getTeacher() , sched, schedule.getDaysOfWeek()}, schedule.getId());
            table.addView(row);
            counter++;
            if(counter<specSched.size())
                table.addView(util.createDivider(c));
        }
    }
}
