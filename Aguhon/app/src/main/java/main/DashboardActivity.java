package main;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aguhan.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.AccountsFragment;
import fragments.AnnouncementFragment;
import fragments.HistoryFragment;
import fragments.HomeFragment;
import fragments.ScheduleFragment;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment currentFragment;
    BottomAppBar bottomAppBar;
    Context c = this;
    int paddingBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            return ViewCompat.onApplyWindowInsets(v, WindowInsetsCompat.CONSUMED);
        });
        initialize();
        addListeners();
        currentFragment = new HomeFragment();
        replaceFragment(currentFragment);
        paddingBottom = bottomAppBar.getPaddingBottom();

    }

    void addListeners()
    {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_home)
            {
                currentFragment = new HomeFragment();
                replaceFragment(currentFragment);

            }else if(id == R.id.nav_account)
            {
                currentFragment = new AccountsFragment();
                replaceFragment(currentFragment);
            }else if(id == R.id.nav_history)
            {
                currentFragment = new HistoryFragment();
                replaceFragment(currentFragment);
            }else if(id == R.id.nav_schedule)
            {
                currentFragment = new ScheduleFragment();
                replaceFragment(currentFragment);
            }
            else if(id == R.id.nav_announcement)
            {
                currentFragment = new AnnouncementFragment();
                replaceFragment(currentFragment);
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    void initialize()
    {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                bottomAppBar.setPadding(0,0,0,0);
                replaceFragment(currentFragment);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            bottomAppBar.setPadding(0,0,0,paddingBottom);
                replaceFragment(currentFragment);
        }
    }
}
