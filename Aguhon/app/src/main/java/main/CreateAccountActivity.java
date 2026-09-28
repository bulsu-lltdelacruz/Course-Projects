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

import database.AccountType;
import database.DatabaseManager;
import database.ErrorArr;
import database.ModelAccount;
import database.ModelSchedule;
import main.util;
import session.TransactionType;

public class CreateAccountActivity extends AppCompatActivity {

    private AccountType accountType;
    private TransactionType transactionType;
    private Context c = this;

    private EditText etFirstName, etMiddleName, etLastName, etEmail, etPhoneNumber, etUsername, etPassword, etConfirmPassword;
    private Button btnCommit, btnOpenCam, btnOpenGallery;
    private ImageView imgProfile;
    private TextView tvHeader;
    private Spinner spnGradeLevel;

    private Intent intent;
    private String id;
    private Bitmap imgBit;
    private ArrayAdapter<String> gradeLevelAdapter;
    private final int REQUEST_CODE_CAM = 5;
    private final int REQUEST_CODE_CHOOSE_IMG = 6;
    private final int REQUEST_CAMERA_USAGE = 200;

    boolean isPasswordShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_edit_account);
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
        accountType = (AccountType) intent.getSerializableExtra("accountType");
        transactionType = (TransactionType) intent.getSerializableExtra("transactionType");

        initialize();
        populateIfEditing();
        addListeners();

        if(accountType == AccountType.ADMIN)
        {
            spnGradeLevel.setVisibility(View.GONE);
            findViewById(R.id.label_grade_level).setVisibility(View.GONE);
        }

    }

    private void initialize() {
        ImageButton back = findViewById(R.id.btnBack);
        Button cancel = findViewById(R.id.btnCancel);
        back.setOnClickListener(v -> finish());
        cancel.setOnClickListener(v -> finish());
        tvHeader = findViewById(R.id.title);
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        spnGradeLevel = findViewById(R.id.spnGradeLevel);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCommit = findViewById(R.id.btnCommit);
        btnOpenCam  = findViewById(R.id.btnTakePhoto);
        btnOpenGallery  = findViewById(R.id.btnChoosePhoto);
        imgProfile = findViewById(R.id.imgProfile);

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
        etFirstName.setOnFocusChangeListener(focusScroll);
        etMiddleName.setOnFocusChangeListener(focusScroll);
        etLastName.setOnFocusChangeListener(focusScroll);

        spnGradeLevel.setOnFocusChangeListener(focusScroll);

        etEmail.setOnFocusChangeListener(focusScroll);
        etPhoneNumber.setOnFocusChangeListener(focusScroll);
        etUsername.setOnFocusChangeListener(focusScroll);
        etPassword.setOnFocusChangeListener(focusScroll);
        etConfirmPassword.setOnFocusChangeListener(focusScroll);

        String[] gradeLevelItems = getResources().getStringArray(R.array.grade_level);

        gradeLevelAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                gradeLevelItems
        );

        spnGradeLevel.setAdapter(gradeLevelAdapter);


        tvHeader.setText(util.toTitleCase(transactionType.toString()) + " " +
                util.toTitleCase(accountType.toString()) + " Account");
    }

    private void populateIfEditing() {
        if (transactionType != TransactionType.EDIT) return;

        id = intent.getStringExtra("id");
        ModelAccount targetAccount = null;

        if (accountType == AccountType.ADMIN) {
            for (ModelAccount acc : DatabaseManager.adminAccounts) {
                if (acc.getId().equals(id)) {
                    targetAccount = acc;
                    break;
                }
            }
        } else if (accountType == AccountType.TEACHER) {
            for (ModelAccount acc : DatabaseManager.teacherAccounts) {
                if (acc.getId().equals(id)) {
                    targetAccount = acc;
                    break;
                }
            }
        }

        if (targetAccount == null) {
            Toast.makeText(c, "Account not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etFirstName.setText(targetAccount.getFirstName());
        etMiddleName.setText(targetAccount.getMiddleName());
        etLastName.setText(targetAccount.getLastName());
        etEmail.setText(targetAccount.getEmailAddress());
        etPhoneNumber.setText(targetAccount.getPhoneNumber());
        etUsername.setText(targetAccount.getUsername());
        etPassword.setText(targetAccount.getPassword());
        etConfirmPassword.setText(targetAccount.getPassword());
        imgBit = targetAccount.getImgBit();
        spnGradeLevel.setSelection(gradeLevelAdapter.getPosition(targetAccount.getGradeLevel()));
        if(imgBit!=null)
        {
            imgProfile.setImageBitmap(util.cropToSquare(imgBit));
            imgProfile.setVisibility(View.VISIBLE);
        }

        btnCommit.setText("Update");
        findViewById(R.id.imgSpace1).setVisibility(View.VISIBLE);
        findViewById(R.id.imgSpace2).setVisibility(View.VISIBLE);
        TextView s = findViewById(R.id.textView4);
        s.setText("Please fill in the details to update the account.");
    }



    @SuppressLint("ClickableViewAccessibility")
    private void addListeners() {
        btnCommit.setOnClickListener(v -> {
            ErrorArr error = util.isAnyEmpty(c, new EditText[]{
                    etFirstName,
                    etLastName,
                    etEmail,
                    etPhoneNumber,
                    etUsername,
                    etPassword,
                    etConfirmPassword,
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
            String grade = "";
            if(accountType!= AccountType.ADMIN)
                grade = spnGradeLevel.getSelectedItem().toString();

            if ( !util.isPasswordMatch(password, confirmPassword)) {
                Toast.makeText(c, "Password does not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!util.isValidEmail(email)) {
                Toast.makeText(c, "Invalid email format", Toast.LENGTH_SHORT).show();
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

            if (transactionType == TransactionType.EDIT && accountType == AccountType.TEACHER) {
                ModelAccount oldAccount = null;
                for (ModelAccount acc : DatabaseManager.teacherAccounts) {
                    if (acc.getId().equals(id)) {
                        oldAccount = acc;
                        break;
                    }
                }

                if (oldAccount != null &&
                        oldAccount.getGradeLevel() != null &&
                        !oldAccount.getGradeLevel().trim().equalsIgnoreCase(grade.trim())) {

                    String teacherFullName = oldAccount.getFirstName() + " " +
                            (oldAccount.getMiddleName() != null && !oldAccount.getMiddleName().isEmpty() ?
                                    oldAccount.getMiddleName().substring(0,1) + ". " : "") +
                            oldAccount.getLastName();

                    int scheduleCount = 0;
                    for (ModelSchedule schedule : DatabaseManager.scheduleList) {
                        if (schedule.getTeacher() != null &&
                                schedule.getTeacher().trim().equalsIgnoreCase(teacherFullName.trim())) {
                            scheduleCount++;
                        }
                    }

                    if (scheduleCount > 0) {
                        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(c);
                        warningBuilder.setTitle("Warning: Grade Level Change");
                        warningBuilder.setMessage(
                                "Changing the grade level will produce conflicts with "
                                        + scheduleCount + " schedule/s currently assigned to this teacher.\n\n" +
                                        "Do you want to continue?"
                        );
                        warningBuilder.setPositiveButton("Yes, Continue", (dialog, which) -> {
                            performAccountUpdate(firstName, middleName, lastName, email,
                                    phoneNumber, username, password, spnGradeLevel.getSelectedItem().toString());
                        });
                        warningBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        warningBuilder.show();
                        return;
                    }
                }
            }

            performAccountAction(firstName, middleName, lastName, email,
                    phoneNumber, username, password, grade);
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
    private void performAccountAction(String firstName, String middleName, String lastName,
                                      String email, String phoneNumber, String username,
                                      String password, String grade) {
        ModelAccount newAccount = null;

        if(accountType == AccountType.TEACHER)
            newAccount = new ModelAccount(
                    accountType, firstName, middleName, lastName, grade,
                    email, phoneNumber, username, password, imgBit
            );
        else if(accountType == AccountType.ADMIN)
            newAccount = new ModelAccount(
                    accountType, firstName, middleName, lastName,
                    email, phoneNumber, username, password, imgBit
            );

        switch (transactionType) {
            case CREATE:
                var result = DatabaseManager.createAccount(accountType, newAccount);
                if (result.success) {
                    Toast.makeText(c, "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(c, result.error, Toast.LENGTH_SHORT).show();
                }
                break;

            case EDIT:
                var editResult = DatabaseManager.editAccount(accountType, newAccount, id);
                if (editResult.success) {
                    Toast.makeText(c, "Account updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(c, editResult.error, Toast.LENGTH_SHORT).show();
                }
                break;

            case DELETE:
                boolean deleted = DatabaseManager.deleteAccount(id, accountType, c);
                Toast.makeText(c,
                        deleted ? "Account deleted successfully" : "Account not found",
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
    private void performAccountUpdate(String firstName, String middleName, String lastName,
                                      String email, String phoneNumber, String username,
                                      String password, String grade) {
        performAccountAction(firstName, middleName, lastName, email,
                phoneNumber, username, password, grade);
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
                findViewById(R.id.imgSpace2).setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }

        }else if (requestCode == REQUEST_CODE_CHOOSE_IMG && resultCode == RESULT_OK) {
            try {
                Uri imgUri = data.getData();
                imgBit = util.loadCorrectly(this, imgUri);
                imgProfile.setImageBitmap(
                        util.cropToSquare(imgBit)
                );
                imgProfile.setVisibility(View.VISIBLE);
                findViewById(R.id.imgSpace1).setVisibility(View.VISIBLE);
                findViewById(R.id.imgSpace2).setVisibility(View.VISIBLE);
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
