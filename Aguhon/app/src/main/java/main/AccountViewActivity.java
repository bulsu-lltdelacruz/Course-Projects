package main; // Replace with your package name

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;

import database.AccountType;
import database.DatabaseManager;
import database.ModelAccount;
import database.ModelStudentAccount;

public class AccountViewActivity extends AppCompatActivity {

    // Declare all UI components
    private ImageView imgProfile;
    private TextView txtAccountName;
    private TextView txtAccountType;
    private TextView txtEmail;
    private TextView txtSection;
    private TextView txtPhoneNumber;
    private TextView txtUsername;
    private TextView txtGradeLevel;
    private TextView txtRelationToStudent;
    private TextView txtGuardianName;
    private Button btnClose;
    LinearLayout gardeLevelP;
    LinearLayout guardianNameP;
    LinearLayout relationP;
    LinearLayout sectionP;
    Context c = this;
    AccountType accountType;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.account_view);
        sectionP = findViewById(R.id.sectionParent);
        txtSection = findViewById(R.id.txtSection);
        imgProfile = findViewById(R.id.imgProfile);
        txtAccountName = findViewById(R.id.txtAccountName);
        txtAccountType = findViewById(R.id.txtAccountType);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtUsername = findViewById(R.id.txtUsername);
        txtGradeLevel = findViewById(R.id.txtGradeLevel);
        txtRelationToStudent = findViewById(R.id.txtGuardianRelation);
        txtGuardianName = findViewById(R.id.txtGuardianName);

        gardeLevelP = findViewById(R.id.gradeLevelParent);
        guardianNameP = findViewById(R.id.guardianNameparent);
        relationP = findViewById(R.id.guardianRelationparent);

        btnClose = findViewById(R.id.btnClose);

        getDataFromIntent();
        setupListeners();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        String firstName = null;
        String lastName = null;
        String accountTypeStr = null;
        String email = null;
        String phone = null;
        String username = null;
        String gradeLevel = null;
        String section = null;
        String guardianName = null;
        String relation = null;
        String status = null;

        accountType = (AccountType) intent.getSerializableExtra("accountType");
        id = intent.getStringExtra("id");
        switch (accountType)
        {
            case STUDENT:
                for (ModelStudentAccount student: DatabaseManager.studentAccounts) {
                    if(student.getId().equals(id.trim()))
                    {
                        firstName = student.getFirstName();
                        lastName = student.getLastName();
                        accountTypeStr = util.toTitleCase(student.getAccountType().toString());
                        email = student.getEmailAddress();
                        phone = student.getPhoneNumber();
                        username = student.getUsername();
                        gradeLevel = student.getGradeLevel();
                        section = student.getSection();
                        guardianName = student.getGuardianFirstName()+" "+student.getGuardianLastName();
                        relation = student.getRelationToStudent();
                        status = "active";
                        Bitmap imgBit = student.getImgBit();
                        if(imgBit != null)
                            imgProfile.setImageBitmap(util.cropToSquare(imgBit));
                        break;
                    }
                }
                break;
            case TEACHER:
                for (ModelAccount teacher: DatabaseManager.teacherAccounts) {
                    if (teacher.getId().equals(id.trim())) {
                        firstName = teacher.getFirstName();
                        lastName = teacher.getLastName();
                        accountTypeStr = util.toTitleCase(teacher.getAccountType().toString());
                        email = teacher.getEmailAddress();
                        phone = teacher.getPhoneNumber();
                        username = teacher.getUsername();
                        gradeLevel = teacher.getGradeLevel();
                        status = "active";
                        Bitmap imgBit = teacher.getImgBit();
                        if(imgBit != null)
                            imgProfile.setImageBitmap(imgBit);
                        break;
                    }
                }
                    break;

            case ADMIN:
                for (ModelAccount admin: DatabaseManager.adminAccounts) {
                    if (admin.getId().equals(id.trim())) {
                        firstName = admin.getFirstName();
                        lastName = admin.getLastName();
                        accountTypeStr = util.toTitleCase(admin.getAccountType().toString());
                        email = admin.getEmailAddress();
                        phone = admin.getPhoneNumber();
                        username = admin.getUsername();
                        status = "active";
                        Bitmap imgBit = admin.getImgBit();
                        if(imgBit != null)
                            imgProfile.setImageBitmap(imgBit);
                        break;
                    }
                }
                break;
        }



        if (firstName != null && lastName != null) {
            txtAccountName.setText(firstName + " " + lastName);
        }

        if (accountTypeStr != null) {
            txtAccountType.setText(accountTypeStr);
        }

        if (email != null) {
            txtEmail.setText(email);
        }

        if (phone != null) {
            txtPhoneNumber.setText(phone);
        }

        if (username != null) {
            txtUsername.setText(username);
        }if (section != null) {
            txtSection.setText(section);
        }else
        {
            sectionP.setVisibility(View.GONE);
            txtSection.setVisibility(View.GONE);
            findViewById(R.id.borderSection).setVisibility(View.GONE);
            findViewById(R.id.boder1).setVisibility(View.GONE);
        }

        if (gradeLevel != null) {
            txtGradeLevel.setText(gradeLevel);
        }else
        {
            gardeLevelP.setVisibility(View.GONE);
            findViewById(R.id.boder0).setVisibility(View.GONE);
            findViewById(R.id.boder1).setVisibility(View.GONE);
        }

        if (guardianName != null) {
            txtGuardianName.setText(guardianName);
        }else
        {
            guardianNameP.setVisibility(View.GONE);
            findViewById(R.id.boder2).setVisibility(View.GONE);
        }
        if (relation != null) {
            txtRelationToStudent.setText(relation);
        }else
        {
            relationP.setVisibility(View.GONE);
        }

    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());
    }
}