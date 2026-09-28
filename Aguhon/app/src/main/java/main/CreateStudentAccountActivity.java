package main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aguhan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.AccountType;
import database.DatabaseManager;
import database.ErrorArr;
import database.ModelStudentAccount;
import session.TransactionType;

public class CreateStudentAccountActivity extends AppCompatActivity {
    AccountType accountType;
    TransactionType transactionType;
    FloatingActionButton btnAddAccount;
    TextView tvheader;
    EditText etFirstName;
    EditText etMiddleName;
    EditText etLastName;
    EditText etEmail;
    EditText etPhoneNumber;
    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etStudentCode;
    Spinner spnRelation;
    Spinner spnGradeLevel;
    Spinner spnSection;
    ImageView imgProfile;

    EditText etGuardianFirstName;
    EditText etGuardianMiddleName;
    EditText etGuardianLastName;
    Button btnCommit, btnOpenCam, btnOpenGallery;
    boolean isPasswordShow = false;


    ArrayAdapter<String> relationAdapter;

    ArrayAdapter<String> gradeLevelAdapter;
    Context c = this;
    Intent intent;
    String id;
    Bitmap imgBit;

    private final int REQUEST_CODE_CAM = 5;

    private final int REQUEST_CODE_CHOOSE_IMG = 6;

    private final int REQUEST_CAMERA_USAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_edit_student_account);
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
        accountType = (AccountType)intent.getSerializableExtra("accountType");
        transactionType = (TransactionType) intent.getSerializableExtra("transactionType");
        initialize();
        populateIfEditing();
        addListeners();

    }
    private void populateIfEditing()
    {
        if(transactionType == TransactionType.EDIT)
        {
            id = intent.getStringExtra("id");
            ModelStudentAccount studentAccount = null;
            for (ModelStudentAccount student:DatabaseManager.studentAccounts) {
                if(student.getId().equals(id))
                {
                    studentAccount = student;
                }
            }
            if(studentAccount == null)
            {
                Toast.makeText(c, "Error", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            etFirstName.setText(studentAccount.getFirstName());
            etMiddleName.setText(studentAccount.getMiddleName());
            etLastName.setText(studentAccount.getLastName());
            etEmail.setText(studentAccount.getEmailAddress());
            etPhoneNumber.setText(studentAccount.getPhoneNumber());
            etUsername.setText(studentAccount.getUsername());
            etPassword.setText(studentAccount.getPassword());
            etConfirmPassword.setText(studentAccount.getPassword());
            etStudentCode.setText(studentAccount.getStudentCode());
            spnRelation.setSelection(relationAdapter.getPosition(studentAccount.getRelationToStudent()));
            spnGradeLevel.setSelection(gradeLevelAdapter.getPosition(studentAccount.getGradeLevel()));
            etGuardianFirstName.setText(studentAccount.getGuardianFirstName());
            etGuardianMiddleName.setText(studentAccount.getGuardianMiddleName());
            etGuardianLastName.setText(studentAccount.getGuardianLastName());
            spnGradeLevel.setSelection(gradeLevelAdapter.getPosition(studentAccount.getGradeLevel()));
            imgBit = studentAccount.getImgBit();

            if(imgBit!=null)
            {
                imgProfile.setImageBitmap(util.cropToSquare(imgBit));
                imgProfile.setVisibility(View.VISIBLE);
            }

            btnCommit.setText("Update");
            findViewById(R.id.imgSpace1).setVisibility(View.VISIBLE);
            TextView s = findViewById(R.id.textView4);
            s.setText("Please fill in the details to update the account.");
        }
    }
    private void initialize()
    {
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

        ImageButton back = findViewById(R.id.btnBack);
        Button cancel = findViewById(R.id.btnCancel);
        back.setOnClickListener(v -> finish());
        cancel.setOnClickListener(v -> finish());
        tvheader = findViewById(R.id.title);
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etStudentCode = findViewById(R.id.etStudentCode);
        spnRelation = findViewById(R.id.spnRelationToStudent);
        spnGradeLevel = findViewById(R.id.spnGradeLevel);
        spnSection = findViewById(R.id.spnSection);
        etGuardianFirstName = findViewById(R.id.etGuardianFirstName);
        etGuardianMiddleName = findViewById(R.id.etGuardianMiddleName);
        etGuardianLastName = findViewById(R.id.etGuardianLastName);
        btnCommit = findViewById(R.id.btnCommit);
        btnOpenCam  = findViewById(R.id.btnTakePhoto);
        btnOpenGallery  = findViewById(R.id.btnChoosePhoto);
        imgProfile = findViewById(R.id.imgProfile);

        etFirstName.setOnFocusChangeListener(focusScroll);
        etMiddleName.setOnFocusChangeListener(focusScroll);
        etLastName.setOnFocusChangeListener(focusScroll);
        etEmail.setOnFocusChangeListener(focusScroll);
        etPhoneNumber.setOnFocusChangeListener(focusScroll);
        etUsername.setOnFocusChangeListener(focusScroll);
        etPassword.setOnFocusChangeListener(focusScroll);
        etConfirmPassword.setOnFocusChangeListener(focusScroll);
        etStudentCode.setOnFocusChangeListener(focusScroll);

        etGuardianFirstName.setOnFocusChangeListener(focusScroll);
        etGuardianMiddleName.setOnFocusChangeListener(focusScroll);
        etGuardianLastName.setOnFocusChangeListener(focusScroll);

        spnRelation.setOnFocusChangeListener(focusScroll);
        spnGradeLevel.setOnFocusChangeListener(focusScroll);
        spnSection.setOnFocusChangeListener(focusScroll);


        String[] relationItems = getResources().getStringArray(R.array.relation_to_student);
        String[] gradeLevelItems = getResources().getStringArray(R.array.grade_level);
        String[] sectionItems = {"A", "B"};
        relationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                relationItems
        );

        gradeLevelAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                gradeLevelItems
        );
        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                sectionItems
        );

        spnRelation.setAdapter(relationAdapter);
        spnGradeLevel.setAdapter(gradeLevelAdapter);
        spnSection.setAdapter(sectionAdapter);
        tvheader.setText(util.toTitleCase(transactionType.toString())+" "+ util.toTitleCase(accountType.toString())+" Account");
    }
    @SuppressLint("ClickableViewAccessibility")
    private void addListeners()
    {
        btnCommit.setOnClickListener(v -> {
            ErrorArr error = util.isAnyEmpty(c, new EditText[]{
                    etFirstName,
                    etLastName,
                    etEmail,
                    etPhoneNumber,
                    etUsername,
                    etPassword,
                    etConfirmPassword,
                    etStudentCode,
                    etGuardianFirstName,
                    etGuardianLastName
            });
            if(!error.success)
            {
                AlertDialog.Builder emptyBuilder = util.makeAlert(c, "The Following fields are empty", error.error);
                emptyBuilder.
                        setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }
            String firstName = etFirstName.getText().toString().trim();
            String middleName = etMiddleName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String studentCode = etStudentCode.getText().toString().trim();
            String relation = spnRelation.getSelectedItem().toString().trim();
            String gradeLevel = spnGradeLevel.getSelectedItem().toString().trim();
            String section = spnSection.getSelectedItem().toString().trim();
            String guardianFirstName = etGuardianFirstName.getText().toString().trim();
            String guardianMiddleName = etGuardianMiddleName.getText().toString().trim();
            String guardianLastName = etGuardianLastName.getText().toString().trim();

            if(!util.isPasswordMatch(password, confirmPassword))
            {
                Toast.makeText(c, "Password does not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!util.isValidEmail(email))
            {
                Toast.makeText(c, email+" is not a valid email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d{11}")) {
                Toast.makeText(c, "Invalid phone number format", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length()<8 || password.length()>16) {
                Toast.makeText(c, "Password must be between 8-16 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            ModelStudentAccount studentAccount = new ModelStudentAccount
                    (accountType, firstName, middleName, lastName, email,
                    phoneNumber, username, password, studentCode, gradeLevel, section, relation,
                            guardianFirstName, guardianMiddleName, guardianLastName, imgBit);

            switch (transactionType) {
                case CREATE:
                    var result = DatabaseManager.createAccount(AccountType.STUDENT, studentAccount);
                    if (result.success) {
                        Toast.makeText(c, "Account created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(c, result.error, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case EDIT:
                    var editResult = DatabaseManager.editAccount(AccountType.STUDENT, studentAccount, id);
                    if (editResult.success) {
                        Toast.makeText(c, "Account updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(c, editResult.error, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        });
        View.OnTouchListener onPasswordShow = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                EditText etPass = (EditText) v;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable rightDrawable = etPass.getCompoundDrawables()[DRAWABLE_RIGHT];

                    if (rightDrawable != null) {
                        float x = event.getX();

                        int drawableStart = etPass.getWidth() - etPass.getPaddingRight() - rightDrawable.getIntrinsicWidth();

                        if (x >= drawableStart) {
                            Drawable newRightDrawable;
                            Drawable leftDrawable = ContextCompat.getDrawable(c, R.drawable.padlock);

                            if (etPass.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                                newRightDrawable = ContextCompat.getDrawable(c, R.drawable.icon_eye_visible);
                                etPass.setCompoundDrawablesWithIntrinsicBounds(null, null, newRightDrawable, null);
                                etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                isPasswordShow = true;
                            } else {
                                newRightDrawable = ContextCompat.getDrawable(c, R.drawable.icon_eye);
                                etPass.setCompoundDrawablesWithIntrinsicBounds(null, null, newRightDrawable, null);
                                etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                isPasswordShow = false;
                            }

                            etPass.setSelection(etPass.getText().length());
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        btnOpenCam.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 21) {
                int permissionCheck = ContextCompat.checkSelfPermission(c, Manifest.permission.CAMERA);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_USAGE);
                } else {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CODE_CAM);
                }
            }


        });
        btnOpenGallery.setOnClickListener(v -> {
            Intent camera = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(camera, REQUEST_CODE_CHOOSE_IMG);

        });
        etPassword.setOnTouchListener(onPasswordShow);
        etConfirmPassword.setOnTouchListener(onPasswordShow);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAM && resultCode == RESULT_OK)
        {
            try{
                Bundle extras = data.getExtras();
                imgBit = (Bitmap) extras.get("data");
                imgProfile.setImageBitmap(util.cropToSquare(imgBit));
                imgProfile.setVisibility(View.VISIBLE);
                findViewById(R.id.imgSpace1).setVisibility(View.VISIBLE);
            }catch (Exception e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }

        }else if (requestCode == REQUEST_CODE_CHOOSE_IMG && resultCode == RESULT_OK) {
            try {
                Uri imgUri = data.getData();
                imgBit = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imgProfile.setImageBitmap(util.cropToSquare(imgBit));
                imgProfile.setVisibility(View.VISIBLE);
                findViewById(R.id.imgSpace1).setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_USAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CODE_CAM);
                } else {
                    Toast.makeText(getApplicationContext(), "Can't open camera without permission", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

}
