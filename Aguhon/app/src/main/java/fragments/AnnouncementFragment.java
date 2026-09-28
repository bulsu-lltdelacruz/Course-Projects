package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.aguhan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.DatabaseManager;
import database.ModelAnnouncement;
import main.AnnouncementDetailsActivity;
import main.CreateAnnouncementActivity;
import main.CreateEditScheduleActivity;
import main.ScheduleDetailsActivity;
import main.util;
import session.GradeLevel;
import session.TransactionType;

public class AnnouncementFragment extends Fragment {
    private TableLayout table;
    private View root;
    private LinearLayout llNursery;
    private LinearLayout llKinder;
    private LinearLayout llPrep;
    Context c;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    public static AnnouncementFragment newInstance(String param1, String param2) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_announcement, container, false);
        EdgeToEdge.enable(getActivity());
        setupViews();
        addListeners();
        return root;
    }
    private void addListeners() {

        llNursery.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnnouncementDetailsActivity.class);
            intent.putExtra("gradeLevel", GradeLevel.NURSERY);
            startActivity(intent);
        });
        llKinder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnnouncementDetailsActivity.class);
            intent.putExtra("gradeLevel", GradeLevel.KINDER);
            startActivity(intent);
        });
        llPrep.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AnnouncementDetailsActivity.class);
            intent.putExtra("gradeLevel", GradeLevel.PREP);
            startActivity(intent);
        });

    }
    /*private void addListeners()
    {
        btnAdd.setOnClickListener(v ->
        {
            Intent switchIntent = new Intent(c, CreateAnnouncementActivity.class);
            switchIntent.putExtra("transactionType", TransactionType.CREATE);
            startActivity(switchIntent);
        });
    }*/
    private void setupViews() {
        llNursery = root.findViewById(R.id.llNursery);
        llKinder = root.findViewById(R.id.llKinder);
        llPrep = root.findViewById(R.id.llPrep);
    }
    /*private void setupViews() {
        table = root.findViewById(R.id.tableAnnouncements);
        btnAdd = root.findViewById(R.id.add);
        c = root.getContext();

    }

    private void renderTable() {
        for (ModelAnnouncement announcement: DatabaseManager.announcementList) {
            TableRow row = util.createAnnouncementRow(c, announcement.gettitle(), announcement.getTargetGrade(), announcement.getId());
            table.addView(row);
            table.addView(util.createDivider(c));
        }
    }*/

}
