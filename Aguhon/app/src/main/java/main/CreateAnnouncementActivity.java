package main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aguhan.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import database.DatabaseManager;
import database.ErrorArr;
import database.ModelAnnouncement;
import session.GradeLevel;
import session.TransactionType;

public class CreateAnnouncementActivity extends AppCompatActivity {

    private TransactionType transactionType;
    private Context c = this;

    private EditText etTitle, etDetail, etDate, etTime;
    private CheckBox cbNursery, cbKinder, cbPrep;
    private GradeLevel gradeLevel;
    private Button btnCommit;
    private TextView tvHeader;

    private Intent intent;
    private String id;

    private Calendar selectedCalendar = Calendar.getInstance();
    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_announcement);
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
        gradeLevel = (GradeLevel)intent.getSerializableExtra("gradeLevel");
        transactionType = (TransactionType) intent.getSerializableExtra("transactionType");

        initialize();
        populateIfEditing();
        addListeners();
    }

    private void initialize() {
        ImageButton back = findViewById(R.id.btnBack);
        Button cancel = findViewById(R.id.btnCancel);
        back.setOnClickListener(v -> finish());
        cancel.setOnClickListener(v -> finish());
        tvHeader = findViewById(R.id.tvAnnouncementTitle);
        etTitle = findViewById(R.id.etAnnouncementTitle);
        etDetail = findViewById(R.id.etAnnouncementDetails);
        etDate = findViewById(R.id.etAnnouncementDate);
        etTime = findViewById(R.id.etAnnouncementTime);
        cbNursery = findViewById(R.id.cbNursery);
        cbKinder = findViewById(R.id.cbKinder);
        cbPrep = findViewById(R.id.cbPrep);
        btnCommit = findViewById(R.id.btnCommitAnnouncement);

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
        etTitle.setOnFocusChangeListener(focusScroll);
        etDetail.setOnFocusChangeListener(focusScroll);
        etDate.setOnFocusChangeListener(focusScroll);
        etTime.setOnFocusChangeListener(focusScroll);

        if(gradeLevel == GradeLevel.NURSERY)
            cbNursery.setChecked(true);
        else if(gradeLevel == GradeLevel.KINDER)
            cbKinder.setChecked(true);
        else if(gradeLevel == GradeLevel.PREP)
            cbPrep.setChecked(true);


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date now = new Date();
        selectedDate = dateFormat.format(now);
        selectedTime = timeFormat.format(now);
        etDate.setText(selectedDate);
        etTime.setText("12:00 AM");

        tvHeader.setText(util.toTitleCase(transactionType.toString()) + " Announcement");
    }

    private void populateIfEditing() {
        if (transactionType != TransactionType.EDIT) return;

        id = intent.getStringExtra("id");
        ModelAnnouncement targetAnnouncement = null;

        for (ModelAnnouncement ann : DatabaseManager.announcementList) {
            if (ann.getId().equals(id)) {
                targetAnnouncement = ann;
                break;
            }
        }

        if (targetAnnouncement == null) {
            Toast.makeText(c, "Announcement not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etTitle.setText(targetAnnouncement.gettitle());
        etDetail.setText(targetAnnouncement.getdetail());
        etDate.setText(targetAnnouncement.getDate());
        etTime.setText(targetAnnouncement.getTime());

        selectedDate = targetAnnouncement.getDate();
        selectedTime = targetAnnouncement.getTime();

        String targetGrade = targetAnnouncement.getTargetGrade();
        cbNursery.setChecked(targetGrade.contains("Nursery"));
        cbKinder.setChecked(targetGrade.contains("Kinder"));
        cbPrep.setChecked(targetGrade.contains("Prep"));

        if (transactionType == TransactionType.EDIT)
            btnCommit.setText("Update");
        TextView s = findViewById(R.id.txtView4);
        s.setText("Fill in the details to update Announcement.");
    }

    private void addListeners() {
        etDate.setOnClickListener(v -> showDatePicker());

        etTime.setOnClickListener(v -> showTimePicker());

        btnCommit.setOnClickListener(v -> {
            ErrorArr error = util.isAnyEmpty(c, new EditText[]{
                    etTitle,
                    etDetail,
                    etDate,
                    etTime
            });

            if(!error.success) {
                AlertDialog.Builder emptyBuilder = util.makeAlert(c, "The Following fields are empty", error.error);
                emptyBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
                return;
            }

            if(!cbNursery.isChecked() && !cbKinder.isChecked() && !cbPrep.isChecked()) {
                AlertDialog.Builder emptyBuilder = util.makeAlert(c, "Select Grade Level", "Grade Level must be selected");
                emptyBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
                return;
            }

            String title = etTitle.getText().toString().trim();
            String detail = etDetail.getText().toString().trim();
            String date = selectedDate;
            String time = selectedTime;

            StringBuilder targetGradeBuilder = new StringBuilder();
            if (cbNursery.isChecked()) targetGradeBuilder.append("Nursery, ");
            if (cbKinder.isChecked()) targetGradeBuilder.append("Kinder, ");
            if (cbPrep.isChecked()) targetGradeBuilder.append("Prep, ");
            String targetGrade = targetGradeBuilder.toString().trim();

            if (targetGrade.endsWith(",")) {
                targetGrade = targetGrade.substring(0, targetGrade.length() - 1).trim();
            }

            if (title.isEmpty() || detail.isEmpty() || date.isEmpty() || time.isEmpty() || targetGrade.isEmpty()) {
                Toast.makeText(c, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isAnnouncementScheduleValid(date, time))
            {
                Toast.makeText(c, "Date and time must not be in the past", Toast.LENGTH_SHORT).show();
                return;
            }

            ModelAnnouncement newAnnouncement = new ModelAnnouncement(title, detail, date, time, targetGrade);

            switch (transactionType) {
                case CREATE:
                    var result = DatabaseManager.createAnnouncement(newAnnouncement);
                    if (result.success) {
                        Toast.makeText(c, "Announcement created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(c, result.error, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case EDIT:
                    var editResult = DatabaseManager.editAnnouncement(newAnnouncement, id);
                    if (editResult.success) {
                        Toast.makeText(c, "Announcement updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(c, editResult.error, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case DELETE:
                    boolean deleted = DatabaseManager.deleteAnnouncement(id, c);
                    Toast.makeText(c,
                            deleted ? "Announcement deleted successfully" : "Announcement not found",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        });
    }

    private boolean isAnnouncementScheduleValid(String date, String time)
    {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
            String dateTimeString = date + " " + time;
            Date selectedDateTime = dateTimeFormat.parse(dateTimeString);
            Date now = new Date();

            if (selectedDateTime.before(now)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                    selectedDate = dateFormat.format(selectedCalendar.getTime());
                    etDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedCalendar.set(Calendar.MINUTE, minute);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    selectedTime = timeFormat.format(selectedCalendar.getTime());
                    etTime.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }
}