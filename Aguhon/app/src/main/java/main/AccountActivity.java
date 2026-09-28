package main;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aguhan.R;

public class AccountActivity extends AppCompatActivity {
    LinearLayout viewStudents;
    LinearLayout viewTeachers;
    LinearLayout viewAdmins;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        EdgeToEdge.enable(this);
        initialize();
        viewStudents.removeAllViews();
    }

    private void initialize()
    {
        viewStudents = findViewById(R.id.llViewStudentAccounts);
        viewTeachers = findViewById(R.id.llViewTeachertAccounts);
        viewAdmins = findViewById(R.id.llViewAdminAccounts);
    }
}
