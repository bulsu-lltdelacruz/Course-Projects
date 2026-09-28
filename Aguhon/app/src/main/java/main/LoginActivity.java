package main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aguhan.R;

import database.AccountType;
import database.DatabaseManager;
import database.ErrorArr;
import session.SESSION;

public class LoginActivity extends AppCompatActivity {
    EditText etPassword;
    EditText etUsername;
    Button btnLogIn;
    boolean isPasswordShow = false;
    Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(0);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(SESSION.currentAdminAccount != null)
        {
            Intent intent = new Intent(c, DashboardActivity.class);
            startActivity(intent);
        }
        initialize();
        addListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListeners()
    {
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable rightDrawable = etPassword.getCompoundDrawables()[DRAWABLE_RIGHT];

                    if (rightDrawable != null) {
                        float x = event.getX();

                        int drawableStart = etPassword.getWidth() - etPassword.getPaddingRight() - rightDrawable.getIntrinsicWidth();

                        if (x >= drawableStart) {
                            Drawable newRightDrawable;
                            Drawable leftDrawable = ContextCompat.getDrawable(c, R.drawable.padlock);

                            if (!isPasswordShow) {
                                newRightDrawable = ContextCompat.getDrawable(c, R.drawable.icon_eye_visible);
                                etPassword.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, newRightDrawable, null);
                                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                isPasswordShow = true;
                            } else {
                                newRightDrawable = ContextCompat.getDrawable(c, R.drawable.icon_eye);
                                etPassword.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, newRightDrawable, null);
                                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                isPasswordShow = false;
                            }

                            etPassword.setSelection(etPassword.getText().length());
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(etUsername.getText().toString().trim());
                String password = String.valueOf(etPassword.getText().toString().trim());
                if(username.isBlank() || password.isBlank())
                {
                    Toast.makeText(c, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                ErrorArr error = DatabaseManager.checkAccount(AccountType.ADMIN, username, password);
                if(error.success)
                {
                    Intent intent = new Intent(c, DashboardActivity.class);
                    intent.putExtra("username", etUsername.getText());
                    intent.putExtra("password", etPassword.getText());
                    startActivity(intent);
                    Toast.makeText(c, "Welcome", Toast.LENGTH_SHORT).show();
                    if(DatabaseManager.getAccount(AccountType.ADMIN, username, password) != null)
                        SESSION.currentAdminAccount = DatabaseManager.getAccount(AccountType.ADMIN, username, password);
                    else
                        Toast.makeText(c, "notfound", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(c, error.error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void initialize()
    {
        DatabaseManager.start();
        etPassword = findViewById(R.id.etPassword);
        etUsername  = findViewById(R.id.etUsername);
        btnLogIn = findViewById(R.id.btnLogin);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(SESSION.currentAdminAccount != null)
        {
            finish();
            System.exit(0);
        }
    }
}