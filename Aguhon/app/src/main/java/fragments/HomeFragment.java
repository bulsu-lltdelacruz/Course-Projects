package fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aguhan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import database.DatabaseManager;
import database.ModelAnnouncement;
import main.LoginActivity;
import main.util;
import session.SESSION;
import session.TransactionType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView txtWelcome;
    private TextView txtCurrentDate;

    private TextView txtTotalStudents;
    private TextView txtTotalTeachers;
    private TextView txtTotalSchedules;
    private TextView txtTotalAdmins;

    private TextView txtTotalCreates;
    private TextView txtTotalEdits;
    private TextView txtTotalDeletes;
    private TextView txtTotalTransactions;
    private LinearLayout announcementCard;

    private ImageButton btnLogOut;
    private View root;

    public HomeFragment() {

    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        EdgeToEdge.enable(getActivity());
        btnLogOut = root.findViewById(R.id.btnLogOut);

        // Welcome section
        txtWelcome = root.findViewById(R.id.txtWelcome);
        txtCurrentDate = root.findViewById(R.id.txtCurrentDate);

        // Overview cards
        txtTotalStudents = root.findViewById(R.id.txtTotalStudents);
        txtTotalTeachers = root.findViewById(R.id.txtTotalTeachers);
        txtTotalSchedules = root.findViewById(R.id.txtTotalSchedules);
        txtTotalAdmins = root.findViewById(R.id.txtTotalAdmins);

        // Recent activity stats
        txtTotalCreates = root.findViewById(R.id.txtTotalCreates);
        txtTotalEdits = root.findViewById(R.id.txtTotalEdits);
        txtTotalDeletes = root.findViewById(R.id.txtTotalDeletes);
        txtTotalTransactions = root.findViewById(R.id.txtTotalTransactions);


        announcementCard = root.findViewById(R.id.announcementCard1);
        addListeners();
        addSummary();
        return root;
    }
    private void addSummary()
    {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = sdf.format(now);

        txtWelcome.setText("Welcome, "+SESSION.currentAdminAccount.getFirstName()+" "+SESSION.currentAdminAccount.getLastName());
        txtCurrentDate.setText(formattedDate);
        txtTotalStudents.setText(String.valueOf(DatabaseManager.studentAccounts.size()));
        txtTotalTeachers.setText(String.valueOf(DatabaseManager.teacherAccounts.size()));
        txtTotalAdmins.setText(String.valueOf(DatabaseManager.adminAccounts.size()));
        txtTotalSchedules.setText(String.valueOf(DatabaseManager.scheduleList.size()));
        txtTotalCreates.setText(String.valueOf(DatabaseManager.getHistoryCount(TransactionType.CREATE)));
        txtTotalEdits.setText(String.valueOf(DatabaseManager.getHistoryCount(TransactionType.EDIT)));
        txtTotalDeletes.setText(String.valueOf(DatabaseManager.getHistoryCount(TransactionType.DELETE)));
        txtTotalTransactions.setText(String.valueOf(DatabaseManager.getHistoryCount()));

        if(DatabaseManager.announcementList.size()>0)
            announcementCard.removeAllViews();
        for(ModelAnnouncement announcement:DatabaseManager.announcementList)
        {
            announcementCard.addView(makeAnnouncementHeadTv(String.valueOf(announcement.gettitle())));
            announcementCard.addView(makeAnnouncementDateTv(String.valueOf(announcement.getDate()+", "+announcement.getTime())));
        }
    }
    private void addListeners()
    {
        btnLogOut.setOnClickListener(v -> {
            AlertDialog.Builder confirmLogOutBuilder =  util.makeAlert(root.getContext(),"Log Out","Are you sure you want to Log Out?");
            confirmLogOutBuilder.setPositiveButton("Yes", (dialog, which) -> {
                SESSION.currentAdminAccount = null;
                Intent loginIntent = new Intent(root.getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }).setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            confirmLogOutBuilder.show();
        });
    }

    public TextView makeAnnouncementHeadTv(String announcement)
    {
        TextView tvAnnouncement1Title = new TextView(root.getContext());
        tvAnnouncement1Title.setId(View.generateViewId());
        tvAnnouncement1Title.setText(announcement);
        tvAnnouncement1Title.setTypeface(null, Typeface.BOLD);
        tvAnnouncement1Title.setTextSize(18);
        tvAnnouncement1Title.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        return tvAnnouncement1Title;
    }
    public TextView makeAnnouncementDateTv(String date)
    {
        TextView tvAnnouncementDate = new TextView(root.getContext());
        tvAnnouncementDate.setId(View.generateViewId());
        tvAnnouncementDate.setText(date);
        tvAnnouncementDate.setTextColor(getResources().getColor(R.color.muted));
        tvAnnouncementDate.setTextSize(14);
        tvAnnouncementDate.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        return tvAnnouncementDate;
    }
}