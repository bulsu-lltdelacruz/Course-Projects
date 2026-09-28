package main;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aguhan.R;

import database.DatabaseManager;
import database.ErrorArr;
import database.ModelSchedule;
import session.GradeLevel;
import session.TransactionType;

public class CreateEditScheduleActivity extends AppCompatActivity {

    String id;
    Spinner spnTeacher;
    Spinner spnSubject;
    Spinner spnSection;
    GradeLevel gradeLevel;
    TransactionType transactionType;
    TextView title;
    EditText etSchedStart;
    EditText etSchedEnd;
    LinearLayout cbParent;
    CheckBox[] cbdaysOfWeek = new CheckBox[6];
    Button btnCommit;
    Context c = this;
    ArrayAdapter<String> gradeLevelAdapter;
    ArrayAdapter<String> subjectAdapter;
    ArrayAdapter<String> teacherAdapter;
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_edit_schedule);
        View root = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets imeInsets = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime()).toPlatformInsets();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                view.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop(),
                        view.getPaddingRight(),
                        imeInsets.bottom
                );
            }

            return insets;
        });
        intent = getIntent();
        gradeLevel = GradeLevel.valueOf(intent.getStringExtra("gradeLevel").toUpperCase());
        transactionType = (TransactionType)intent.getSerializableExtra("transactionType");
        initialize();
        populateIfEditing();
        addListeners();
    }

    private void populateIfEditing()
    {
        if(transactionType == TransactionType.EDIT)
        {
            title.setText("Edit Schedule");
            id = intent.getStringExtra("id");
            ModelSchedule schedule = null;
            for (ModelSchedule specschedule:DatabaseManager.scheduleList) {
                if(specschedule.getId().equals(id))
                {
                    schedule = specschedule;
                }
            }
            if(schedule == null)
            {
                Toast.makeText(c, "Error", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            gradeLevel = GradeLevel.valueOf(schedule.getGradeLevel().toUpperCase());
            spnSubject.setSelection(subjectAdapter.getPosition(schedule.getSubject()));
            etSchedStart.setText(schedule.getScheduleStart());
            etSchedEnd.setText(schedule.getScheduleEnd());

            if(spnTeacher.getAdapter() != null && schedule.getTeacher() != null && !schedule.getTeacher().isEmpty()) {
                int teacherPosition = teacherAdapter.getPosition(schedule.getTeacher());
                if(teacherPosition >= 0) {
                    spnTeacher.setSelection(teacherPosition);
                }
            }

            btnCommit.setText("Update");
            TextView s =findViewById(R.id.textView4);
            s.setText("Please fill in the details to update schedule.");
            String[] daysOfWeekArr = schedule.getDaysOfWeek().split(",");
            for(int i=0; i<cbdaysOfWeek.length; i++)
            {
                for(int j=0; j<daysOfWeekArr.length; j++)
                {
                    if(daysOfWeekArr[j].trim().equalsIgnoreCase(cbdaysOfWeek[i].getText().toString().trim()))
                    {
                        cbdaysOfWeek[i].setChecked(true);
                    }
                }
            }
        }
    }

    private void addListeners() {
        btnCommit.setOnClickListener(v -> {
            ErrorArr emptyError = util.isAnyEmpty(c, new EditText[]{
                    etSchedStart,
                    etSchedEnd,
            });
            if(!emptyError.success)
            {
                AlertDialog.Builder emptyBuilder = util.makeAlert(c, "The Following fields are empty", emptyError.error);
                emptyBuilder.
                        setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }
            boolean isChecked = false;
            for(int i=0; i<cbdaysOfWeek.length; i++)
            {
                if(cbdaysOfWeek[i].isChecked())
                {
                    isChecked = true;
                }
            }
            if(!isChecked)
            {
                AlertDialog.Builder emptyBuilder = util.makeAlert(c, "Days of the week", "Please select days of the week");
                emptyBuilder.
                        setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            String gradeLevelStr = util.toTitleCase(gradeLevel.toString());
            String subject = spnSubject.getSelectedItem().toString();
            String section = spnSection.getSelectedItem().toString();

            String schedStart = etSchedStart.getText().toString().trim();
            String schedEnd = etSchedEnd.getText().toString().trim();
            String teacher = "";
            if(spnTeacher.getAdapter() != null)
                teacher = spnTeacher.getSelectedItem().toString();
            ErrorArr errorTime = isValidTimeRange(schedStart, schedEnd);
            if (!errorTime.success) {
                Toast.makeText(c, errorTime.error, Toast.LENGTH_LONG).show();
                return;
            }

            StringBuilder daysBuilder = new StringBuilder();
            for (CheckBox cb : cbdaysOfWeek) {
                if (cb != null && cb.isChecked()) {
                    if (daysBuilder.length() > 0) {
                        daysBuilder.append(", ");
                    }
                    daysBuilder.append(cb.getText().toString());
                }
            }
            String daysOfWeek = daysBuilder.toString();
            ModelSchedule schedule = new ModelSchedule(subject, gradeLevelStr, schedStart, schedEnd, daysOfWeek, teacher, section);

            String excludeId = (transactionType == TransactionType.EDIT) ? id : null;
            ErrorArr conflictCheck = DatabaseManager.checkScheduleConflict(schedule, excludeId);

            if (!conflictCheck.success) {
                AlertDialog.Builder conflictBuilder = new AlertDialog.Builder(c);
                conflictBuilder.setTitle("Schedule Conflict");
                conflictBuilder.setMessage(conflictCheck.error);
                conflictBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                conflictBuilder.show();
                return;
            }

            if (transactionType == TransactionType.CREATE) {
                ErrorArr error = DatabaseManager.createSchedule(schedule);
                if (error.success)
                {
                    Toast.makeText(c, "Schedule created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(c, error.error, Toast.LENGTH_SHORT).show();

            } else if (transactionType == TransactionType.EDIT) {
                ErrorArr error = DatabaseManager.editSchedule(schedule, id);
                if (error.success)
                {
                    Toast.makeText(c, "Schedule updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(c, error.error, Toast.LENGTH_SHORT).show();

            } else if (transactionType == TransactionType.DELETE) {
                DatabaseManager.deleteSchedule(id, c);
                finish();
            }
        });
    }


    private void initialize() {
        ImageButton back = findViewById(R.id.btnBack);
        Button cancel = findViewById(R.id.btnCancel);
        back.setOnClickListener(v -> finish());
        cancel.setOnClickListener(v -> finish());
        title = findViewById(R.id.textView3);
        cbParent = findViewById(R.id.cbParent);
        spnSubject = findViewById(R.id.spnSubject);
        etSchedStart = findViewById(R.id.etScheduleStart);
        etSchedEnd = findViewById(R.id.etScheduleEnd);
        btnCommit = findViewById(R.id.btnCreateSchedule);
        spnTeacher = findViewById(R.id.spnTeacher);
        spnSection = findViewById(R.id.spnSection);

        ScrollView scrollView = findViewById(R.id.main);
        View.OnFocusChangeListener focusScroll = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.post(() -> {
                        int viewY = v.getTop();
                        int viewHeight = v.getHeight();
                        int scrollHeight = scrollView.getHeight();
                        int centerY = viewY - (scrollHeight / 3) + (viewHeight / 2);

                        scrollView.smoothScrollTo(0, centerY);
                    });
                }
            }
        };
        spnSubject.setOnFocusChangeListener(focusScroll);
        etSchedStart.setOnFocusChangeListener(focusScroll);
        etSchedEnd.setOnFocusChangeListener(focusScroll);
        spnTeacher.setOnFocusChangeListener(focusScroll);
        spnSection.setOnFocusChangeListener(focusScroll);

        int counter = 0;
        for (int i = 0; i < cbParent.getChildCount(); i++) {
            if (cbParent.getChildAt(i) instanceof CheckBox) {
                cbdaysOfWeek[counter] = (CheckBox) cbParent.getChildAt(i);
                counter++;
            }
        }

        String[] subjectItems = getResources().getStringArray(R.array.subject_item);
        subjectAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                subjectItems
        );

        String[] teacherItems = new String[DatabaseManager.geTeacherCountAtGradeLevel(gradeLevel)];
        int teacherCounter = 0;
        for (int i =0; i< DatabaseManager.teacherAccounts.size(); i++) {
            if(DatabaseManager.teacherAccounts.get(i).getGradeLevel().trim().equalsIgnoreCase(gradeLevel.toString().trim()))
            {
                String firstName = DatabaseManager.teacherAccounts.get(i).getFirstName();
                String middleName = DatabaseManager.teacherAccounts.get(i).getMiddleName();
                String lastName = DatabaseManager.teacherAccounts.get(i).getLastName();

                String teacherName = firstName + " " +
                        (middleName != null && !middleName.isEmpty() ? middleName.substring(0,1) + ". " : "") +
                        lastName;

                teacherItems[teacherCounter] = teacherName;
                teacherCounter++;
            }
        }
        String[] sectionItems = {"A", "B"};
        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                sectionItems
        );
        teacherAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                teacherItems
        );

        if (teacherItems.length <= 0)
            Toast.makeText(c, "There is currently no record for teachers in this grade level, you can add this later", Toast.LENGTH_LONG).show();
        else
            spnTeacher.setAdapter(teacherAdapter);

        spnSubject.setAdapter(subjectAdapter);
        spnSection.setAdapter(sectionAdapter);
        etSchedStart.setOnClickListener(v -> showTimePicker(etSchedStart));
        etSchedEnd.setOnClickListener(v -> showTimePicker(etSchedEnd));
    }

    private void showTimePicker(EditText editText) {
        int hour = 8;
        int minute = 0;

        String current = editText.getText().toString();
        if (!current.isEmpty()) {
            String[] parts = current.split(":|\\s");
            try {
                hour = Integer.parseInt(parts[0]);
                minute = Integer.parseInt(parts[1]);
            } catch (Exception e) {
                // Keep default values
            }
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int hourFormatted = hourOfDay % 12;
                    if (hourFormatted == 0) hourFormatted = 12;

                    String time = String.format("%d:%02d%s", hourFormatted, minute1, amPm);
                    editText.setText(time);
                },
                hour,
                minute,
                false
        );

        timePickerDialog.show();
    }

    private ErrorArr isValidTimeRange(String start, String end) {//if falase

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("h:mma");
            sdf.setLenient(false);

            java.util.Date startTime = sdf.parse(start.toUpperCase());
            java.util.Date endTime = sdf.parse(end.toUpperCase());
            java.util.Date earliest = sdf.parse("7:00AM");
            java.util.Date latest = sdf.parse("6:00PM");

            if (startTime == null || endTime == null)
                return new ErrorArr(false, "Schedule time fields are empty");

            if (startTime.before(earliest) || endTime.after(latest))
                return new ErrorArr(false, "Schedule is only from 7:00AM to 6:00PM");

            ErrorArr error = new ErrorArr(true, "");
            if(!startTime.before(endTime))
            {
                error.success = false;
                error.error = "Start of Schedule must be before end of schedule";
            }

            return error;

        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorArr(false, "There was an error");
        }
    }
}