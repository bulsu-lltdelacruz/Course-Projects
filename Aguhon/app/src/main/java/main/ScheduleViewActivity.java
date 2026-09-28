package main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;

import database.DatabaseManager;
import database.ModelSchedule;

public class ScheduleViewActivity extends AppCompatActivity {

    private TextView txtGradeSection;
    private TextView txtSubject;
    private TextView txtTeacher, txtSection;
    private TextView txtGradeLevel;
    private TextView txtTimeStart;
    private TextView txtTimeEnd;
    private TextView txtDays;
    private Button btnClose;

    private String scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.schedule_view);

        Intent intent = getIntent();
        scheduleId = intent.getStringExtra("id");

        initializeViews();
        loadScheduleData();
        setupListeners();
    }

    private void initializeViews() {
        txtGradeSection = findViewById(R.id.txtGradeSection);
        txtSubject = findViewById(R.id.txtSubject);
        txtSection = findViewById(R.id.txtSection);
        txtTeacher = findViewById(R.id.txtTeacher);
        txtGradeLevel = findViewById(R.id.txtGradeLevel);
        txtTimeStart = findViewById(R.id.txtTimeStart);
        txtTimeEnd = findViewById(R.id.txtTimeEnd);
        txtDays = findViewById(R.id.txtDays);
        btnClose = findViewById(R.id.btnClose);
    }

    private void loadScheduleData() {
        ModelSchedule schedule = DatabaseManager.getScheduleById(scheduleId);

        if (schedule != null) {
            txtSubject.setText(schedule.getSubject());
            txtTeacher.setText(schedule.getTeacher());
            txtSection.setText(schedule.getSection());
            txtGradeLevel.setText(schedule.getGradeLevel());
            txtTimeStart.setText(schedule.getScheduleStart());
            txtTimeEnd.setText(schedule.getScheduleEnd());
            txtDays.setText(schedule.getDaysOfWeek());

            txtGradeSection.setText(schedule.getGradeLevel());
        }
    }

    private void setupListeners() {

        btnClose.setOnClickListener(v -> finish());
    }
}