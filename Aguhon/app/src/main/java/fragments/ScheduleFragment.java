package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aguhan.R;

import main.ScheduleDetailsActivity;
import session.GradeLevel;

public class ScheduleFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View root;
    private LinearLayout llNursery;
    private LinearLayout llKinder;
    private LinearLayout llPrep;

    public ScheduleFragment() {
        // Required empty public constructor
    }
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        root = inflater.inflate(R.layout.fragment_schedule, container, false);
        EdgeToEdge.enable(getActivity());
        setupViews();
        addListeners();

        return root;
    }

    private void addListeners() {

        llNursery.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScheduleDetailsActivity.class);
            intent.putExtra("gradeLevel", GradeLevel.NURSERY);
            startActivity(intent);
        });
        llKinder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScheduleDetailsActivity.class);
            intent.putExtra("gradeLevel", GradeLevel.KINDER);
            startActivity(intent);
        });
        llPrep.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScheduleDetailsActivity.class);
            intent.putExtra("gradeLevel", GradeLevel.PREP);
            startActivity(intent);
        });

    }

    private void setupViews() {
        llNursery = root.findViewById(R.id.llNursery);
        llKinder = root.findViewById(R.id.llKinder);
        llPrep = root.findViewById(R.id.llPrep);
    }
}